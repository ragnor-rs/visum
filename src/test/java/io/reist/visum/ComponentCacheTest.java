package io.reist.visum;

import android.content.Context;
import android.support.annotation.NonNull;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import rx.functions.Func0;

/**
 * Created by Reist on 26.05.16.
 */
public class ComponentCacheTest {

    private ComponentCache componentCache;

    private TestClientOne clientOne;
    private TestClientThree clientThree;
    private TestClientTwo clientTwo;

    private ComponentCache.ComponentEntry componentEntry;

    @Before
    public void start() {

        componentCache = new ComponentCache();

        Func0<Object> componentFactory = new Func0<Object>() {

            @Override
            public Object call() {
                return new TestComponentOne();
            }

        };

        componentCache.register(
                Arrays.asList(TestClientOne.class, TestClientThree.class),
                componentFactory
        );

        clientOne = new TestClientOne();
        clientTwo = new TestClientTwo();
        clientThree = new TestClientThree();

        componentEntry = componentCache.findComponentEntryByClient(clientOne);

        Assert.assertNotNull("ComponentEntry has not been created via ComponentCache.register()", componentEntry);
        Assert.assertTrue(
                "ComponentEntry client classes don't match the supplied ones via ComponentCache.register()",
                componentEntry.clientClasses.size() == 2 &&
                componentEntry.clientClasses.contains(TestClientOne.class) &&
                componentEntry.clientClasses.contains(TestClientThree.class)
        );
        Assert.assertEquals("ComponentEntry factory doesn't match the supplied Func0", componentFactory, componentEntry.componentFactory);

        Assert.assertTrue("No clients should be attached till ComponentCache.start() is called", componentEntry.clients.isEmpty());
        Assert.assertNull("No components should be created until the very first call of ComponentCache.start()", componentEntry.component);

    }

    @Test
    public void testFewRegistrations() {

        componentCache.register(TestClientFour.class, new Func0<Object>() {

            @Override
            public Object call() {
                return new TestComponentTwo();
            }

        });

        TestClientFour client = new TestClientFour();

        ComponentCache.ComponentEntry componentEntry = componentCache.findComponentEntryByClient(client);
        Assert.assertNotEquals("A new entry should have been created for a new registration", this.componentEntry, componentEntry);

        Object component = componentCache.start(client);
        Assert.assertEquals("Invalid type of the created component", TestComponentTwo.class, component.getClass());

    }

    @Test
    public void testFindComponentOk() {
        Assert.assertEquals(
                "Can't find a ComponentEntry for the client of a registered type",
                componentEntry,
                componentCache.findComponentEntryByClientOrThrow(clientOne)
        );
    }

    @Test(expected = IllegalStateException.class)
    public void testFindComponentFail() {
        componentCache.findComponentEntryByClientOrThrow(clientTwo);
    }

    @Test
    public void testStartAndStop() {

        Object component = componentCache.start(clientOne);
        Assert.assertTrue(
                "Only one client should be registered here",
                componentEntry.clients.size() == 1 && componentEntry.clients.contains(clientOne)
        );
        Assert.assertNotNull("No component has been created for the client", componentEntry.component);

        try {
            componentCache.start(clientOne);
            Assertions.shouldHaveThrown(IllegalStateException.class);
        } catch (IllegalStateException ignored) {}

        Assert.assertEquals("Invalid type of the created component", TestComponentOne.class, component.getClass());
        Assert.assertEquals("Internal reference to the component doesn't match the returned one", component, componentEntry.component);

        componentCache.stop(clientOne);
        Assert.assertTrue("Requested client stop but there are still some clients attached", componentEntry.clients.isEmpty());
        Assert.assertNull("ComponentCache should have removed the unused component", componentEntry.component);

        try {
            componentCache.stop(clientOne);
            Assertions.shouldHaveThrown(IllegalStateException.class);
        } catch (IllegalStateException ignored) {}

    }

    @Test
    public void testTwoClients() {

        Object component1 = componentCache.start(clientOne);
        Assert.assertNotNull("No component is returned for a client of a registered type", component1);

        Object component3 = componentCache.start(clientThree);
        Assert.assertEquals(
                "ComponentCache should have reused the existing component due to client registration data given during registration",
                component1,
                component3
        );

        componentCache.stop(clientOne);
        Assert.assertNotNull("The component is still in use", componentEntry.component);

        componentCache.stop(clientThree);
        Assert.assertNull("ComponentCache should have removed the unused component", componentEntry.component);

    }

    @After
    public void finish() {

        componentCache = null;

        clientOne = null;
        clientTwo = null;
        clientThree = null;

        componentEntry = null;

    }

    private abstract class TestClient implements VisumClient {

        @Override
        public ComponentCache getComponentCache() {
            return componentCache;
        }

        @Override
        public void onStartClient() {}

        @Override
        public void onStopClient() {}

        @Override
        public void inject(@NonNull Object from) {}

        @SuppressWarnings("ConstantConditions")
        @NonNull
        @Override
        public Context getContext() {
            return null;
        }

    }

    private class TestClientOne extends TestClient {}

    private class TestClientTwo extends TestClient {}

    private class TestClientThree extends TestClient {}

    private class TestClientFour extends TestClient {}

    private static class TestComponentOne {}

    private static class TestComponentTwo {}

}
