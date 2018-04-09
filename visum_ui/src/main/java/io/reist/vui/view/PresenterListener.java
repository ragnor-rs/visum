package io.reist.vui.view;

import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by reist on 07.04.17.
 */

public interface PresenterListener<P extends VisumPresenter> {

    void onPresenterAttached(P presenter);

    void onPresenterDetached();

}
