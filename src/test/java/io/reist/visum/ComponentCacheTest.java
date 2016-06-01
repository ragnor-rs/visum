package io.reist.visum;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import rx.functions.Func0;

/**
 * Created by Reist on 26.05.16.
 */
public class ComponentCacheTest extends VisumTest<TestClient> {

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

    protected TestClient createClient() {
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
        Assert.assertNotEquals("A new entry should have been created for a new registration", getComponentEntry(), componentEntry);

        Object component = componentCache.start(client);
        Assert.assertEquals("Invalid type of the created component", TestComponentTwo.class, component.getClass());

    }

    @Test
    public void testFindComponentOk() {
        Assert.assertEquals(
                "Can't find a ComponentEntry for the client of a registered type",
                getComponentEntry(),
                getComponentCache().findComponentEntryByClientOrThrow(getClient())
        );
    }

    @Test(expected = IllegalStateException.class)
    public void testFindComponentFail() {
        getComponentCache().findComponentEntryByClientOrThrow(clientTwo);
    }

    @Test
    public void testComponentListener() {

        TestClient client = getClient();
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

        TestClient client = getClient();
        ComponentCache componentCache = getComponentCache();

        Object component = componentCache.start(client);
        assertClientStarted();

        try {
            componentCache.start(client);
            Assertions.shouldHaveThrown(IllegalStateException.class);
        } catch (IllegalStateException ignored) {}

        Assert.assertEquals("Invalid type of the created component", TestComponentOne.class, component.getClass());
        Assert.assertEquals("Internal reference to the component doesn't match the returned one", component, getComponentEntry().component);

        componentCache.stop(client, false);
        assertClientStoppedAndComponentRemoved();

        try {
            componentCache.stop(client, false);
            Assertions.shouldHaveThrown(IllegalStateException.class);
        } catch (IllegalStateException ignored) {}

    }

    @Test
    public void testRetainComponent() {

        TestClient client = getClient();
        ComponentCache componentCache = getComponentCache();

        Object component = componentCache.start(client);
        assertClientStarted();

        componentCache.stop(client, true);
        assertClientStoppedAndComponentRetained(component);

        componentCache.start(client);
        assertClientStarted();

        componentCache.stop(client, false);
        assertClientStoppedAndComponentRemoved();

    }

    @Test
    public void testMultiClients() {

        TestClient client = getClient();
        ComponentCache componentCache = getComponentCache();

        Object component1 = componentCache.start(client);
        Assert.assertNotNull("No component is returned for a client of a registered type", component1);

        Object component3 = componentCache.start(clientThree);
        Assert.assertEquals(
                "ComponentCache should have reused the existing component due to client registration data given during registration",
                component1,
                component3
        );

        componentCache.stop(client, false);
        Assert.assertNotNull("The component is still in use", getComponentEntry().component);

        componentCache.stop(clientThree, false);
        Assert.assertNull("ComponentCache should have removed the unused component", getComponentEntry().component);

    }

    @After
    public void finish() {

        tearDown();

        clientTwo = null;
        clientThree = null;

        dummyListener = null;

    }

    private static class TestClientOne extends TestClient {
        private TestClientOne(ComponentCache componentCache) {
            super(componentCache);
        }
    }

    private static class TestClientTwo extends TestClient {
        private TestClientTwo(ComponentCache componentCache) {
            super(componentCache);
        }
    }

    private static class TestClientThree extends TestClient {
        private TestClientThree(ComponentCache componentCache) {
            super(componentCache);
        }
    }

    private static class TestClientFour extends TestClient {
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
