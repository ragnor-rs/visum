package io.reist.visum;

import android.content.Context;
import android.support.annotation.NonNull;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import rx.functions.Func0;

/**
 * Created by Reist on 26.05.16.
 */
public class ComponentCacheTest {

    private ComponentCache componentCache;
    private Context context;

    private TestClientOne clientOne;
    private TestClientThree clientThree;
    private TestClientTwo clientTwo;

    private ComponentCache.ComponentEntry componentEntry;

    @Before
    public void start() {

        Func0<Object> componentFactory = new Func0<Object>() {

            @Override
            public Object call() {
                return new TestComponent();
            }

        };

        componentCache = new ComponentCache();
        componentCache.register(Arrays.asList(TestClientOne.class, TestClientThree.class), componentFactory);

        context = Mockito.mock(Context.class);

        clientOne = new TestClientOne(context);
        clientTwo = new TestClientTwo(context);
        clientThree = new TestClientThree(context);

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

        Assert.assertTrue("Invalid type of the created component", component instanceof TestComponent);
        Assert.assertEquals("Internal reference to the component doesn't match the returned one", component, componentEntry.component);

        componentCache.stop(clientOne);
        Assert.assertTrue("Requested client stop but there are some attached clients", componentEntry.clients.isEmpty());
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

        context = null;

        clientOne = null;
        clientTwo = null;
        clientThree = null;

        componentEntry = null;

    }

    private static class TestClientOne extends VisumBaseClient {

        public TestClientOne(@NonNull Context context) {
            super(context);
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestComponent) from).inject(this);
        }

    }

    private static class TestClientTwo extends VisumBaseClient {

        public TestClientTwo(@NonNull Context context) {
            super(context);
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestComponent) from).inject(this);
        }

    }

    private static class TestClientThree extends VisumBaseClient {

        public TestClientThree(@NonNull Context context) {
            super(context);
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestComponent) from).inject(this);
        }

    }

    private static class TestComponent {

        public void inject(TestClientOne client) {}

        public void inject(TestClientTwo client) {}

        public void inject(TestClientThree client) {}

    }

}
