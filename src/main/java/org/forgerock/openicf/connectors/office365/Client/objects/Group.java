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
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.Uid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Group object as found in Office365 AzureAD via Graph API
 *
 * Official schema:
 <EntityType Name="Group" BaseType="Microsoft.DirectoryServices.DirectoryObject" OpenType="true">
 <Property Name="description" Type="Edm.String"/>
 <Property Name="dirSyncEnabled" Type="Edm.Boolean"/>
 <Property Name="displayName" Type="Edm.String"/>
 <Property Name="lastDirSyncTime" Type="Edm.DateTime"/>
 <Property Name="mail" Type="Edm.String"/>
 <Property Name="mailNickname" Type="Edm.String"/>
 <Property Name="mailEnabled" Type="Edm.Boolean"/>
 <Property Name="onPremisesSecurityIdentifier" Type="Edm.String"/>
 <Property Name="provisioningErrors" Type="Collection(Microsoft.DirectoryServices.ProvisioningError)" Nullable="false"/>
 <Property Name="proxyAddresses" Type="Collection(Edm.String)" Nullable="false"/>
 <Property Name="securityEnabled" Type="Edm.Boolean"/>
 <NavigationProperty Name="appRoleAssignments" Relationship="Microsoft.DirectoryServices.Group_appRoleAssignments" ToRole="appRoleAssignments" FromRole="Group"/>
 </EntityType>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group extends O365Object {

    @JsonProperty
    public String description;

    @JsonProperty
    public Boolean dirSyncEnabled;

    @JsonProperty
    public String displayName;

    @JsonProperty
    public String id;

    @JsonProperty
    public String lastDirSyncTime;

    @JsonProperty
    public String mail;

    @JsonProperty
    public String mailNickname;

    @JsonProperty
    public Boolean mailEnabled;

    @JsonProperty
    public String onPremisesSecurityIdentifier;

    @JsonProperty
    public List<String> proxyAddresses = new ArrayList<String>();

    @JsonProperty
    public Boolean securityEnabled;

    Group() {}

    /**
     * Return a new Group built from a Set of Attributes.
     *
     * @param attrs the Attributes from which to build the new object
     * @return the new object
     */
    public Group(Set<Attribute> attrs) {
        for (Attribute attr : attrs) {
            if (attr.getName().equals("description")) {
                description = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("dirSyncEnabled")) {
                dirSyncEnabled = (Boolean) attr.getValue().get(0);
            } else if (attr.getName().equals(Name.NAME)) {
                displayName = (String) attr.getValue().get(0);
            } else if (attr.getName().equals(Uid.NAME)) {
                id = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("lastDirSyncTime")) {
                lastDirSyncTime = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("mail")) {
                mail = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("mailNickname")) {
                mailNickname = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("mailEnabled")) {
                mailEnabled = (Boolean) attr.getValue().get(0);
            } else if (attr.getName().equals("onPremisesSecurityIdentifier")) {
                onPremisesSecurityIdentifier = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("proxyAddresses")) {
                proxyAddresses = (List<String>) attr.getValue().get(0);
            } else if (attr.getName().equals("securityEnabled")) {
                securityEnabled = (Boolean) attr.getValue().get(0);
            }
        }
    }

    @Override
    public Set<Attribute> toAttributes() {
        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("description", description));
        attrs.add(AttributeBuilder.build("dirSyncEnabled", dirSyncEnabled));
        attrs.add(AttributeBuilder.build("displayName", displayName));
        attrs.add(AttributeBuilder.build("id", id));
        attrs.add(AttributeBuilder.build("lastDirSyncTime", lastDirSyncTime));
        attrs.add(AttributeBuilder.build("mail", mail));
        attrs.add(AttributeBuilder.build("mailNickname", mailNickname));
        attrs.add(AttributeBuilder.build("mailEnabled", mailEnabled));
        attrs.add(AttributeBuilder.build("onPremisesSecurityIdentifier", onPremisesSecurityIdentifier));
        attrs.add(AttributeBuilder.build("proxyAddresses", proxyAddresses));
        attrs.add(AttributeBuilder.build("securityEnabled", securityEnabled));
        return attrs;
    }

    @JsonIgnore
    @Override
    public List<SchemaAttribute> getSchemaAttributes() {
        final Boolean BOOL = true;
        final String STRING = "";

        return new ArrayList<SchemaAttribute>() {{
            add(new SchemaAttribute("id", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("description", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("dirSyncEnabled", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("displayName", STRING).setReturned(true).setReadable(true).setRequired(true));
            add(new SchemaAttribute("lastDirSyncTime", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("mail", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("mailNickname", STRING).setReturned(true).setReadable(true).setRequired(true));
            add(new SchemaAttribute("mailEnabled", BOOL).setReturned(true).setReadable(true).setRequired(true));
            add(new SchemaAttribute("onPremisesSecurityIdentifier", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("proxyAddresses", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("securityEnabled", BOOL).setReturned(true).setReadable(true).setRequired(true));
        }};
    }
}
