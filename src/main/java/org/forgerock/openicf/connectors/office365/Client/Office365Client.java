/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance
 * with the License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for
 * the specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file
 * and include the License file at legal/CDDLv1.0.txt. If applicable, add the following
 * below the CDDL Header, with the fields enclosed by brackets [] replaced by your
 * own identifying information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2016 ForgeRock AS.
 */
package org.forgerock.openicf.connectors.office365.client;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.forgerock.http.header.GenericHeader;
import org.forgerock.http.protocol.Request;
import org.forgerock.json.JsonValue;
import org.forgerock.openicf.connectors.office365.O365Configuration;
import org.forgerock.openicf.connectors.office365.client.objects.Group;
import org.forgerock.openicf.connectors.office365.client.objects.CreateUser;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.forgerock.openicf.connectors.office365.client.objects.O365Object;
import org.forgerock.openicf.connectors.office365.client.objects.ReadUser;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.SecurityUtil;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.ObjectClass;

/**
 * Client class for interacting with Office365 AzureAD via its Graph API
 */
public class Office365Client {
    /**
     * Setup logging for the {@link Office365Client}.
     */
    private static final Log logger = Log.getLog(Office365Client.class);

    public static final String SEPARATOR = "/";

    private static final ObjectMapper mapper = new ObjectMapper();

    private final HttpClient httpClient;

    /* Pagination properties */
    private Integer top;
    private String skipToken;

    private O365Configuration configuration;

    /**
     * Create a Client for a given configuration
     *
     * @param configuration
     */
    public Office365Client(final O365Configuration configuration) {
        this.configuration = configuration;
        this.httpClient = new HttpClient(new OAuth2Signer(configuration, configuration.O365TOKEN_REFRESH_URI));
    }

    /**
     * Fluently return a Client with paging properties set
     *
     * @param top The $top value for an O365 request
     * @param skipToken The $skipToken value for an O365 request
     * @return a copy of the calling client with paging properties set
     */
    public Office365Client withPaging(Integer top, String skipToken) {
        Office365Client client = new Office365Client(this.configuration);
        client.top = top;
        client.skipToken = skipToken;
        return client;
    }

    /**
     * Return the next skipToken or null if no more pages available
     *
     * @return next skipToken
     */
    public String getSkipToken() {
        return skipToken;
    }

    private URI getO365Uri(ObjectClass objectClass, String objectId, String filter) {
        String uri = configuration.O365HOST
                + (objectClass == null
                ? ""
                : configuration.getURIComponent(objectClass) + SEPARATOR);
        if (objectId != null) {
            uri += objectId + SEPARATOR;
        }
        String sepChar = "?";
        if (filter != null) {
            try {
                uri += sepChar + "$filter=" + URLEncoder.encode(filter.trim(), "utf-8");
            } catch (UnsupportedEncodingException e) {
                logger.warn(e, "Bad URI syntax for filter: {}", filter);
                throw new ConnectorException(e);
            }
            sepChar = "&";
        }
        if (top != null && top > -1) {
            uri += sepChar + "$top=" + top;
            sepChar = "&";
        }
        if (skipToken != null) {
            uri += sepChar + "$skipToken=" + skipToken;
        }

        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            logger.warn(e, "Bad URI syntax: {}", uri);
            throw new ConnectorException(e);
        }
    }

    private URI getO365Uri(ObjectClass objectClass, String objectId) {
        return getO365Uri(objectClass, objectId, null);
    }

    private URI getO365Uri(ObjectClass objectClass) {
        return getO365Uri(objectClass, null, null);
    }

    /**
     * Test the connection
     *
     * @throws Exception on failure
     */
    public void testConnection() throws URISyntaxException {
        httpClient.sendGetRequest(new URI("https://graph.microsoft.com/v1.0/$metadata"));
    }

    /**
     * Fetch an object of objectClass whose id is objectId
     *
     * @param objectClass ObjectClass of the object
     * @param objectId id of the object
     * @return the object
     */
    public O365Object getObject(ObjectClass objectClass, String objectId) {
        try {
            JsonValue json = httpClient.sendGetRequest(getO365Uri(objectClass, objectId));
            O365Object object = mapper.readValue(json.toString(),
                    objectClass.equals(ObjectClass.ACCOUNT)
                        ? CreateUser.class
                        : Group.class);
            return object;
        } catch (Exception e) {
            logger.error(e, "Failed to retrieve object: {0} {1}", configuration.getEntitySet(objectClass), objectId);
            throw new ConnectorException("Failed to retrieve entity", e);
        }
    }

    public List<O365Object> getObjects(ObjectClass objectClass, String filter) {
        try {
            List<O365Object> objects = new ArrayList<O365Object>();
            JsonValue json = httpClient.sendGetRequest(getO365Uri(objectClass, null, filter));
            for (JsonValue oneObj : json.get("value")) {
                objects.add(mapper.readValue(oneObj.toString(),
                        objectClass.equals(ObjectClass.ACCOUNT)
                                ? CreateUser.class
                                : Group.class));
            }

            // Capture the nextSkipToken if there was one
            if (json.get("@odata.nextSkipToken").isNotNull()) {
                String url = json.get("@odata.nextSkipToken").asString();
                if (url != null) {
                    String[] urlbits = url.split("nextSkipToken=");
                    if (urlbits.length == 2) {
                        String[] urlbitsbits = urlbits[1].split("&");
                        skipToken = urlbitsbits[0];
                    }
                }
            }
            return objects;
        } catch (Exception e) {
            logger.error(e, "Failed to retrieve objects: {0} {1}", configuration.getEntitySet(objectClass), filter);
            throw new ConnectorException("Failed to retrieve objects", e);
        }
    }

    public O365Object createObject(ObjectClass objectClass, O365Object object) {
        try {
            JsonValue json = httpClient.sendPostRequest(getO365Uri(objectClass), "application/json",
                    mapper.writeValueAsBytes(object));
            O365Object obj = mapper.readValue(json.toString(),
                    objectClass.equals(ObjectClass.ACCOUNT)
                            ? ReadUser.class
                            : Group.class);
            return obj;
        } catch (Exception e) {
            logger.error(e, "Failed to create object: {0}", configuration.getEntitySet(objectClass));
            throw new ConnectorException("Failed to create object", e);
        }
    }

    /**
     * Delete an object by unique name
     *
     * @param objectClass ObjectClass of the object
     * @param objectId name property of the object
     */
    public void deleteObject(ObjectClass objectClass, String objectId) {
        try {
            httpClient.sendDeleteRequest(getO365Uri(objectClass, objectId));
        } catch (Exception e) {
            logger.error(e, "Failed to delete object: {0} {1}", configuration.getEntitySet(objectClass), objectId);
            throw new ConnectorException("Failed to delete object", e);
        }
    }

    public void updateObject(ObjectClass objectClass, String objectId, O365Object object) {
        try {
            httpClient.sendPatchRequest(getO365Uri(objectClass, objectId), "application/json",
                    mapper.writeValueAsBytes(object));
        } catch (Exception e) {
            logger.error(e, "Failed to update object: {0} {1}", configuration.getEntitySet(objectClass), objectId);
            throw new ConnectorException("Failed to update object", e);
        }
    }

    private class OAuth2Signer implements AuthSigner {
        private final O365Configuration config;
        private final String tokenRefreshUrl;

        public OAuth2Signer(O365Configuration config, String tokenRefreshUrl) {
            this.config = config;
            this.tokenRefreshUrl = tokenRefreshUrl;
        }

        @Override
        public void sign(Request request) {
            if (config.getAccessToken() == null
                    || config.getTokenExpiration().compareTo(new Date().getTime() / 1000) < 0) {
                // OAuth2 token is expired, attempt to refresh it
                try {
                    HttpPost httprequest = new HttpPost(new URI(tokenRefreshUrl));
                    httprequest.setHeader("content-type", "application/x-www-form-urlencoded");
                    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
                    nameValuePairs.add(new BasicNameValuePair("grant_type", "refresh_token"));
                    nameValuePairs.add(new BasicNameValuePair("refresh_token", config.getRefreshToken()));
                    nameValuePairs.add(new BasicNameValuePair("client_secret",
                            SecurityUtil.decrypt(config.getClientSecret())));
                    nameValuePairs.add(new BasicNameValuePair("client_id", config.getClientId()));
                    httprequest.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = HttpClients.createDefault().execute(httprequest);
                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {
                        // success, parse response body and set new member values for oConfig
                        String json = EntityUtils.toString(response.getEntity());
                        OAuthRefreshResponse oResponse = new ObjectMapper().readValue(json, OAuthRefreshResponse.class);
                        config.setAccessToken(oResponse.accessToken);
                        config.setRefreshToken(oResponse.refreshToken);
                        config.setTokenExpiration(oResponse.expiresOn);
                        // TODO: Find a way to propagate these new tokens to wherever the config came from
                    } else {
                        throw new ConnectorException("Failed : HTTP error code : "
                                + response.getStatusLine().getStatusCode());
                    }
                } catch (Exception e) {
                    throw new ConnectorException(e);
                }
            }

            request.getHeaders().put(new GenericHeader("Authorization", "Bearer " + configuration.getAccessToken()));
            request.getHeaders().put(new GenericHeader("Accept", "application/json;odata.metadata=full"));
        }
    }
}
