package io.reist.sandbox.time.view;

import io.reist.sandbox.time.presenter.TimePresenter;
import io.reist.visum.view.VisumView;

/**
 * Created by Reist on 20.05.16.
 */
public interface TimeView extends VisumView<TimePresenter> {
    void showTime(Long t);
}
