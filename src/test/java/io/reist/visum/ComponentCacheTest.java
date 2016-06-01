package io.reist.visum;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import rx.functions.Func0;

/**
 * Created by Reist on 26.05.16.
 */
public class ComponentCacheTest extends VisumTest<TestClient> {

    private TestClientThree clientThree;
    private TestClientTwo clientTwo;

    @Before
    public void start() {
        start(
                new Func0<Object>() {

                    @Override
                    public Object call() {
                        return new TestComponentOne();
                    }

                },
                TestClientOne.class, TestClientThree.class
        );
    }

    protected TestClient createClient() {
        clientTwo = new TestClientTwo(getComponentCache());
        clientThree = new TestClientThree(getComponentCache());
        return new TestClientOne(getComponentCache());
    }

    @Test
    public void testFewRegistrations() {

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
    public void testStartAndStop() {

        TestClient client = getClient();
        ComponentCache componentCache = getComponentCache();

        Object component = componentCache.start(client);
        checkClientStarted();

        try {
            componentCache.start(client);
            Assertions.shouldHaveThrown(IllegalStateException.class);
        } catch (IllegalStateException ignored) {}

        Assert.assertEquals("Invalid type of the created component", TestComponentOne.class, component.getClass());
        Assert.assertEquals("Internal reference to the component doesn't match the returned one", component, getComponentEntry().component);

        componentCache.stop(client, false);
        checkClientStopped();

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
        checkClientStarted();

        componentCache.stop(client, true);
        checkComponentRetained(component);

        componentCache.start(client);
        checkClientStarted();

        componentCache.stop(client, false);
        checkClientStopped();

        try {
            componentCache.stop(client, false);
            Assertions.shouldHaveThrown(IllegalStateException.class);
        } catch (IllegalStateException ignored) {}

    }

    @Test
    public void testTwoClients() {

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

        stop();

        clientTwo = null;
        clientThree = null;

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

}
