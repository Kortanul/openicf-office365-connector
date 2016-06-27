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
package org.forgerock.openicf.connectors.office365;

import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.objects.Attribute;
import org.identityconnectors.framework.common.objects.Name;
import org.identityconnectors.framework.common.objects.ObjectClass;
import org.identityconnectors.framework.common.objects.Uid;
import org.identityconnectors.framework.common.objects.filter.AbstractFilterTranslator;
import org.identityconnectors.framework.common.objects.filter.ContainsAllValuesFilter;
import org.identityconnectors.framework.common.objects.filter.ContainsFilter;
import org.identityconnectors.framework.common.objects.filter.EndsWithFilter;
import org.identityconnectors.framework.common.objects.filter.EqualsFilter;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanFilter;
import org.identityconnectors.framework.common.objects.filter.GreaterThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanFilter;
import org.identityconnectors.framework.common.objects.filter.LessThanOrEqualFilter;
import org.identityconnectors.framework.common.objects.filter.StartsWithFilter;

/**
 * This is an implementation of AbstractFilterTranslator that gives a concrete representation
 * of which filters can be applied at the connector level (natively).
 */
public class O365FilterVisitor extends AbstractFilterTranslator<String> {

    private final O365Configuration config;
    private final ObjectClass objectClass;

    public O365FilterVisitor(O365Configuration config, ObjectClass objectClass) {
        this.config = config;
        this.objectClass = objectClass;
    }

    private String getAttributeName(Attribute attr) {
        String name = attr.getName();
        if (name.equals(Uid.NAME)) {
            if (objectClass.equals(ObjectClass.ACCOUNT)) {
                return O365Configuration.FIELD_ACCOUNT_UID;
            } else if (objectClass.equals(ObjectClass.GROUP)) {
                return O365Configuration.FIELD_GROUP_ID;
            }
            return null;
        }
        if (name.equals(Name.NAME)) {
            if (objectClass.equals(ObjectClass.ACCOUNT)) {
                return O365Configuration.FIELD_ACCOUNT_NAME;
            } else if (objectClass.equals(ObjectClass.GROUP)) {
                return O365Configuration.FIELD_GROUP_NAME;
            }
            return null;
        }
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createAndExpression(final String leftExpression, final String rightExpression) {
        return leftExpression + " and " + rightExpression + " ";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createContainsAllValuesExpression(final ContainsAllValuesFilter filter,
            boolean not) {
        throw new ConnectorException("Unsupported filter: containsAllValues");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createContainsExpression(final ContainsFilter filter, boolean not) {
        throw new ConnectorException("Unsupported filter: contains");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createEndsWithExpression(final EndsWithFilter filter, boolean not) {
        throw new ConnectorException("Unsupported filter: endsWith");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createEqualsExpression(final EqualsFilter filter, boolean not) {
        String fltr = " " + getAttributeName(filter.getAttribute()) + " eq '" + filter.getAttribute().getValue().get(0) + "'";
        return not ? "not (" + filter + ") " : fltr + " ";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createGreaterThanExpression(final GreaterThanFilter filter, boolean not) {
        // Simulate 'gt' behavior with 'ge and not(eq)'
        String fltr = createAndExpression(createGreaterThanOrEqualExpression(
                new GreaterThanOrEqualFilter(filter.getAttribute()), false),
                createEqualsExpression(new EqualsFilter(filter.getAttribute()), true));
        return not ? "not (" + filter + ") " : fltr + " ";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createGreaterThanOrEqualExpression(final GreaterThanOrEqualFilter filter,
            boolean not) {
        String fltr = " " + getAttributeName(filter.getAttribute()) + " ge '" + filter.getAttribute().getValue().get(0) + "'";
        return not ? "not (" + filter + ") " : fltr + " ";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createLessThanExpression(final LessThanFilter filter, boolean not) {
        // Simulate 'lt' behavior with 'le and not(eq)'
        String fltr = createAndExpression(createLessThanOrEqualExpression(
                new LessThanOrEqualFilter(filter.getAttribute()), false),
                createEqualsExpression(new EqualsFilter(filter.getAttribute()), true));
        return not ? "not (" + filter + ") " : fltr + " ";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createLessThanOrEqualExpression(final LessThanOrEqualFilter filter, boolean not) {
        String fltr = " " + getAttributeName(filter.getAttribute()) + " le '" + filter.getAttribute().getValue().get(0) + "'";
        return not ? "not (" + filter + ") " : fltr + " ";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createOrExpression(final String leftExpression, final String rightExpression) {
        return "(" + leftExpression + " or " + rightExpression + ") ";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String createStartsWithExpression(final StartsWithFilter filter, boolean not) {
        String fltr = "startsWith(" + getAttributeName(filter.getAttribute()) + ", '"
                + filter.getAttribute().getValue().get(0) + "') ";
        return not ? "not (" + filter + ") " : fltr + " ";
    }
}
