package io.reist.visum.view;

import io.reist.visum.ComponentCache;
import io.reist.visum.TestClient;
import io.reist.visum.presenter.TestPresenter;

/**
 * Created by Reist on 26.05.16.
 */
public abstract class TestView extends TestClient implements VisumView<TestPresenter> {

    private final TestPresenter presenter;
    private final int viewId;

    protected TestView(ComponentCache componentCache, TestPresenter presenter, int viewId) {
        super(componentCache);
        this.presenter = presenter;
        this.viewId = viewId;
    }

    @Override
    public TestPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void attachPresenter() {
        onStartClient();
        presenter.setView(viewId, this);
    }

    @Override
    public void detachPresenter() {
        presenter.setView(viewId, null);
        onStopClient();
    }

}
