package io.reist.vui.view;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.CallSuper;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reist.visum.presenter.VisumPresenter;
import io.reist.visum.view.VisumActivity;

/**
 * Prevents client code from overriding standard Android lifecycle methods.
 * This is to minimize bugs regarding view attachment. For proper initialization / cleanup, use
 * {@link #init(Context, Bundle)}, {@link #attachPresenter()} and {@link #detachPresenter()}.
 *
 * Created by Reist on 15.03.17.
 */
public abstract class VisumCompositeActivity<P extends VisumPresenter>
        extends VisumActivity<P>
        implements VisumCompositeView<P> {

    private Unbinder unbinder;

    @Override
    public final void bindUiElements() {
        if (unbinder == null) {
            unbinder = ButterKnife.bind(this);
        }
    }

    @Override
    public final void unbindUiElements() {
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    @Override
    public final void rebindUiElements() {
        unbindUiElements();
        bindUiElements();
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
//        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
        super.onCreate(savedInstanceState);
        bindUiElements();
        init(getContext(), savedInstanceState);
    }

    @Override
    public final void onResume() {
        super.onResume();
    }

    @Override
    public final void onPause() {
        super.onPause();
    }

    @Override
    public final void onDestroy() {
        super.onDestroy();
    }

    @Override
    public final void attachPresenter() {
        if (!isFinishing()) {

            bindUiElements();
            super.attachPresenter();
            onPresenterAttached(getPresenter());

        }
    }

    @Override
    public final void detachPresenter() {
        onPresenterDetached();
        super.detachPresenter();
        unbindUiElements();
    }

    @CallSuper
    @Override
    public void init(Context context, Bundle savedInstanceState) {}

    @CallSuper
    @Override
    public void onPresenterAttached(P presenter) {}

    @CallSuper
    @Override
    public void onPresenterDetached() {}

}
