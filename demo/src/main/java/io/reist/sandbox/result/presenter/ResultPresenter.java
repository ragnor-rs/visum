package io.reist.sandbox.result.presenter;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.result.view.ResultView;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Reist on 07.06.16.
 */
@Singleton
public class ResultPresenter extends VisumPresenter<ResultView> {

    private static final String TAG = ResultPresenter.class.getSimpleName();

    public void receiveResult() {
        Log.i(TAG, "Presenter received a result successfully");
    }

    @Inject
    public ResultPresenter() {}

}
