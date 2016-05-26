package io.reist.visum;

import android.support.annotation.NonNull;

import org.junit.Assert;
import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

import rx.functions.Func0;

/**
 * Created by Reist on 26.05.16.
 */
public abstract class VisumImplTest<C> {

    private C component;

    private final Class<? extends C> componentClass;

    private final Func0<Object> componentFactory = new Func0<Object>() {

        @Override
        public C call() {
            return component = mock();
        }

    };

    protected C mock() {
        return Mockito.mock(componentClass);
    }

    protected VisumImplTest(Class<? extends C> componentClass) {
        this.componentClass = componentClass;
    }

    @NonNull
    protected ComponentCache getComponentCache() {
        return ((TestApplication) RuntimeEnvironment.application).getComponentCache();
    }

    protected void register(@NonNull List<? extends Class<? extends VisumClient>> clientClasses) {
        getComponentCache().register(clientClasses, componentFactory);
    }

    protected void checkClientDetached(VisumClient client) {
        ComponentCache.ComponentEntry componentEntry = getComponentCache().findComponentEntryByClient(client);
        Assert.assertFalse("Requested client stop but there are still some clients attached", componentEntry.clients.contains(client));
        Assert.assertNull("ComponentCache should have removed the unused component", componentEntry.component);
    }

    protected void checkClientAttached(VisumClient client) {
        ComponentCache.ComponentEntry componentEntry = getComponentCache().findComponentEntryByClient(client);
        Assert.assertTrue( "Only one client should be registered here", componentEntry.clients.contains(client));
        Assert.assertNotNull("No component has been created for the client", componentEntry.component);
    }

    public C getComponent() {
        return component;
    }

}
