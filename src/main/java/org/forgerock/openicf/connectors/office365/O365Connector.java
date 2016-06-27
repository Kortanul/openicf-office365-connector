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

import org.forgerock.openicf.connectors.office365.client.Office365Client;
import org.forgerock.openicf.connectors.office365.client.objects.Group;
import org.forgerock.openicf.connectors.office365.client.objects.O365Object;
import org.forgerock.openicf.connectors.office365.client.objects.CreateUser;
import org.identityconnectors.common.CollectionUtil;
import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.exceptions.ConnectorException;
import org.identityconnectors.framework.common.exceptions.InvalidAttributeValueException;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.Filter;
import org.identityconnectors.framework.common.objects.filter.FilterTranslator;
import org.identityconnectors.framework.spi.Configuration;
import org.identityconnectors.framework.spi.ConnectorClass;
import org.identityconnectors.framework.spi.PoolableConnector;
import org.identityconnectors.framework.spi.SearchResultsHandler;
import org.identityconnectors.framework.spi.operations.*;

import java.util.*;

/**
 * Main implementation of the O365 Connector.
 *
 */
@ConnectorClass(
        displayNameKey = "O365.connector.display",
        configurationClass = O365Configuration.class)
public class O365Connector implements PoolableConnector, CreateOp, DeleteOp, ResolveUsernameOp,
        SchemaOp, SearchOp<Filter>, TestOp, UpdateOp {

    /**
     * Setup logging for the {@link O365Connector}.
     */
    private static final Log logger = Log.getLog(O365Connector.class);

    /**
     * Place holder for the {@link Configuration} passed into the init() method
     * {@link O365Connector#init(org.identityconnectors.framework.spi.Configuration)}.
     */
    private O365Configuration configuration;

    private Schema schema = null;
    Office365Client client = null;

    private static final String CONTENT_TYPE = "UTF-8";

    /**
     * Gets the Configuration context for this connector.
     *
     * @return The current {@link Configuration}
     */
    public Configuration getConfiguration() {
        return this.configuration;
    }

    /**
     * Callback method to receive the {@link Configuration}.
     *
     * @param configuration the new {@link Configuration}
     * @see org.identityconnectors.framework.spi.Connector#init(org.identityconnectors.framework.spi.Configuration)
     */
    public void init(final Configuration configuration) {
        this.configuration = (O365Configuration) configuration;
        try {
            client = new Office365Client(this.configuration);
        } catch (Exception e) {
            logger.error(e, "Failed to initialize connection to O365 resource");
        }
    }

    /**
     * Disposes of the {@link O365Connector}'s resources.
     *
     * @see org.identityconnectors.framework.spi.Connector#dispose()
     */
    public void dispose() {
        configuration = null;
    }

    /**
    *  {@inheritDoc}
    */
    public void checkAlive() {
        try {
            client.testConnection();
        } catch (Exception e) {
            logger.warn(e, "checkAlive failed");
            throw new ConnectorException("checkAlive() failed", e);
        }
    }


    /******************
     * SPI Operations
     *
     * Implement the following operations using the contract and
     * description found in the Javadoc for these methods.
     ******************/

    /**
     * {@inheritDoc}
     */
    public Uid resolveUsername(final ObjectClass objectClass, final String userName,
            final OperationOptions options) {
        if (ObjectClass.ACCOUNT.equals(objectClass)) {
            O365Object object = client.getObject(ObjectClass.ACCOUNT, userName);
            Attribute attr = AttributeUtil.find(O365Configuration.FIELD_ACCOUNT_UID, object.toAttributes());
            return attr == null ? null : new Uid(attr.getValue().get(0).toString());
        } else {
            logger.warn("ResolveUsername of type {0} is not supported", objectClass.getObjectClassValue());
            throw new UnsupportedOperationException("ResolveUsername of type "
                    + objectClass.getObjectClassValue() + " is not supported");
        }
    }

    /**
     * {@inheritDoc}
     */
    public Uid create(final ObjectClass objectClass, final Set<Attribute> createAttributes,
            final OperationOptions options) {
        if (ObjectClass.ACCOUNT.equals(objectClass) || ObjectClass.GROUP.equals(objectClass)) {
            Name name = AttributeUtil.getNameFromAttributes(createAttributes);
            if (name != null) {
                try {
                    O365Object object = objectClass.equals(ObjectClass.ACCOUNT)
                            ? new CreateUser(createAttributes)
                            : new Group(createAttributes);
                    O365Object created = client.createObject(objectClass, object);
                    String field = objectClass.equals(ObjectClass.ACCOUNT)
                            ? O365Configuration.FIELD_ACCOUNT_NAME
                            : O365Configuration.FIELD_GROUP_NAME;
                    Attribute attr = AttributeUtil.find(field, created.toAttributes());
                    return attr == null ? null : new Uid(attr.getValue().get(0).toString());
                } catch (Exception e) {
                    logger.error(e, "Failed to create {0} object: {1}", objectClass.toString(), name);
                    throw new ConnectorException("Failed to create " + objectClass.toString() + " object: " + name, e);
                }
            } else {
                throw new InvalidAttributeValueException("Name attribute is required");
            }
        } else {
            logger.warn("Create of type {0} is not supported", objectClass.getObjectClassValue());
            throw new UnsupportedOperationException("Create of type "
                    + objectClass.getObjectClassValue() + " is not supported");
        }
    }

    /**
     * {@inheritDoc}
     */
    public void delete(final ObjectClass objectClass, final Uid uid, final OperationOptions options) {
        if (ObjectClass.ACCOUNT.equals(objectClass) || ObjectClass.GROUP.equals(objectClass)) {
            try {
                client.deleteObject(objectClass, uid.getUidValue());
            } catch (Exception e) {
                logger.error(e, "Failed to delete {0} object: {1}", objectClass.toString(), uid.getUidValue());
                throw new ConnectorException("Failed to delete " + objectClass.toString() + " object: "
                        + uid.getUidValue(), e);
            }
        } else {
            logger.warn("Delete of type {0} is not supported", objectClass.getObjectClassValue());
            throw new UnsupportedOperationException("Delete of type "
                    + objectClass.getObjectClassValue() + " is not supported");
        }
    }

    /**
     * {@inheritDoc}
     */
    public FilterTranslator<Filter> createFilterTranslator(ObjectClass objectClass, OperationOptions options) {
        return new FilterTranslator<Filter>() {
            public List<Filter> translate(Filter filter) {
                return CollectionUtil.newList(filter);
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public void executeQuery(ObjectClass objectClass, Filter query, ResultsHandler handler,
            OperationOptions options) {
        int limit = options == null || options.getPageSize() == null ? -1 : options.getPageSize();
        // O365 does not support offset ($skip) despite the documentation
        //int offset = options == null || options.getPagedResultsOffset() == null ? -1 : options.getPagedResultsOffset();
        String cookie = options == null ? null : options.getPagedResultsCookie();

        FilterTranslator<String> filter = new O365FilterVisitor(configuration, objectClass);
        String o365query = query == null ? null : filter.translate(query).get(0);
        Office365Client pagedClient = client.withPaging(limit, cookie);
        for (O365Object object : pagedClient.getObjects(objectClass, o365query)) {
            Set<Attribute> attributes = object.toAttributes();
            if (objectClass.equals(ObjectClass.ACCOUNT)) {
                Attribute uidAttr = AttributeUtil.find(O365Configuration.FIELD_ACCOUNT_UID, attributes);
                Attribute nameAttr = AttributeUtil.find(O365Configuration.FIELD_ACCOUNT_NAME, attributes);
                if (uidAttr != null) {
                    attributes.add(uidAttr);
                }
                if (nameAttr != null) {
                    attributes.add(nameAttr);
                }
            } else if (objectClass.equals(ObjectClass.GROUP)) {
                Attribute uidAttr = AttributeUtil.find(O365Configuration.FIELD_GROUP_ID, attributes);
                if (uidAttr != null) {
                    attributes.add(uidAttr);
                    attributes.add(AttributeBuilder.build(Name.NAME, uidAttr.getValue().get(0)));
                }
            }

            ConnectorObject obj = new ConnectorObject(objectClass, attributes);
            if (!handler.handle(obj)) {
                // Stop iterating because the handler stopped processing
                break;
            }
        }

        // Pass back the paging cookie, if any
        if (pagedClient.getSkipToken() != null && handler instanceof SearchResultsHandler) {
            ((SearchResultsHandler) handler).handleResult(new SearchResult(pagedClient.getSkipToken(), 0));
        }
    }

    /**
     * {@inheritDoc}
     */
    public void sync(ObjectClass objectClass, SyncToken token, SyncResultsHandler handler,
            final OperationOptions options) {
        logger.warn("Sync is not supported");
        throw new UnsupportedOperationException("Sync is not supported");
    }

    /**
     * {@inheritDoc}
     */
    public SyncToken getLatestSyncToken(ObjectClass objectClass) {
        logger.warn("Sync is not supported");
        throw new UnsupportedOperationException("Sync is not supported");
    }

    /**
     * {@inheritDoc}
     */
    public void test() {
        try {
            client.testConnection();
        } catch (Exception e) {
            logger.error(e, "Connector test failed");
            throw new ConnectorException("Connector test failed!", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Schema schema() {
        if (null == schema) {
            final SchemaBuilder builder = new SchemaBuilder(O365Connector.class);

            ObjectClassInfoBuilder infoBuilder = new ObjectClassInfoBuilder();
            infoBuilder.setType(ObjectClass.ACCOUNT.getObjectClassValue());
            infoBuilder.addAttributeInfo(Name.INFO);
            // TODO: For want of Java8 the addSchema() method is non-static
            new CreateUser(new HashSet<Attribute>()).addSchema(infoBuilder);
            builder.defineObjectClass(infoBuilder.build());

            infoBuilder = new ObjectClassInfoBuilder();
            infoBuilder.setType(ObjectClass.GROUP.getObjectClassValue());
            infoBuilder.addAttributeInfo(Name.INFO);
            // TODO: For want of Java8 the addSchema() method is non-static
            new Group(new HashSet<Attribute>()).addSchema(infoBuilder);
            builder.defineObjectClass(infoBuilder.build());

            // Operation Options
            builder.defineOperationOption(OperationOptionInfoBuilder.buildPageSize(), SearchOp.class);
            builder.defineOperationOption(OperationOptionInfoBuilder.buildPagedResultsCookie(), SearchOp.class);

            schema = builder.build();
        }
        return schema;
    }

    /**
     * {@inheritDoc}
     */
    public Uid update(ObjectClass objectClass, Uid uid, Set<Attribute> replaceAttributes,
            OperationOptions options) {
        if (ObjectClass.ACCOUNT.equals(objectClass) || ObjectClass.GROUP.equals(objectClass)) {
            Name name = AttributeUtil.getNameFromAttributes(replaceAttributes);
            if (name != null) {
                try {
                    O365Object object = objectClass.equals(ObjectClass.ACCOUNT)
                            ? new CreateUser(replaceAttributes)
                            : new Group(replaceAttributes);
                    client.updateObject(objectClass, uid.getUidValue(), object);
                    return uid;
                } catch (Exception e) {
                    logger.error(e, "Failed to update {0} object: {1}", objectClass.toString(), name);
                    throw new ConnectorException("Failed to update " + objectClass.toString() + " object: " + name, e);
                }
            } else {
                throw new InvalidAttributeValueException("Name attribute is required");
            }
        } else {
            logger.warn("Update of type {0} is not supported", objectClass.getObjectClassValue());
            throw new UnsupportedOperationException("Update of type "
                    + objectClass.getObjectClassValue() + " is not supported");
        }
    }
}
