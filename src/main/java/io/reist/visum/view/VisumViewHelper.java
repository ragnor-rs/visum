package io.reist.visum.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import io.reist.visum.ComponentCache;
import io.reist.visum.ComponentCacheProvider;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Reist on 19.05.16.
 */
public class VisumViewHelper {

    private static final String ARG_STATE_COMPONENT_ID = VisumViewHelper.class.getName() + ".ARG_STATE_COMPONENT_ID";

    private final int viewId;
    private final VisumView view;

    private Long componentId;
    private boolean stateSaved;

    public VisumViewHelper(int viewId, VisumView view) {
        this.viewId = viewId;
        this.view = view;
    }

    public Long getComponentId() {
        return componentId;
    }

    public void setComponentId(Long componentId) {
        this.componentId = componentId;
    }

    public Object getComponent() {
        ComponentCache componentCache = view.getComponentCache();
        if (componentCache != null) {
            return componentCache.getComponentFor(view);
        } else {
            return null;
        }
    }

    public ComponentCache getComponentCache(Context context) {
        return ((ComponentCacheProvider) context.getApplicationContext()).getComponentCache();
    }

    public void onCreate() {
        view.inject(view.getComponent());
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        componentId = savedInstanceState == null ? null : savedInstanceState.getLong(ARG_STATE_COMPONENT_ID);
        stateSaved = false;
    }

    public void onResume() {
        view.attachPresenter();
    }

    public void onPause() {
        view.detachPresenter();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(ARG_STATE_COMPONENT_ID, componentId);
        stateSaved = true;
    }

    public void onDestroy() {
        if (!stateSaved) {
            view.onInvalidateComponent();
        }
    }

    public void onInvalidateComponent() {
        view.getComponentCache().invalidateComponentFor(view);
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void attachPresenter() {
        final VisumPresenter presenter = view.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, view);
        }
    }

    @SuppressWarnings("unchecked") // todo setView should be checked call
    public void detachPresenter() {
        final VisumPresenter presenter = view.getPresenter();
        if (presenter != null) {
            presenter.setView(viewId, null);
        }
    }

}
