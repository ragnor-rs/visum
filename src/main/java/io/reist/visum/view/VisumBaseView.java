package io.reist.visum.view;

import android.content.Context;
import android.util.Log;

import io.reist.visum.ComponentCache;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Reist on 20.05.16.
 */
public abstract class VisumBaseView<P extends VisumPresenter> implements VisumView<P> {

    private static final String TAG = VisumBaseView.class.getName();

    private final VisumViewHelper<VisumBaseView<P>> viewHelper;

    private final Context context;

    private boolean created;

    /**
     * @deprecated use {@link #VisumBaseView(int, Context)} instead
     */
    @SuppressWarnings({"deprecation", "unused"})
    @Deprecated
    public VisumBaseView(Context context) {
        this(VisumPresenter.VIEW_ID_DEFAULT, context);
    }

    public VisumBaseView(int viewId, Context context) {
        this.viewHelper = new VisumViewHelper<>(viewId, this);
        this.context = context;
    }


    //region VisumClient implementation

    @Override
    public final Long getComponentId() {
        return viewHelper.getComponentId();
    }

    @Override
    public final void setComponentId(Long componentId) {
        viewHelper.setComponentId(componentId);
    }

    @Override
    public final Object getComponent() {
        return viewHelper.getComponent();
    }

    @Override
    public final ComponentCache getComponentCache() {
        return viewHelper.getComponentCache(context);
    }

    //endregion


    //region VisumView implementation

    @Override
    public void onInvalidateComponent() {
        viewHelper.onInvalidateComponent();
    }

    @Override
    public void attachPresenter() {
        if (created) {
            Log.d(TAG, "VisumBaseView is already created");
            return;
        }
        viewHelper.onCreate();
        viewHelper.onResume();
        created = true;
        viewHelper.attachPresenter();
    }

    @Override
    public void detachPresenter() {
        viewHelper.detachPresenter();
        if (!created) {
            Log.d(TAG, "VisumBaseView is not created");
            return;
        }
        viewHelper.onPause();
        viewHelper.onDestroy();
        created = false;
    }

    //endregion


}
