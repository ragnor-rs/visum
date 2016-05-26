package io.reist.visum.view;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.util.Log;

import io.reist.visum.VisumBaseClient;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Reist on 20.05.16.
 */
@SuppressWarnings("unused")
public abstract class VisumBaseView<P extends VisumPresenter>
        extends VisumBaseClient implements VisumView<P> {

    private static final String TAG = VisumBaseView.class.getName();

    private final VisumViewHelper<P> helper;

    private boolean attachedToPresenter;

    /**
     * @deprecated use {@link #VisumBaseView(int, Context)} instead
     */
    @SuppressWarnings({"deprecation", "unused"})
    @Deprecated
    public VisumBaseView(Context context) {
        this(VisumPresenter.VIEW_ID_DEFAULT, context);
    }

    public VisumBaseView(int viewId, Context context) {
        super(context);
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }


    //region VisumView implementation

    @Override
    @CallSuper
    public void attachPresenter() {
        if (attachedToPresenter) {
            Log.d(TAG, "VisumBaseView is already created");
            return;
        }
        attachedToPresenter = true;
        helper.onStartClient();
        helper.attachPresenter();
    }

    @Override
    @CallSuper
    public void detachPresenter() {
        if (!attachedToPresenter) {
            Log.d(TAG, "VisumBaseView is not created");
            return;
        }
        attachedToPresenter = false;
        helper.detachPresenter();
        helper.onStopClient();
    }

    //endregion


}
