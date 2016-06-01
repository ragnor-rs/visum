package io.reist.visum;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import org.junit.Assert;
import org.robolectric.RuntimeEnvironment;

import rx.functions.Func0;

/**
 * A test for components (IoC-containers) of the given type. Unlike {@link VisumTest}, this test
 * supports assertions for multiple clients operating simultaneously in a single component cache.
 *
 * On start-up, a new component is created via {@link #createComponent()}. Subclasses should
 * register component clients by calling {@link #register(Class[])} in the setUp method. A local
 * {@link io.reist.visum.ComponentCache.Listener} is registered and available via
 * {@link #onComponentCreated(Object)} and {@link #onComponentDestroyed(Object)}.
 *
 * Basic assertions include {@link #assertClientAttached(VisumClient)} and
 * {@link #assertClientDetached(VisumClient)} to check client lifecycle.
 *
 * Subclasses must call {@link #tearDown()} in tearDown methods to free resources.
 *
 * @see #getComponent()
 * @see #getComponentCache()
 *
 * @param <C>   root component class
 */
public abstract class VisumImplTest<C> implements ComponentCache.Listener {

    private C component;

    private final Func0<Object> componentFactory = new Func0<Object>() {

        @Override
        public C call() {
            return component;
        }

    };

    protected abstract C createComponent();

    @NonNull
    protected ComponentCache getComponentCache() {
        return ((TestApplication) RuntimeEnvironment.application).getComponentCache();
    }

    @SafeVarargs
    protected final void register(Class<? extends VisumClient>... clientClasses) {
        getComponentCache().register(componentFactory, clientClasses);
    }

    protected void assertClientDetached(VisumClient client) {
        ComponentCache.ComponentEntry componentEntry = getComponentCache().findComponentEntryByClient(client);
        Assert.assertFalse("There are still some clients attached", componentEntry.clients.contains(client));
        Assert.assertNull("ComponentCache should have removed the unused component", componentEntry.component);
    }

    protected void assertClientAttached(VisumClient client) {
        ComponentCache.ComponentEntry componentEntry = getComponentCache().findComponentEntryByClient(client);
        Assert.assertTrue("The client is not attached", componentEntry.clients.contains(client));
        Assert.assertNotNull("No component has been created for the client", componentEntry.component);
    }

    public C getComponent() {
        return component;
    }

    @CallSuper
    public void setUp() {
        getComponentCache().setListener(this);
        this.component = createComponent();
    }

    @CallSuper
    public void tearDown() {
        getComponentCache().setListener(null);
    }

    @Override
    public void onComponentDestroyed(Object component) {}

    @Override
    public void onComponentCreated(Object component) {}

}
