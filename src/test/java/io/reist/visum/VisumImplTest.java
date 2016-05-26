package io.reist.visum;

import android.support.annotation.NonNull;

import org.mockito.Mockito;
import org.robolectric.RuntimeEnvironment;

import java.util.List;

import rx.functions.Func0;

/**
 * Created by Reist on 26.05.16.
 */
public abstract class VisumImplTest<C> {

    protected C testComponent;

    private final Class<? extends C> componentClass;

    private final Func0<Object> componentFactory = new Func0<Object>() {

        @Override
        public C call() {
            return testComponent = Mockito.mock(componentClass);
        }

    };

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

    protected void register(@NonNull Class<? extends VisumClient> clientClass) {
        getComponentCache().register(clientClass, componentFactory);
    }

}
