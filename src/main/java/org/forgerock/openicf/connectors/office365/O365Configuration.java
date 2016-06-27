/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2016 ForgeRock AS.
 */

package org.forgerock.openicf.connectors.office365;

import org.identityconnectors.common.StringUtil;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.spi.AbstractConfiguration;
import org.identityconnectors.framework.spi.ConfigurationProperty;

import java.util.HashMap;
import java.util.Map;


/**
 * Extends the {@link AbstractConfiguration} class to provide all the necessary
 * parameters to initialize the O365 Connector.
 *
 */
public class O365Configuration extends AbstractConfiguration {

    /** The Office365 host and token refresh uri */
    public final static String O365HOST = "https://graph.microsoft.com/v1.0/";
    public final static String O365TOKEN_REFRESH_URI = "https://login.windows.net/common/oauth2/token";

    /** UID field for ACCOUNT object type */
    public final static String FIELD_ACCOUNT_UID = "id";

    /** NAME field for ACCOUNT object type */
    public final static String FIELD_ACCOUNT_NAME = "userPrincipalName";

    /** UID field for GROUP object type */
    public final static String FIELD_GROUP_ID = "id";

    /** NAME field for GROUP object type */
    public final static String FIELD_GROUP_NAME = "displayName";

    /** The Azure tenant name */
    private String tenant;

    /** The OAuth2 client identifier for the tenant that the request targets. */
    private String clientId;

    /** OAuth2 client secret  */
    private GuardedString clientSecret = null;

    /** OAuth2 access token */
    private String accessToken = null;

    /** OAuth2 expiration timestamp for accessToken */
    private Long tokenExpiration = null;

    /** OAuth2 refresh token */
    private String refreshToken = null;

    /** Map of ObjectClasses to O365 EntitySet Strings */
    private Map<ObjectClass, String> objectClassEntitySet = new HashMap<ObjectClass, String>();
    {
        objectClassEntitySet.put(ObjectClass.ACCOUNT, "User");
        objectClassEntitySet.put(ObjectClass.GROUP, "Group");
    }

    /** Map of ObjectClasses to O365 Graph API UIR components */
    private Map<ObjectClass, String> objectClassURIComponent = new HashMap<ObjectClass, String>();
    {
        objectClassURIComponent.put(ObjectClass.ACCOUNT, "users");
        objectClassURIComponent.put(ObjectClass.GROUP, "groups");
    }

    /**
     * Constructor.
     */
    public O365Configuration() {

    }

    @ConfigurationProperty(order = 1, displayMessageKey = "tenant.display",
            groupMessageKey = "basic.group", helpMessageKey = "tenant.help", required = true,
            confidential = false)
    public String getTenant() {
        return tenant;
    }

    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    @ConfigurationProperty(order = 2, displayMessageKey = "clientId.display",
            groupMessageKey = "basic.group", helpMessageKey = "clientId.help", required = true,
            confidential = false)
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @ConfigurationProperty(order = 3, displayMessageKey = "clientSecret.display",
            groupMessageKey = "basic.group", helpMessageKey = "clientSecret.help", required = true,
            confidential = true)
    public GuardedString getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(GuardedString clientSecret) {
        this.clientSecret = clientSecret;
    }

    @ConfigurationProperty(order = 4, displayMessageKey = "accessToken.display",
            groupMessageKey = "basic.group", helpMessageKey = "accessToken.help",
            required = true, confidential = false)
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @ConfigurationProperty(order = 5, displayMessageKey = "tokenExpiration.display",
            groupMessageKey = "basic.group", helpMessageKey = "tokenExpiration.help",
            confidential = false)
    public Long getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Long tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    @ConfigurationProperty(order = 6, displayMessageKey = "refreshToken.display",
            groupMessageKey = "basic.group", helpMessageKey = "refreshToken.help", required = true,
            confidential = false)
    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @ConfigurationProperty(order = 7, displayMessageKey = "accountEntitySet.display",
            groupMessageKey = "o365.group", helpMessageKey = "accountEntitySet.help",
            required = true, confidential = false)
    public String getAccountEntitySet() {
        return objectClassEntitySet.get(ObjectClass.ACCOUNT);
    }

    public void setAccountEntitySet(String entitySet) {
        this.objectClassEntitySet.put(ObjectClass.ACCOUNT, entitySet);
    }

    @ConfigurationProperty(order = 8, displayMessageKey = "groupEntitySet.display",
            groupMessageKey = "o365.group", helpMessageKey = "groupEntitySet.help",
            required = true, confidential = false)
    public String getGroupEntitySet() {
        return objectClassEntitySet.get(ObjectClass.GROUP);
    }

    public void setGroupEntitySet(String entitySet) {
        this.objectClassEntitySet.put(ObjectClass.GROUP, entitySet);
    }

    /**
     * Retrieve the EntitySet name corresponding to
     * @param objClass
     * @return
     */
    public String getEntitySet(ObjectClass objClass) {
        return objectClassEntitySet.get(objClass);
    }

    public ObjectClass getEntitySetObjectClass(String entitySet) {
        for (ObjectClass oc : objectClassEntitySet.keySet()) {
            if (objectClassEntitySet.get(oc).equals(entitySet)) {
                return oc;
            }
        }
        return null;
    }

    @ConfigurationProperty(order = 8, displayMessageKey = "accountURIComponent.display",
            groupMessageKey = "o365.group", helpMessageKey = "accountURIComponent.help",
            required = true, confidential = false)
    public String getAccountURIComponent() {
        return objectClassURIComponent.get(ObjectClass.ACCOUNT);
    }

    public void setAccountURIComponent(String uriComponent) {
        this.objectClassURIComponent.put(ObjectClass.ACCOUNT, uriComponent);
    }

    @ConfigurationProperty(order = 9, displayMessageKey = "groupURIComponent.display",
            groupMessageKey = "o365.group", helpMessageKey = "groupURIComponent.help",
            required = true, confidential = false)
    public String getGroupURIComponent() {
        return objectClassURIComponent.get(ObjectClass.GROUP);
    }

    public void setGroupURIComponent(String uriComponent) {
        this.objectClassURIComponent.put(ObjectClass.GROUP, uriComponent);
    }

    /**
     * Retrieve the EntitySet name corresponding to
     * @param objClass
     * @return
     */
    public String getURIComponent(ObjectClass objClass) {
        return objectClassURIComponent.get(objClass);
    }

    public ObjectClass getURIComponentObjectClass(String entitySet) {
        for (ObjectClass oc : objectClassEntitySet.keySet()) {
            if (objectClassEntitySet.get(oc).equals(entitySet)) {
                return oc;
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void validate() {
        if (StringUtil.isBlank(clientId)) {
            throw new IllegalArgumentException("ClientId cannot be null or empty.");
        }

        if (null == accessToken) {
            throw new IllegalArgumentException("Access Token cannot be null or empty.");
        }

        if (null == clientSecret) {
            throw new IllegalArgumentException("Client Secret cannot be null or empty.");
        }
    }
}
