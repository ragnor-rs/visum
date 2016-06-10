package io.reist.sandbox.result.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reist.sandbox.R;
import io.reist.sandbox.result.ResultComponent;
import io.reist.sandbox.result.presenter.ResultPresenter;
import io.reist.visum.view.VisumActivity;

/**
 * This activity demonstrates how a result received from another activity can be passed directly
 * to a {@link io.reist.visum.presenter.VisumPresenter}. This was made possible by performing
 * dependency injection in {@link VisumActivity#onActivityResult(int, int, Intent)}. Among other
 * dependencies, a presenter is injected directly into the activity. Since then, you can call any
 * of presenter's methods. In this example, the method is {@link ResultPresenter#receiveResult()}.
 *
 * Created by Reist on 07.06.16.
 */
public class ResultActivity extends VisumActivity<ResultPresenter> implements ResultView {

    private static final int REQUEST_CODE = 1;

    @Inject
    ResultPresenter presenter;

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_result;
    }

    @Override
    public ResultPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void inject(@NonNull Object from) {
        ((ResultComponent) from).inject(this);
    }

    @OnClick(R.id.start_and_receive_btn)
    void onStartAndReceiveClicked() {
        startActivityForResult(new Intent(this, SubActivity.class), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.receiveResult();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

}
