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

import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.AttributeInfoBuilder;
import org.identityconnectors.framework.common.objects.ObjectClassInfoBuilder;

import java.util.List;
import java.util.Set;

/**
 * Office365 object base class
 */
public abstract class O365Object {

    O365Object() {}

    O365Object(Set<Attribute> attrs) {}

    /**
     * Return this object's properties as a Set of Attributes.
     *
     * @return Set of Attributes representing this object's properties
     */
    public abstract Set<Attribute> toAttributes();

    /**
     * Return this object's properties as a List of SchemaAttributes.
     *
     * @return List of SchemaAttributes representing this object's properties
     */
    public abstract List<SchemaAttribute> getSchemaAttributes();

    /**
     * Add to a schema helper that describes this object's properties.
     *
     * @param infoBuilder the ObjectClassInfoBuilder to populate.
     * @return ObjectClassInfoBuilder schema helper.
     */
    public ObjectClassInfoBuilder addSchema(ObjectClassInfoBuilder infoBuilder) {
        for (SchemaAttribute attr : getSchemaAttributes()) {
            infoBuilder.addAttributeInfo(AttributeInfoBuilder.define(attr.property)
                    .setCreateable(attr.createable)
                    .setReadable(attr.readable)
                    .setRequired(attr.required)
                    .setMultiValued(attr.multivalued)
                    .setReturnedByDefault(attr.returned)
                    .setType(attr.object.getClass())
                    .build()
            );
        }
        return infoBuilder;
    }
}
