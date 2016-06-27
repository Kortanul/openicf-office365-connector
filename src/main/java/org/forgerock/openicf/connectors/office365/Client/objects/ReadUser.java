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
package org.forgerock.openicf.connectors.office365.client.objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User object as read from Office365 AzureAD via Graph API
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReadUser extends O365Object {

    @JsonProperty
    public String displayName;

    @JsonProperty
    public String givenName;

    @JsonProperty
    public String id;

    @JsonProperty
    public String jobTitle;

    @JsonProperty
    public String mail;

    @JsonProperty
    public String mobilePhone;

    @JsonProperty
    public String preferredLanguage;

    @JsonProperty
    public String surname;

    @JsonProperty
    public String userPrincipalName;

    ReadUser() {}

    /**
     * Return a new User built from a Set of Attributes.
     *
     * @param attrs the Attributes frmo which to build the new object
     * @return the new object
     */
    public ReadUser(Set<Attribute> attrs) {
        super(attrs);
        for (Attribute attr : attrs) {
            if (attr.getName().equals("displayName")) {
                displayName = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("givenName")) {
                givenName = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("id")) {
                id = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("jobTitle")) {
                jobTitle = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("mail")) {
                mail = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("mobilePhone")) {
                mobilePhone = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("preferredLanguage")) {
                preferredLanguage = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("surname")) {
                surname = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("userPrincipalName")) {
                userPrincipalName = (String) attr.getValue().get(0);
            }
        }
    }

    @Override
    public Set<Attribute> toAttributes() {
        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("displayName", displayName));
        attrs.add(AttributeBuilder.build("givenName", givenName));
        attrs.add(AttributeBuilder.build("id", id));
        attrs.add(AttributeBuilder.build("jobTitle", jobTitle));
        attrs.add(AttributeBuilder.build("mail", mail));
        attrs.add(AttributeBuilder.build("mobilePhone", mobilePhone));
        attrs.add(AttributeBuilder.build("preferredLanguage", preferredLanguage));
        attrs.add(AttributeBuilder.build("surname", surname));
        attrs.add(AttributeBuilder.build("userPrincipalName", userPrincipalName));
        return attrs;
    }

    @JsonIgnore
    @Override
    public List<SchemaAttribute> getSchemaAttributes() {
        final String STRING = "";

        return new ArrayList<SchemaAttribute>() {{
            add(new SchemaAttribute("displayName", STRING).setRequired(true));
            add(new SchemaAttribute("givenName", STRING));
            add(new SchemaAttribute("id", STRING));
            add(new SchemaAttribute("jobTitle", STRING));
            add(new SchemaAttribute("mail", STRING));
            add(new SchemaAttribute("mobilePhone", STRING));
            add(new SchemaAttribute("preferredLanguage", STRING));
            add(new SchemaAttribute("surname", STRING));
            add(new SchemaAttribute("userPrincipalName", STRING).setRequired(true));
        }};
    }
}
