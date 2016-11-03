package io.reist.visum.view;

import io.reist.visum.BaseTestClient;
import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.TestPresenter;

/**
 * Created by Reist on 26.05.16.
 */
public abstract class BaseTestView extends BaseTestClient implements VisumView<TestPresenter> {

    private final TestPresenter presenter;
    private final VisumViewHelper<TestPresenter> helper;

    protected BaseTestView(ComponentCache componentCache, TestPresenter presenter, int viewId) {
        super(componentCache);
        this.presenter = presenter;
        helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }

    @Override
    public TestPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void attachPresenter() {
        helper.attachPresenter();
    }

    @Override
    public void detachPresenter() {
        helper.detachPresenter();
    }

    @Override
    public boolean isPresenterAttached() {
        return helper.isPresenterAttached();
    }

}
