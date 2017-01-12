package io.reist.visum.view;

import io.reist.visum.BaseTestClient;
import io.reist.visum.ComponentCache;
import io.reist.visum.VisumClientHelper;
import io.reist.visum.presenter.SingleViewPresenter;
import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Reist on 26.05.16.
 */
public abstract class BaseTestView extends BaseTestClient implements VisumView<VisumPresenter> {

    private final VisumPresenter presenter;
    private final VisumViewHelper<VisumPresenter> helper;

    protected BaseTestView(ComponentCache componentCache, VisumPresenter presenter) {
        this(componentCache, presenter, SingleViewPresenter.DEFAULT_VIEW_ID);
    }

    protected BaseTestView(ComponentCache componentCache, VisumPresenter presenter, int viewId) {
        super(componentCache);
        this.presenter = presenter;
        helper = new VisumViewHelper<>(viewId, new VisumClientHelper<>(this));
    }

    @Override
    public VisumPresenter getPresenter() {
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

}
