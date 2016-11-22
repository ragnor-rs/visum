package io.reist.visum.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumBasePresenter;
import io.reist.visum.presenter.VisumViewPresenter;
import io.reist.visum.presenter.VisumViewsPresenter;

/**
 * A helper class for implementations of {@link VisumView}. It provides callback for
 * typical Android UI components such as {@link android.app.Activity} and
 * {@link android.app.Fragment}.
 * <p>
 * Created by Reist on 19.05.16.
 */
public final class VisumViewHelper<P extends VisumBasePresenter> {

    private static final String LOG_TAG = VisumViewHelper.class.getSimpleName();

    private final VisumClientHelper<? extends VisumView<P>> helper;

    public VisumViewHelper(@NonNull VisumClientHelper<? extends VisumView<P>> helper) {
        this.helper = helper;
    }

    public P getPresenter() {
        return helper.getClient().getPresenter();
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void attachPresenter() {
        VisumView<P> view = helper.getClient();
        VisumBasePresenter presenter = view.getPresenter();
        if (presenter != null) {
            if (presenter instanceof VisumViewsPresenter) {
                ((VisumViewsPresenter) presenter).setView(view.getViewId(), view);
            }
            if (presenter instanceof VisumViewPresenter) {
                ((VisumViewPresenter) presenter).setView(view);
            }
        } else {
            Log.w(LOG_TAG, "presenter is null");
        }
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void detachPresenter() {
        VisumView<P> view = helper.getClient();
        VisumBasePresenter presenter = view.getPresenter();
        if (presenter != null) {
            if (presenter instanceof VisumViewsPresenter) {
                ((VisumViewsPresenter) presenter).setView(view.getViewId(), null);
            }
            if (presenter instanceof VisumViewPresenter) {
                ((VisumViewPresenter) presenter).setView(null);
            }
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
