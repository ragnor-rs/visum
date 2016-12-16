package io.reist.visum.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.SingleViewPresenter;
import io.reist.visum.presenter.VisumPresenter;

/**
 * A helper class for implementations of {@link VisumView}. It provides callback for
 * typical Android UI components such as {@link android.app.Activity} and
 * {@link android.app.Fragment}.
 * <p>
 * Created by Reist on 19.05.16.
 */
public final class VisumViewHelper<P extends VisumPresenter> {

    private static final String LOG_TAG = VisumViewHelper.class.getSimpleName();

    private final int viewId;
    private final VisumClientHelper<? extends VisumView<P>> helper;

    public VisumViewHelper(int viewId, @NonNull VisumClientHelper<? extends VisumView<P>> helper) {
        this.viewId = viewId;
        this.helper = helper;
    }

    public VisumViewHelper(@NonNull VisumClientHelper<? extends VisumView<P>> helper) {
        this.viewId = SingleViewPresenter.VIEW_ID_DEFAULT;
        this.helper = helper;
    }

    public P getPresenter() {
        return helper.getClient().getPresenter();
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void attachPresenter() {
        VisumView<P> view = helper.getClient();
        VisumPresenter presenter = view.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, view);
        } else {
            Log.w(LOG_TAG, "presenter is null");
        }
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void detachPresenter() {
        VisumView<P> view = helper.getClient();
        VisumPresenter presenter = view.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, null);
        } else {
            Log.w(LOG_TAG, "presenter is null");
        }
    }

    public ComponentCache getComponentCache() {
        return helper.getComponentCache();
    }

    public void onCreate() {
        helper.onCreate();
    }

    public void onDestroy(boolean retainComponent) {
        helper.onDestroy(retainComponent);
    }

    @NonNull
    public Context getContext() {
        return helper.getContext();
    }

}
