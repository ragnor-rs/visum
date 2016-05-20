package io.reist.visum.view;

import android.os.Bundle;
import android.support.annotation.NonNull;

import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumPresenter;

/**
 * A helper class for implementations of {@link VisumView}. It persists {@link #componentId}
 * on configuration changes and provides callback for typical Android UI components such as
 * {@link android.app.Activity} and {@link android.app.Fragment}.
 *
 * @param <V>   MVP view class
 *
 * Created by Reist on 19.05.16.
 */
public class VisumViewHelper<V extends VisumView> extends VisumClientHelper<V> {

    private static final String ARG_STATE_COMPONENT_ID = VisumViewHelper.class.getName() + ".ARG_STATE_COMPONENT_ID";

    private final int viewId;

    private boolean stateSaved;

    public VisumViewHelper(int viewId, V view) {
        super(view);
        this.viewId = viewId;
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        componentId = savedInstanceState == null ? null : savedInstanceState.getLong(ARG_STATE_COMPONENT_ID);
        stateSaved = false;
    }

    public void onResume() {
        client.attachPresenter();
    }

    public void onPause() {
        client.detachPresenter();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(ARG_STATE_COMPONENT_ID, componentId);
        stateSaved = true;
    }

    public void onDestroy() {
        if (!stateSaved) {
            super.onDestroy();
        }
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void attachPresenter() {
        final VisumPresenter<V> presenter = client.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, client);
        }
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void detachPresenter() {
        final VisumPresenter<V> presenter = client.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, null);
        }
    }

}
