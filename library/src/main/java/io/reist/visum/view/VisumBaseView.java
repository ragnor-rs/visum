package io.reist.visum.view;

import android.content.Context;
import android.support.annotation.CallSuper;

import io.reist.visum.VisumBaseClient;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.SingleViewPresenter;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Reist on 20.05.16.
 */
@SuppressWarnings("unused")
public abstract class VisumBaseView<P extends VisumPresenter>
        extends VisumBaseClient implements VisumView<P> {

    private static final String TAG = VisumBaseView.class.getName();

    private final VisumViewHelper<P> helper;

    public VisumBaseView(Context context) {
        this(SingleViewPresenter.VIEW_ID_DEFAULT, context);
    }

    public VisumBaseView(int viewId, Context context) {
        super(context);
        this.helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }


    //region VisumView implementation

    @Override
    @CallSuper
    public void attachPresenter() {
        onStartClient();
        helper.attachPresenter();
    }

    @Override
    @CallSuper
    public void detachPresenter() {
        helper.detachPresenter();
        onStopClient();
    }

    //endregion


}
