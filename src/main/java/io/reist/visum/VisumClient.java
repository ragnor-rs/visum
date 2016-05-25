package io.reist.visum;

import android.support.annotation.NonNull;

/**
 * Visum framework addresses these tasks:
 * - providing inversion of control
 * - construction of MVP architecture
 *
 * To achieve that, Visum provides two interfaces:
 * - {@link VisumClient} - implement to take advantage of dependency injection
 * - {@link io.reist.visum.view.VisumView} - an extension to VisumClient which represents MVP views
 *
 * Visum recommends the following structure of application:
 * Views ({@link android.app.Activity}, {@link android.app.Fragment} or {@link android.view.View})
 * have Presenters injected into them.
 *
 * A configuration change view causes view a loss of data which hasn't been explicitly saved.
 * But the attached {@link io.reist.visum.presenter.VisumPresenter} survives configuration change
 * thus pertaining all the data stored inside of it.
 *
 * The presenter re-attachment is carried out by the {@link ComponentCache}
 * (e.g. <a href="https://github.com/ragnor-rs/mvp-sandbox/blob/develop/app/src/main/java/io/reist/sandbox/app/SandboxComponentCache.java">SandboxComponentCache</a>)
 * provided by local {@link android.app.Application} class. The client code also must provide Visum
 * with a singleton component and a singleton presenter (e.g, with means of Dagger framework).
 *
 * The component is passed to {@link VisumClient#inject(Object)} method to inject dependencies into
 * the view.
 *
 * Created by defuera on 02/02/2016.
 */
public interface VisumClient {

    /**
     * Implementations must return a component for this view. E.g.:
     * "return getComponentCache().onStartClient(this);"
     *
     * @return  a component which provides the view with its own dependencies
     *
     * @see VisumClient#inject(Object)
     */
    @NonNull
    Object onStartClient();

    /**
     * Implementations must inject dependencies to the view using the given component. E.g.:
     * "((MyComponent) from).inject(this);"
     *
     * @param from  a component which provides the view with its own dependencies
     */
    void inject(@NonNull Object from);

    /**
     * Implementation must return local component cache. E.g.:
     * "return ((ComponentCacheProvider) context.getApplicationContext()).getComponentCache();"
     *
     * @return  local implementation of the {@link ComponentCache} class
     */
    @NonNull
    ComponentCache getComponentCache();

    /**
     * Called when the view is to be completely destroyed. Re-creation of the view should
     * not trigger this method. Implementations should destroy a component used by this
     * view.
     */
    void onStopClient();

}
