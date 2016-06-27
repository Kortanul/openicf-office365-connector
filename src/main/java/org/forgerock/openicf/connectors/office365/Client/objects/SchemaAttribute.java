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

public class SchemaAttribute {
    public String property;
    public Object object;
    public boolean createable = true;
    public boolean readable = false;
    public boolean required = false;
    public boolean multivalued = false;
    public boolean returned = false;

    public SchemaAttribute(String property, Object object) {
        this.property = property;
        this.object = object;
    }

    public SchemaAttribute setCreatable(boolean state) {
        this.createable = state;
        return this;
    }

    public SchemaAttribute setReadable(boolean state) {
        this.readable = state;
        return this;
    }

    public SchemaAttribute setRequired(boolean state) {
        this.required = state;
        return this;
    }

    public SchemaAttribute setMultivalued(boolean state) {
        this.multivalued = state;
        return this;
    }

    public SchemaAttribute setReturned(boolean state) {
        this.returned = state;
        return this;
    }
}
