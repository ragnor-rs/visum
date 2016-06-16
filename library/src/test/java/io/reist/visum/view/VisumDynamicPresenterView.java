package io.reist.visum.view;

import io.reist.visum.presenter.TestPresenter;

/**
 * Created by Reist on 07.06.16.
 */
interface VisumDynamicPresenterView extends VisumView<TestPresenter> {

    void setPresenter(TestPresenter testPresenter);

}
