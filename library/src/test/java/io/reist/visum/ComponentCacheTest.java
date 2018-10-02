/*
 * Copyright (C) 2017 Renat Sarymsakov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.reist.visum;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import rx.functions.Func0;

import static io.reist.visum.ClientAssert.assertClientStarted;
import static io.reist.visum.ClientAssert.assertClientStoppedAndComponentRemoved;
import static io.reist.visum.ClientAssert.assertClientStoppedAndComponentRetained;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

/**
 * Created by Reist on 26.05.16.
 */
public class ComponentCacheTest extends VisumTest<BaseTestClient> {

    private TestClientThree clientThree;
    private TestClientTwo clientTwo;

    @Before
    public void start() {
        setUp(
                new Func0<Object>() {

                    @Override
                    public Object call() {
                        return new TestComponentOne();
                    }

                },
                TestClientOne.class, TestClientThree.class
        );
        dummyListener = Mockito.mock(ComponentCache.Listener.class);
    }

    protected BaseTestClient createClient() {
        clientTwo = new TestClientTwo(getComponentCache());
        clientThree = new TestClientThree(getComponentCache());
        return new TestClientOne(getComponentCache());
    }

    @Test
    public void testMultiRegistrations() {

        ComponentCache componentCache = getComponentCache();

        componentCache.register(new Func0<Object>() {

            @Override
            public Object call() {
                return new TestComponentTwo();
            }

        }, TestClientFour.class);

        TestClientFour client = new TestClientFour(getComponentCache());

        ComponentCache.ComponentEntry componentEntry = componentCache.findComponentEntryByClient(client);
        assertNotEquals("A new entry should have been created for a new registration", getComponentEntry(), componentEntry);

        Object component = componentCache.start(client);
        assertEquals("Invalid type of the created component", TestComponentTwo.class, component.getClass());

    }

    @Test
    public void testFindComponentOk() {
        assertEquals(
                "Can't find a ComponentEntry for the client of a registered type",
                getComponentEntry(),
                getComponentCache().findComponentEntryByClient(getClient())
        );
    }

    @Test
    public void testFindComponentFail() {
        assertNull(getComponentCache().findComponentEntryByClient(clientTwo));
    }

    @Test
    public void testComponentListener() {

        BaseTestClient client = getClient();
        ComponentCache componentCache = getComponentCache();

        Object component = componentCache.start(client);
        Mockito.verify(dummyListener, Mockito.times(1)).onComponentCreated(component);
        Mockito.verify(dummyListener, Mockito.times(0)).onComponentDestroyed(component);

        componentCache.stop(client, false);
        Mockito.verify(dummyListener, Mockito.times(1)).onComponentCreated(component);
        Mockito.verify(dummyListener, Mockito.times(1)).onComponentDestroyed(component);

    }

    @Test
    public void testStartAndStop() {

        BaseTestClient client = getClient();
        ComponentCache componentCache = getComponentCache();

        Object component = componentCache.start(client);
        assertClientStarted(componentCache, client);

        assertEquals("Invalid type of the created component", TestComponentOne.class, component.getClass());
        assertEquals("Internal reference to the component doesn't match the returned one", component, getComponentEntry().component);

        componentCache.stop(client, false);
        assertClientStoppedAndComponentRemoved(componentCache, client);

    }

    @Test
    public void testRetainComponent() {

        BaseTestClient client = getClient();
        ComponentCache componentCache = getComponentCache();

        componentCache.start(client);
        assertClientStarted(componentCache, client);

        componentCache.stop(client, true);
        assertClientStoppedAndComponentRetained(componentCache, client);

        componentCache.start(client);
        assertClientStarted(componentCache, client);

        componentCache.stop(client, false);
        assertClientStoppedAndComponentRemoved(componentCache, client);

    }

    @Test
    public void testMultiClients() {

        BaseTestClient client = getClient();
        ComponentCache componentCache = getComponentCache();

        Object component1 = componentCache.start(client);
        assertNotNull("No component is returned for a client of a registered type", component1);

        Object component3 = componentCache.start(clientThree);
        assertEquals(
                "ComponentCache should have reused the existing component due to client registration data given during registration",
                component1,
                component3
        );

        componentCache.stop(client, false);
        assertNotNull("The component is still in use", getComponentEntry().component);

        componentCache.stop(clientThree, false);
        assertNull("ComponentCache should have removed the unused component", getComponentEntry().component);

    }

    @After
    public void finish() {

        tearDown();

        clientTwo = null;
        clientThree = null;

        dummyListener = null;

    }

    private static class TestClientOne extends BaseTestClient {
        private TestClientOne(ComponentCache componentCache) {
            super(componentCache);
        }
    }

    private static class TestClientTwo extends BaseTestClient {
        private TestClientTwo(ComponentCache componentCache) {
            super(componentCache);
        }
    }

    private static class TestClientThree extends BaseTestClient {
        private TestClientThree(ComponentCache componentCache) {
            super(componentCache);
        }
    }

    private static class TestClientFour extends BaseTestClient {
        private TestClientFour(ComponentCache componentCache) {
            super(componentCache);
        }
    }

    private static class TestComponentOne {}

    private static class TestComponentTwo {}

    private ComponentCache.Listener dummyListener;

    @Override
    public void onComponentDestroyed(Object component) {
        dummyListener.onComponentDestroyed(component);
    }

    @Override
    public void onComponentCreated(Object component) {
        dummyListener.onComponentCreated(component);
    }

}
