package io.reist.visum.view;

import android.content.Context;
import android.support.annotation.NonNull;

import io.reist.visum.presenter.TestPresenter;

/**
 * Created by Reist on 16.06.16.
 */
public class TestVisumWidget extends VisumWidget<TestPresenter> implements VisumViewWithPresenterSetter {

    private TestPresenter presenter;

    public TestVisumWidget(Context context) {
        super(VisumViewTest.VIEW_ID, context);
    }

    @Override
    protected int getLayoutRes() {
        return 0;
    }

    @Override
    public TestPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void inject(@NonNull Object from) {
        ((VisumViewTest.TestSubComponent) from).inject(this);
    }

    @Override
    public void setPresenter(TestPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    protected void inflate() {
    }

}
