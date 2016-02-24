package io.reist.visum;

import android.os.Bundle;

/**
 * Created by defuera on 02/02/2016.
 * <p>
 * Visum helps you resolve two problems:
 * Construct an MVP architecture and organize inversion of control.
 * Hence it has two interfaces: VisumView and VisumClient.
 * </p><p>
 * VisumClient is an interfaces providing behavior for Views that has dependency injections.
 * Visum recomends following structure of application:
 * Views (ussually it's an {@link android.app.Activity}, {@link android.app.Fragment} or {@link android.view.View})
 * have Presenters injected into them. Being recreated on orientation changed view itself looses all it data, except you save it manually with saveInstantState.
 * But presenter survives orientation change, which gives you a lot of affort not needing to reload big amounts of data.
 * "Magic!" - you say. Yeah. And all this magic happens in your implementation of {@link ComponentCache}
 * (checkout sample <a href="https://github.com/ragnor-rs/mvp-sandbox/blob/develop/app/src/main/java/io/reist/sandbox/app/SandboxComponentCache.java">SandboxComponentCache</a>).
 * </p><p>
 * To provide such cozy expirience you need to have a Singleton Component and a Singleton Presenter.
 * Same component should be always provided into {@link VisumClient#inject(Object)} method for the same view.
 * Talking about the same view we mean more that same instace of a view. It's same view as a logical concept.
 * To easily distinguish same view from another view is just think about it as the same screen or just the same stuff.
 * Just make your view implement VisumClient and follow the interface.
 * </p>
 */
public interface VisumClient {

    /**
     * @return componentId for current view. ComponentId should survive orientation change.
     * Easiest way to implement it is to save it via saveInstantState.
     * See {@link io.reist.visum.view.VisumFragment#onSaveInstanceState(Bundle)} for example.
     */
    Long getComponentId();

    /**
     * Don't worry about it - this is resolved by {@link ComponentCache}.
     * New id is created for every new view.
     * @param componentId the id
     */
    void setComponentId(Long componentId);

    /**
     * Usual implementation is following:
     * return getComponentCache().getComponentFor(this);
     * @return the component to be provided as a parameter of {@link VisumClient#inject(Object)}
     */
    Object getComponent();

    /**
     * Implementation is always trivial:
     * ((MyComponent) from).inject(this);
     * @param from yor component
     */
    void inject(Object from);

    /**
     *
     * @return your implementation of {@link ComponentCache}
     */
    ComponentCache getComponentCache();

}
