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
 * User object as found in Office365 AzureAD via Graph API
 *
 * Official schema:
 <EntityType Name="User" BaseType="Microsoft.DirectoryServices.DirectoryObject" OpenType="true">
 <Property Name="accountEnabled" Type="Edm.Boolean"/>
 <Property Name="signInNames" Type="Collection(Microsoft.DirectoryServices.SignInName)" Nullable="false"/>
 <Property Name="assignedLicenses" Type="Collection(Microsoft.DirectoryServices.AssignedLicense)" Nullable="false"/>
 <Property Name="assignedPlans" Type="Collection(Microsoft.DirectoryServices.AssignedPlan)" Nullable="false"/>
 <Property Name="city" Type="Edm.String"/>
 <Property Name="companyName" Type="Edm.String"/>
 <Property Name="country" Type="Edm.String"/>
 <Property Name="creationType" Type="Edm.String"/>
 <Property Name="department" Type="Edm.String"/>
 <Property Name="dirSyncEnabled" Type="Edm.Boolean"/>
 <Property Name="displayName" Type="Edm.String"/>
 <Property Name="facsimileTelephoneNumber" Type="Edm.String"/>
 <Property Name="givenName" Type="Edm.String"/>
 <Property Name="immutableId" Type="Edm.String"/>
 <Property Name="isCompromised" Type="Edm.Boolean"/>
 <Property Name="jobTitle" Type="Edm.String"/>
 <Property Name="lastDirSyncTime" Type="Edm.DateTime"/>
 <Property Name="mail" Type="Edm.String"/>
 <Property Name="mailNickname" Type="Edm.String"/>
 <Property Name="mobile" Type="Edm.String"/>
 <Property Name="onPremisesSecurityIdentifier" Type="Edm.String"/>
 <Property Name="otherMails" Type="Collection(Edm.String)" Nullable="false"/>
 <Property Name="passwordPolicies" Type="Edm.String"/>
 <Property Name="passwordProfile" Type="Microsoft.DirectoryServices.PasswordProfile"/>
 <Property Name="physicalDeliveryOfficeName" Type="Edm.String"/>
 <Property Name="postalCode" Type="Edm.String"/>
 <Property Name="preferredLanguage" Type="Edm.String"/>
 <Property Name="provisionedPlans" Type="Collection(Microsoft.DirectoryServices.ProvisionedPlan)" Nullable="false"/>
 <Property Name="provisioningErrors" Type="Collection(Microsoft.DirectoryServices.ProvisioningError)" Nullable="false"/>
 <Property Name="proxyAddresses" Type="Collection(Edm.String)" Nullable="false"/>
 <Property Name="refreshTokensValidFromDateTime" Type="Edm.DateTime"/>
 <Property Name="sipProxyAddress" Type="Edm.String"/>
 <Property Name="state" Type="Edm.String"/>
 <Property Name="streetAddress" Type="Edm.String"/>
 <Property Name="surname" Type="Edm.String"/>
 <Property Name="telephoneNumber" Type="Edm.String"/>
 <Property Name="thumbnailPhoto" Type="Edm.Stream" Nullable="false"/>
 <Property Name="usageLocation" Type="Edm.String"/>
 <Property Name="userPrincipalName" Type="Edm.String"/>
 <Property Name="userType" Type="Edm.String"/>
 <NavigationProperty Name="appRoleAssignments" Relationship="Microsoft.DirectoryServices.User_appRoleAssignments" ToRole="appRoleAssignments" FromRole="User"/>
 <NavigationProperty Name="licenseDetails" Relationship="Microsoft.DirectoryServices.User_licenseDetails" ToRole="licenseDetails" FromRole="User"/>
 <NavigationProperty Name="oauth2PermissionGrants" Relationship="Microsoft.DirectoryServices.User_oauth2PermissionGrants" ToRole="oauth2PermissionGrants" FromRole="User"/>
 <NavigationProperty Name="ownedDevices" Relationship="Microsoft.DirectoryServices.User_ownedDevices" ToRole="ownedDevices" FromRole="User"/>
 <NavigationProperty Name="registeredDevices" Relationship="Microsoft.DirectoryServices.User_registeredDevices" ToRole="registeredDevices" FromRole="User"/>
 </EntityType>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateUser extends O365Object {

    @JsonProperty
    public Boolean accountEnabled;

    @JsonProperty
    public String city;

    @JsonProperty
    public String companyName;

    @JsonProperty
    public String country;

    @JsonProperty
    public String creationType;

    @JsonProperty
    public String department;

    @JsonProperty
    public Boolean dirSyncEnabled;

    @JsonProperty
    public String displayName;

    @JsonProperty
    public String facsimileTelephoneNumber;

    @JsonProperty
    public String givenName;

    @JsonProperty("id")
    public String id;

    @JsonProperty
    public Boolean isCompromised;

    @JsonProperty
    public String jobTitle;

    @JsonProperty
    public String lastDirSyncTime;

    @JsonProperty
    public String mail;

    @JsonProperty
    public String mailNickname;

    @JsonProperty("mobile")
    public String mobilePhone;

    @JsonProperty
    public String onPremisesSecurityIdentifier;

    @JsonProperty
    public String passwordPolicies;

    @JsonProperty
    public PasswordProfile passwordProfile = new PasswordProfile();

    @JsonProperty
    public String physicalDeliveryOfficeName;

    @JsonProperty
    public String postalCode;

    @JsonProperty
    public String preferredLanguage;

    @JsonProperty
    public String refreshTokensValidFromDateTime;

    @JsonProperty
    public String sipProxyAddress;

    @JsonProperty
    public String state;

    @JsonProperty
    public String streetAddress;

    @JsonProperty
    public String surname;

    @JsonProperty
    public String telephoneNumber;

    @JsonProperty
    public String thumbnailPhoto;

    @JsonProperty
    public String usageLocation;

    @JsonProperty
    public String userPrincipalName;

    @JsonProperty
    public String userType;

    CreateUser() {}

    /**
     * Return a new User built from a Set of Attributes.
     *
     * @param attrs the Attributes from which to build the new object
     * @return the new object
     */
    public CreateUser(Set<Attribute> attrs) {
        super(attrs);
        for (Attribute attr : attrs) {
            if (attr.getName().equals("accountEnabled")) {
                accountEnabled = (Boolean) attr.getValue().get(0);
            } else if (attr.getName().equals("city")) {
                city = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("companyName")) {
                companyName = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("country")) {
                country = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("creationType")) {
                creationType = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("department")) {
                department = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("dirSyncEnabled")) {
                dirSyncEnabled = (Boolean) attr.getValue().get(0);
            } else if (attr.getName().equals("displayName")) {
                displayName = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("facsimileTelephoneNumber")) {
                facsimileTelephoneNumber = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("givenName")) {
                givenName = (String) attr.getValue().get(0);
            } else if (attr.getName().equals(Uid.NAME)) {
                id = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("isCompromised")) {
                isCompromised = (Boolean) attr.getValue().get(0);
            } else if (attr.getName().equals("jobTitle")) {
                jobTitle = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("lastDirSyncTime")) {
                lastDirSyncTime = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("mail")) {
                mail = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("mailNickname")) {
                mailNickname = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("mobilePhone")) {
                mobilePhone = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("onPremisesSecurityIdentifier")) {
                onPremisesSecurityIdentifier = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("passwordPolicies")) {
                passwordPolicies = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("physicalDeliveryOfficeName")) {
                physicalDeliveryOfficeName = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("postalCode")) {
                postalCode = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("preferredLanguage")) {
                preferredLanguage = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("refreshTokensValidFromDateTime")) {
                refreshTokensValidFromDateTime = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("sipProxyAddress")) {
                sipProxyAddress = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("state")) {
                state = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("streetAddress")) {
                streetAddress = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("surname")) {
                surname = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("telephoneNumber")) {
                telephoneNumber = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("thumbnailPhoto")) {
                thumbnailPhoto = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("usageLocation")) {
                usageLocation = (String) attr.getValue().get(0);
            } else if (attr.getName().equals(Name.NAME)) {
                userPrincipalName = (String) attr.getValue().get(0);
            } else if (attr.getName().equals("userType")) {
                userType = (String) attr.getValue().get(0);
            }
        }
        passwordProfile = new PasswordProfile(attrs);
    }

    @JsonIgnore
    @Override
    public List<SchemaAttribute> getSchemaAttributes() {
        final Boolean BOOL = true;
        final String STRING = "";

        return new ArrayList<SchemaAttribute>() {{
            add(new SchemaAttribute("accountEnabled", BOOL).setRequired(true));
            add(new SchemaAttribute("city", STRING));
            add(new SchemaAttribute("companyName", STRING));
            add(new SchemaAttribute("country", STRING));
            add(new SchemaAttribute("creationType", STRING));
            add(new SchemaAttribute("department", STRING));
            add(new SchemaAttribute("dirSyncEnabled", BOOL));
            add(new SchemaAttribute("displayName", STRING).setRequired(true).setReturned(true).setReadable(true));
            add(new SchemaAttribute("facsimileTelephoneNumber", STRING));
            add(new SchemaAttribute("givenName", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("id", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("isCompromised", BOOL));
            add(new SchemaAttribute("jobTitle", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("lastDirSyncTime", STRING));
            add(new SchemaAttribute("mail", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("mailNickname", STRING));
            add(new SchemaAttribute("mobilePhone", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("onPremisesSecurityIdentifier", STRING));
            add(new SchemaAttribute("passwordPolicies", STRING));
            addAll(passwordProfile.getSchemaAttributes());
            add(new SchemaAttribute("physicalDeliveryOfficeName", STRING));
            add(new SchemaAttribute("postalCode", STRING));
            add(new SchemaAttribute("preferredLanguage", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("refreshTokensValidFromDateTime", STRING));
            add(new SchemaAttribute("sipProxyAddress", STRING));
            add(new SchemaAttribute("state", STRING));
            add(new SchemaAttribute("streetAddress", STRING));
            add(new SchemaAttribute("surname", STRING).setReturned(true).setReadable(true));
            add(new SchemaAttribute("telephoneNumber", STRING));
            add(new SchemaAttribute("thumbnailPhoto", STRING));
            add(new SchemaAttribute("usageLocation", STRING));
            add(new SchemaAttribute("userPrincipalName", STRING)
                    .setRequired(true).setReturned(true).setReadable(true));
            add(new SchemaAttribute("userType", STRING));
        }};
    }

    @Override
    public Set<Attribute> toAttributes() {
        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("accountEnabled", accountEnabled));
        attrs.add(AttributeBuilder.build("city", city));
        attrs.add(AttributeBuilder.build("companyName", companyName));
        attrs.add(AttributeBuilder.build("country", country));
        attrs.add(AttributeBuilder.build("creationType", creationType));
        attrs.add(AttributeBuilder.build("department", department));
        attrs.add(AttributeBuilder.build("dirSyncEnabled", dirSyncEnabled));
        attrs.add(AttributeBuilder.build("displayName", displayName));
        attrs.add(AttributeBuilder.build("facsimileTelephoneNumber", facsimileTelephoneNumber));
        attrs.add(AttributeBuilder.build("givenName", givenName));
        attrs.add(AttributeBuilder.build("id", id));
        attrs.add(AttributeBuilder.build("isCompromised", isCompromised));
        attrs.add(AttributeBuilder.build("jobTitle", jobTitle));
        attrs.add(AttributeBuilder.build("lastDirSyncTime", lastDirSyncTime));
        attrs.add(AttributeBuilder.build("mail", mail));
        attrs.add(AttributeBuilder.build("mailNickname", mailNickname));
        attrs.add(AttributeBuilder.build("mobilePhone", mobilePhone));
        attrs.add(AttributeBuilder.build("onPremisesSecurityIdentifier", onPremisesSecurityIdentifier));
        attrs.add(AttributeBuilder.build("passwordPolicies", passwordPolicies));
        attrs.addAll(passwordProfile.toAttributes());
        attrs.add(AttributeBuilder.build("physicalDeliveryOfficeName", physicalDeliveryOfficeName));
        attrs.add(AttributeBuilder.build("postalCode", postalCode));
        attrs.add(AttributeBuilder.build("preferredLanguage", preferredLanguage));
        attrs.add(AttributeBuilder.build("refreshTokensValidFromDateTime", refreshTokensValidFromDateTime));
        attrs.add(AttributeBuilder.build("sipProxyAddress", sipProxyAddress));
        attrs.add(AttributeBuilder.build("state", state));
        attrs.add(AttributeBuilder.build("streetAddress", streetAddress));
        attrs.add(AttributeBuilder.build("surname", surname));
        attrs.add(AttributeBuilder.build("telephoneNumber", telephoneNumber));
        attrs.add(AttributeBuilder.build("thumbnailPhoto", thumbnailPhoto));
        attrs.add(AttributeBuilder.build("usageLocation", usageLocation));
        attrs.add(AttributeBuilder.build("userPrincipalName", userPrincipalName));
        attrs.add(AttributeBuilder.build("userType", userType));
        return attrs;
    }
}
