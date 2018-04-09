package io.reist.vui.view;

import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by reist on 07.04.17.
 */

interface VisumCompositeView<P extends VisumPresenter> extends CompositeView, PresenterListener<P> {}
