package io.reist.visum;

import org.junit.Assert;

import rx.functions.Func0;

/**
 * A basic test for Visum clients. Unlike {@link VisumImplTest}, this test doesn't support
 * assertions for multiple clients operating simultaneously in a single component cache.
 *
 * Set up your test with {@link #setUp(Func0, Class[])}. This method should be called in a setUp
 * method to enable component instantiation for the registered sub-classes of {@link VisumClient}.
 * The component and the first client (determined by {@link #createClient()}) will be created. A
 * corresponding {@link io.reist.visum.ComponentCache.ComponentEntry} will be available for test
 * cases as well as the instance of {@link ComponentCache}. Also, a local component cache listener
 * is registered to intercept component fabrication events {@link #onComponentCreated(Object)}) and
 * {@link #onComponentDestroyed(Object)}.
 *
 * Use {@link #assertClientStarted()}, {@link #assertClientStoppedAndComponentRemoved()} and
 * {@link #assertClientStoppedAndComponentRetained(Object)} after
 * {@link ComponentCache#start(VisumClient)} and {@link ComponentCache#stop(VisumClient, boolean)}
 * to see if the client's lifecycle is correctly maintained by the {@link ComponentCache} class.
 *
 * Subclasses must call {@link #tearDown()} in tearDown methods to free resources.
 *
 * @see #getClient()
 * @see #getComponentCache()
 * @see #getComponentEntry()
 *
 * @param <C> client type
 */
public abstract class VisumTest<C extends VisumClient> implements ComponentCache.Listener {

    public ComponentCache getComponentCache() {
        return componentCache;
    }

    public ComponentCache.ComponentEntry getComponentEntry() {
        return componentEntry;
    }

    public C getClient() {
        return client;
    }

    private ComponentCache componentCache;

    private ComponentCache.ComponentEntry componentEntry;

    private C client;

    @SafeVarargs
    protected final void setUp(Func0<Object> componentFactory, Class<? extends C>... clientClasses) {

        componentCache = new ComponentCache();
        componentCache.register(componentFactory, clientClasses);
        componentCache.setListener(this);
        client = createClient();
        componentEntry = componentCache.findComponentEntryByClient(client);

        assertClientClasses(clientClasses);

        Assert.assertNotNull("ComponentEntry has not been created via ComponentCache.register()", componentEntry);
        Assert.assertEquals("ComponentEntry factory doesn't match the supplied Func0", componentFactory, componentEntry.componentFactory);

        Assert.assertTrue("No clients should be attached till ComponentCache.start() is called", componentEntry.clients.isEmpty());
        Assert.assertNull("No components should be created until the very first call of ComponentCache.start()", componentEntry.component);

    }

    protected void tearDown() {
        componentCache.setListener(null);
        componentCache = null;
        client = null;
        componentEntry = null;
    }

    private void assertClientClasses(Class<? extends C>[] clientClasses) {

        if (clientClasses.length != componentEntry.clientClasses.size()) {
            Assert.fail("ComponentEntry client classes don't match the supplied ones via ComponentCache.register()");
        }

        for (Class<? extends C> clientClass : clientClasses) {
            if (!componentEntry.clientClasses.contains(clientClass)) {
                Assert.fail("ComponentEntry client classes don't match the supplied ones via ComponentCache.register()");
            }
        }

    }

    protected abstract C createClient();

    private void assertClientStopped() {
        Assert.assertTrue("Requested client stop but there are still some clients attached", componentEntry.clients.isEmpty());
    }

    protected void assertClientStoppedAndComponentRemoved() {
        assertClientStopped();
        Assert.assertNull("ComponentCache should have removed the unused component", componentEntry.component);
    }

    protected void assertClientStoppedAndComponentRetained(Object component) {
        assertClientStopped();
        Assert.assertNotNull("The component is null", componentEntry.component);
        Assert.assertEquals("The component should have been retained", componentEntry.component, component);
    }

    protected void assertClientStarted() {
        Assert.assertTrue(
                "Only one client should be registered here",
                componentEntry.clients.size() == 1 && componentEntry.clients.contains(client)
        );
        Assert.assertNotNull("No component has been created for the client", componentEntry.component);
    }

    @Override
    public void onComponentDestroyed(Object component) {}

    @Override
    public void onComponentCreated(Object component) {}

}
