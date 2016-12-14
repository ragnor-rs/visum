package io.reist.visum.view;

import android.content.Context;
import android.support.annotation.CallSuper;

import io.reist.visum.VisumBaseClient;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.VisumBasePresenter;

/**
 * Created by Reist on 20.05.16.
 */
@SuppressWarnings("unused")
public abstract class VisumBaseView<P extends VisumBasePresenter>
        extends VisumBaseClient implements VisumView<P> {

    private static final String TAG = VisumBaseView.class.getName();

    private final VisumViewHelper<P> helper;

    public VisumBaseView(Context context) {
        super(context);
        this.helper = new VisumViewHelper<>(new VisumClientHelper<>(this));
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

    @Override
    public int getViewId() {
        return VisumView.DEFAULT_ID;
    }
    //endregion

}
