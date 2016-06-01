package io.reist.visum;

import org.junit.Assert;

import rx.functions.Func0;

/**
 * Created by Reist on 26.05.16.
 */
public abstract class VisumTest<C extends VisumClient> {

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
    protected final void start(Func0<Object> componentFactory, Class<? extends C>... clientClasses) {

        componentCache = new ComponentCache();
        componentCache.register(componentFactory, clientClasses);
        client = createClient();
        componentEntry = componentCache.findComponentEntryByClient(client);

        Assert.assertNotNull("ComponentEntry has not been created via ComponentCache.register()", componentEntry);
        Assert.assertTrue("ComponentEntry client classes don't match the supplied ones via ComponentCache.register()", checkClientClasses(clientClasses));
        Assert.assertEquals("ComponentEntry factory doesn't match the supplied Func0", componentFactory, componentEntry.componentFactory);

        Assert.assertTrue("No clients should be attached till ComponentCache.start() is called", componentEntry.clients.isEmpty());
        Assert.assertNull("No components should be created until the very first call of ComponentCache.start()", componentEntry.component);

    }

    protected void stop() {
        componentCache = null;
        client = null;
        componentEntry = null;
    }

    private boolean checkClientClasses(Class<? extends C>[] clientClasses) {

        if (clientClasses.length != componentEntry.clientClasses.size()) {
            return false;
        }

        for (Class<? extends C> clientClass : clientClasses) {
            if (!componentEntry.clientClasses.contains(clientClass)) {
                return false;
            }
        }

        return true;

    }

    protected abstract C createClient();

    protected void checkClientStopped() {
        Assert.assertTrue("Requested client stop but there are still some clients attached", componentEntry.clients.isEmpty());
        Assert.assertNull("ComponentCache should have removed the unused component", componentEntry.component);
    }

    protected void checkComponentRetained(Object component) {
        Assert.assertTrue("Requested client stop but there are still some clients attached", componentEntry.clients.isEmpty());
        Assert.assertNotNull("The component is null", componentEntry.component);
        Assert.assertEquals("The component should have been retained", componentEntry.component, component);
    }

    protected void checkClientStarted() {
        Assert.assertTrue(
                "Only one client should be registered here",
                componentEntry.clients.size() == 1 && componentEntry.clients.contains(client)
        );
        Assert.assertNotNull("No component has been created for the client", componentEntry.component);
    }

}
