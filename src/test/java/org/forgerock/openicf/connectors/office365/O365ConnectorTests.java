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

import org.identityconnectors.common.logging.Log;
import org.identityconnectors.common.security.GuardedString;
import org.identityconnectors.framework.common.objects.*;
import org.identityconnectors.framework.common.objects.filter.FilterBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Attempts to test the {@link O365Connector} with the framework.
 *
 */
public class O365ConnectorTests {

    private static final Log logger = Log.getLog(O365ConnectorTests.class);

    private O365Configuration config = null;
    private O365Connector conn = null;

    @BeforeTest
    private void initConfig() {
        config = new O365Configuration();

        // See: https://graph.microsoft.io/en-us/docs/platform/rest
        // All of these properties must be updated with a working Azure instance in order to run these tests.
        config.setTenant("tenantname");
        config.setClientId("clientid");
        config.setClientSecret(new GuardedString("clientsecret".toCharArray()));
        config.setAccessToken("accesstoken");
        config.setTokenExpiration(0L);
        config.setRefreshToken("refreshtoken");

        conn = new O365Connector();
        conn.init(config);
    }

//    @Test
//    public void test() {
//        logger.info("Running Test Test");
//        conn.test();
//    }
//
//    @Test
//    public void schema() {
//        logger.info("Running Schema Test");
//        Schema schema = conn.schema();
//        Assert.assertNotNull(schema.findObjectClassInfo(ObjectClass.ACCOUNT_NAME));
//    }
//
//    @Test
//    public void authenticate() {
//        // Authenticate is not supported
//    }
//
//    @Test
//    public void resolveUsername() {
//        logger.info("Running ResolveUsername Test");
//        final OperationOptionsBuilder builder = new OperationOptionsBuilder();
//        String id = "bjensen@jonbranchforgerock.onmicrosoft.com";
//        Uid uid = conn.resolveUsername(ObjectClass.ACCOUNT, id, builder.build());
//
//
//        final List<ConnectorObject> objects = new ArrayList<ConnectorObject>();
//        conn.executeQuery(ObjectClass.ACCOUNT, FilterBuilder.equalTo(
//                AttributeBuilder.build(config.getAccountNameField(), id)),
//                new ResultsHandler() {
//                    @Override
//                    public boolean handle(ConnectorObject connectorObject) {
//                        objects.add(connectorObject);
//                        return true;
//                    }
//                }, null);
//        Assert.assertEquals(objects.size(), 1);
//        Assert.assertEquals(objects.get(0).getUid().getUidValue(), uid.getUidValue());
//    }
//
//    @Test
//    public void getUser() {
//        logger.info("Running GetUser Test");
//        conn.executeQuery(ObjectClass.ACCOUNT, FilterBuilder.equalTo(
//                AttributeBuilder.build(config.getAccountNameField(), "bjensen@jonbranchforgerock.onmicrosoft.com")),
//                new ResultsHandler() {
//                    @Override
//                    public boolean handle(ConnectorObject connectorObject) {
//                        return true;
//                    }
//                }, null);
//    }
//
//    @Test(expectedExceptions = UnsupportedOperationException.class)
//    public void resolveUsernameGroupUnsupported() {
//        logger.info("Running ResolveUsernameGroupUnsupported Test");
//        final OperationOptionsBuilder builder = new OperationOptionsBuilder();
//        conn.resolveUsername(ObjectClass.GROUP, "any", builder.build());
//    }
//
//    @Test
//    public void createUser() {
//        logger.info("Running CreateUser Test");
//        final OperationOptionsBuilder builder = new OperationOptionsBuilder();
//        Set<Attribute> createAttributes = new HashSet<Attribute>();
//        String id = "foo@jonbranchforgerock.onmicrosoft.com";
//        createAttributes.add(new Name(id));
//        createAttributes.add(AttributeBuilder.build(OperationalAttributes.PASSWORD_NAME,
//                new GuardedString("Passw0rd".toCharArray())));
//        createAttributes.add(AttributeBuilder.build("givenName", "Foo"));
//        createAttributes.add(AttributeBuilder.build("surname", "Bar"));
//        createAttributes.add(AttributeBuilder.build("displayName", "foo"));
//        createAttributes.add(AttributeBuilder.build("mailNickname", "foo"));
//        createAttributes.add(AttributeBuilder.build("accountEnabled", true));
//        Uid uid = conn.create(ObjectClass.ACCOUNT, createAttributes, builder.build());
//        Assert.assertEquals(uid.getUidValue(), id);
//
//        conn.delete(ObjectClass.ACCOUNT, new Uid(id), builder.build());
//    }
//
//    @Test
//    public void updateUser() {
//        logger.info("Running UpdateUser Test");
//
//        final OperationOptionsBuilder builder = new OperationOptionsBuilder();
//        Set<Attribute> createAttributes = new HashSet<Attribute>();
//        String id = "foo@jonbranchforgerock.onmicrosoft.com";
//        createAttributes.add(new Name(id));
//        createAttributes.add(AttributeBuilder.build(OperationalAttributes.PASSWORD_NAME,
//                new GuardedString("Passw0rd".toCharArray())));
//        createAttributes.add(AttributeBuilder.build("givenName", "Foo"));
//        createAttributes.add(AttributeBuilder.build("surname", "Bar"));
//        createAttributes.add(AttributeBuilder.build("displayName", "foo"));
//        createAttributes.add(AttributeBuilder.build("mailNickname", "foo"));
//        createAttributes.add(AttributeBuilder.build("accountEnabled", true));
//        conn.create(ObjectClass.ACCOUNT, createAttributes, builder.build());
//
//        Set<Attribute> patchAttributes = new HashSet<Attribute>();
//        patchAttributes.add(new Name(id));
//        patchAttributes.add(AttributeBuilder.build("givenName", "Bar"));
//        patchAttributes.add(AttributeBuilder.build("surname", "Foo"));
//        conn.update(ObjectClass.ACCOUNT, new Uid(id), patchAttributes, builder.build());
//
//        final List<ConnectorObject> objects = new ArrayList<ConnectorObject>();
//        conn.executeQuery(ObjectClass.ACCOUNT, FilterBuilder.equalTo(
//                AttributeBuilder.build(config.getAccountNameField(), id)),
//                new ResultsHandler() {
//                    @Override
//                    public boolean handle(ConnectorObject connectorObject) {
//                        objects.add(connectorObject);
//                        return true;
//                    }
//                }, null);
//        Assert.assertEquals(objects.size(), 1);
//        Assert.assertEquals(objects.get(0).getAttributeByName("givenName").getValue().get(0), "Bar");
//        Assert.assertEquals(objects.get(0).getAttributeByName("surname").getValue().get(0), "Foo");
//
//        conn.delete(ObjectClass.ACCOUNT, new Uid(id), builder.build());
//    }
//
//    @Test
//    public void deleteUser() {
//        logger.info("Running DeleteUser Test");
//        final OperationOptionsBuilder builder = new OperationOptionsBuilder();
//        Set<Attribute> createAttributes = new HashSet<Attribute>();
//        String id = "foo@jonbranchforgerock.onmicrosoft.com";
//        createAttributes.add(new Name(id));
//        createAttributes.add(AttributeBuilder.build(OperationalAttributes.PASSWORD_NAME,
//                new GuardedString("Passw0rd".toCharArray())));
//        createAttributes.add(AttributeBuilder.build("givenName", "Foo"));
//        createAttributes.add(AttributeBuilder.build("surname", "Bar"));
//        createAttributes.add(AttributeBuilder.build("displayName", "foo"));
//        createAttributes.add(AttributeBuilder.build("mailNickname", "foo"));
//        createAttributes.add(AttributeBuilder.build("accountEnabled", true));
//        conn.create(ObjectClass.ACCOUNT, createAttributes, builder.build());
//
//        conn.delete(ObjectClass.ACCOUNT, new Uid(id), builder.build());
//
//        final List<ConnectorObject> objs = new ArrayList<ConnectorObject>();
//        conn.executeQuery(ObjectClass.ACCOUNT, FilterBuilder.equalTo(
//                AttributeBuilder.build(config.getAccountNameField(), id)),
//                new ResultsHandler() {
//                    @Override
//                    public boolean handle(ConnectorObject connectorObject) {
//                        objs.add(connectorObject);
//                        return true;
//                    }
//                }, null);
//        Assert.assertEquals(objs.size(), 0);
//    }
//
//    @Test
//    public void getGroup() {
//        logger.info("Running GetGroup Test");
//        final List<ConnectorObject> objs = new ArrayList<ConnectorObject>();
//        conn.executeQuery(ObjectClass.GROUP, FilterBuilder.equalTo(
//                AttributeBuilder.build(config.getGroupNameField(), "First")),
//                new ResultsHandler() {
//                    @Override
//                    public boolean handle(ConnectorObject connectorObject) {
//                        objs.add(connectorObject);
//                        return true;
//                    }
//                }, null);
//        Assert.assertEquals(objs.size(), 1);
//    }
//
//    @Test
//    public void createAndDeleteGroup() {
//        logger.info("Running CreateGroup Test");
//        final OperationOptionsBuilder builder = new OperationOptionsBuilder();
//        Set<Attribute> createAttributes = new HashSet<Attribute>();
//        String name = "Second";
//        createAttributes.add(new Name(name));
//        createAttributes.add(AttributeBuilder.build("description", "This is the second group"));
//        createAttributes.add(AttributeBuilder.build("mailEnabled", false));
//        createAttributes.add(AttributeBuilder.build("mailNickname", name));
//        createAttributes.add(AttributeBuilder.build("securityEnabled", true));
//        Uid uid = conn.create(ObjectClass.GROUP, createAttributes, builder.build());
//        Assert.assertNotNull(uid);
//
//        conn.delete(ObjectClass.GROUP, uid, builder.build());
//    }
//
//    @Test(expectedExceptions = UnsupportedOperationException.class)
//    public void getLatestSyncToken() {
//        logger.info("Running GetLatestSyncToken Test");
//        SyncToken token = conn.getLatestSyncToken(ObjectClass.ACCOUNT);
//    }
//
//    @Test(expectedExceptions = UnsupportedOperationException.class)
//    public void sync() {
//        logger.info("Running Sync Test");
//        final OperationOptionsBuilder builder = new OperationOptionsBuilder();
//        builder.setPageSize(10);
//        final SyncResultsHandler handler = new SyncResultsHandler() {
//            public boolean handle(SyncDelta delta) {
//                return false;
//            }
//        };
//
//        conn.sync(ObjectClass.ACCOUNT, new SyncToken(10), handler, builder.build());
//    }
}
