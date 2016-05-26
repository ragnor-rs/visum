package io.reist.visum.presenter;

import io.reist.visum.ComponentCache;
import io.reist.visum.TestClient;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.view.VisumView;
import io.reist.visum.view.VisumViewHelper;

/**
 * Created by Reist on 26.05.16.
 */
public abstract class TestView extends TestClient implements VisumView<TestPresenter> {

    private final TestPresenter presenter;
    private final VisumViewHelper<TestPresenter> helper;

    protected TestView(ComponentCache componentCache, TestPresenter presenter, int viewId) {
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
        helper.onStartClient();
        helper.attachPresenter();
    }

    @Override
    public void detachPresenter() {
        helper.detachPresenter();
        helper.onStopClient();
    }

}
