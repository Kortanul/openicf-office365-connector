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

import org.forgerock.http.Client;
import org.forgerock.http.HttpApplicationException;
import org.forgerock.http.apache.sync.SyncHttpClientProvider;
import org.forgerock.http.handler.HttpClientHandler;
import org.forgerock.http.header.ContentTypeHeader;
import org.forgerock.http.protocol.Request;
import org.forgerock.http.protocol.Response;
import org.forgerock.http.protocol.Status;
import org.forgerock.http.spi.Loader;
import org.forgerock.json.JsonValue;
import org.forgerock.util.Function;
import org.forgerock.util.Options;
import org.forgerock.util.promise.NeverThrowsException;
import org.forgerock.util.promise.Promise;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.framework.common.exceptions.ConnectorException;

import java.io.IOException;
import java.net.URI;

import static org.forgerock.json.JsonValue.json;
import static org.forgerock.http.handler.HttpClientHandler.OPTION_LOADER;
import static org.forgerock.json.JsonValue.object;

/**
 * Basic HTTP client
 */
public class HttpClient {
    private static final Log logger = Log.getLog(HttpClient.class);

    private Client httpClient;

    private final AuthSigner signer;

    public HttpClient() {
        this.signer = new PassthroughSigner();
        initClient();
    }

    public HttpClient(AuthSigner signer) {
        this.signer = signer;
        initClient();
    }

    private void initClient() {
        try {
            httpClient = new Client(
                    new HttpClientHandler(
                            Options.defaultOptions()
                                    .set(OPTION_LOADER, new Loader() {
                                        @Override
                                        public <S> S load(Class<S> service, Options options) {
                                            return service.cast(new SyncHttpClientProvider());
                                        }
                                    })
                    ));
        } catch (HttpApplicationException e) {
            logger.error(e, "HttpClientHandler failure");
            throw new ConnectorException("HttpClientHandler failure", e);
        }
    }

    public JsonValue sendGetRequest(URI uri) {
        return _sendGetRequest("GET", uri);
    }

    public JsonValue sendDeleteRequest(URI uri) {
        return _sendGetRequest("DELETE", uri);
    }

    public JsonValue sendQueryRequest(URI uri) {
        return _sendGetRequest("QUERY", uri);
    }

    public JsonValue sendPostRequest(URI uri, String contentType, Object body) {
        return _sendPostRequest("POST", uri, contentType, body);
    }

    public JsonValue sendPutRequest(URI uri, String contentType, Object body) {
        return _sendPostRequest("PUT", uri, contentType, body);
    }

    public JsonValue sendPatchRequest(URI uri, String contentType, Object body) {
        return _sendPostRequest("PATCH", uri, contentType, body);
    }

    private JsonValue _sendGetRequest(String method, URI uri) {
        return _sendRequest(method, uri, null, null);
    }

    private JsonValue _sendPostRequest(String method, URI uri, String contentType, Object body) {
        return _sendRequest(method, uri, contentType, body);
    }

    private JsonValue _sendRequest(String method, URI uri, String contentType, Object body) {
        Request request = new Request()
                .setMethod(method)
                .setUri(uri);
        signer.sign(request);
        if (contentType != null) {
            request.getHeaders().put(ContentTypeHeader.NAME, contentType);
        }
        if (body != null) {
            request.setEntity(body);
        }
        return httpClient
                .send(request)
                .then(
                        new Function<Response, JsonValue, NeverThrowsException>() {
                            @Override
                            public JsonValue apply(Response response) {
                                try {
                                    if (response.getStatus().isSuccessful()) {
                                        if (response.getStatus().equals(Status.NO_CONTENT)) {
                                            return json(object());
                                        }
                                        String type = response.getHeaders().get("Content-Type") != null
                                                ? response.getHeaders().get("Content-Type").getFirstValue()
                                                : null;
                                        if (type != null && type.startsWith("application/xml")) {
                                            // no way to pass this back, also not needed currently
                                            return json(object());
                                        } else if (type != null && type.startsWith("application/json")) {
                                            return json(response.getEntity().getJson());
                                        }
                                        return json(object());
                                    } else {
                                        throw new IOException(response.getStatus().getReasonPhrase());
                                    }
                                } catch (IOException e) {
                                    throw new IllegalStateException("Unable to perform request", e);
                                }
                            }
                        },
                        new Function<NeverThrowsException, JsonValue, NeverThrowsException>() {
                            @Override
                            public JsonValue apply(NeverThrowsException e) {
                                // return null on Exceptions
                                return null;
                            }
                        })
                .getOrThrowUninterruptibly();
    }
}
