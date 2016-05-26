package io.reist.visum.view;

import android.content.Context;
import android.support.annotation.NonNull;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumPresenter;

/**
 * A helper class for implementations of {@link VisumView}. It provides callback for
 * typical Android UI components such as {@link android.app.Activity} and
 * {@link android.app.Fragment}.
 *
 * Created by Reist on 19.05.16.
 */
public final class VisumViewHelper<P extends VisumPresenter> implements VisumView<P> {

    private final int viewId;
    private final VisumClientHelper<? extends VisumView<P>> helper;

    private boolean stateSaved;

    public VisumViewHelper(int viewId, @NonNull VisumClientHelper<? extends VisumView<P>> helper) {
        this.viewId = viewId;
        this.helper = helper;
    }

    @Override
    public P getPresenter() {
        return helper.getClient().getPresenter();
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void attachPresenter() {
        VisumView<P> view = helper.getClient();
        view.attachPresenter();
        VisumPresenter presenter = view.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, view);
        }
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void detachPresenter() {
        VisumView<P> view = helper.getClient();
        view.detachPresenter();
        VisumPresenter presenter = view.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, null);
        }
    }

    @Override
    public ComponentCache getComponentCache() {
        return helper.getComponentCache();
    }

    @Override
    public void onStartClient() {
        if (!stateSaved) {
            helper.onStartClient();
        } else {
            helper.onRestartClient();
        }
    }

    @Override
    public void onStopClient() {
        if (!stateSaved) {
            helper.onStopClient();
        }
    }

    @Override
    public void inject(@NonNull Object from) {
        helper.inject(from);
    }

    @NonNull
    @Override
    public Context getContext() {
        return helper.getContext();
    }

    public void onRestoreInstanceState() {
        stateSaved = false;
    }

    public void onSaveInstanceState() {
        stateSaved = true;
    }

}
