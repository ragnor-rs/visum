package io.reist.visum.view;

import android.content.Context;
import android.support.annotation.NonNull;

import io.reist.visum.presenter.TestPresenter;

/**
 * Created by Reist on 16.06.16.
 */
class TestVisumView extends VisumBaseView<TestPresenter> implements VisumDynamicPresenterView {

    private TestPresenter presenter;

    public TestVisumView(Context context) {
        super(VisumViewTest.VIEW_ID, context);
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

}
