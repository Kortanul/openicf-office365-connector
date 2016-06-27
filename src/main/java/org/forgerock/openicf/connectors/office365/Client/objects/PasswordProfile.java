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
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.common.security.SecurityUtil;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeBuilder;
import org.identityconnectors.framework.common.objects.OperationalAttributes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * PasswordProfile object as found in Office365 AzureAD via Graph API
 *
 * Official schema:
 <ComplexType Name="PasswordProfile">
 <Property Name="password" Type="Edm.String"/>
 <Property Name="forceChangePasswordNextLogin" Type="Edm.Boolean"/>
 <Property Name="enforceChangePasswordPolicy" Type="Edm.Boolean"/>
 </ComplexType>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class PasswordProfile extends O365Object {

    @JsonProperty
    public String password;

    protected PasswordProfile() {}

    public PasswordProfile(Set<Attribute> attrs) {
        for (Attribute attr : attrs) {
            if (attr.getName().equals(OperationalAttributes.PASSWORD_NAME)) {
                password = SecurityUtil.decrypt((GuardedString) attr.getValue().get(0));
            }
        }
    }

    @Override
    public Set<Attribute> toAttributes() {
        Set<Attribute> attrs = new HashSet<Attribute>();
        attrs.add(AttributeBuilder.build("password", password == null
                ? null
                : new GuardedString(password.toCharArray())));
        return attrs;
    }

    @JsonIgnore
    @Override
    public List<SchemaAttribute> getSchemaAttributes() {
        final GuardedString GS = new GuardedString();

        return new ArrayList<SchemaAttribute>() {{
            add(new SchemaAttribute("password", GS));
        }};
    }
}
