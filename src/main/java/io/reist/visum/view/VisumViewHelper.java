package io.reist.visum.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumPresenter;

/**
 * A helper class for implementations of {@link VisumView}. It persists
 * {@link VisumClientHelper#componentId} during configuration changes and provides callback for
 * typical Android UI components such as {@link android.app.Activity} and
 * {@link android.app.Fragment}.
 *
 * Created by Reist on 19.05.16.
 */
public final class VisumViewHelper {

    private static final String ARG_STATE_COMPONENT_ID = VisumViewHelper.class.getName() + ".ARG_STATE_COMPONENT_ID";

    private final int viewId;
    private final VisumClientHelper helper;

    private boolean stateSaved;

    public VisumViewHelper(int viewId, VisumClientHelper helper) {
        this.viewId = viewId;
        this.helper = helper;
    }

    public Long getComponentId() {
        return helper.getComponentId();
    }

    public void setComponentId(Long componentId) {
        helper.setComponentId(componentId);
    }

    public Object getComponent() {
        return helper.getComponent();
    }

    public ComponentCache getComponentCache(Context context) {
        return helper.getComponentCache(context);
    }

    public void onInvalidateComponent() {
        helper.onInvalidateComponent();
    }

    public void onCreate() {
        helper.onCreate();
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            helper.setComponentId(savedInstanceState.getLong(ARG_STATE_COMPONENT_ID));
        }
        stateSaved = false;
    }

    public void onResume() {
        ((VisumView) helper.getClient()).attachPresenter();
    }

    public void onPause() {
        ((VisumView) helper.getClient()).detachPresenter();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(ARG_STATE_COMPONENT_ID, helper.getComponentId());
        stateSaved = true;
    }

    public void onDestroy() {
        if (!stateSaved) {
            helper.onDestroy();
        }
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void attachPresenter() {
        VisumView view = ((VisumView) helper.getClient());
        VisumPresenter presenter = view.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, view);
        }
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void detachPresenter() {
        VisumView view = ((VisumView) helper.getClient());
        VisumPresenter presenter = view.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, null);
        }
    }

    public int getViewId() {
        return viewId;
    }

}
