package io.reist.sandbox.time.view;

import io.reist.sandbox.time.presenter.TimePresenter;
import io.reist.visum.view.VisumView;

/**
 * Created by Reist on 20.05.16.
 */
public interface TimeView extends VisumView<TimePresenter> {
    int ID_MAIN = 1;
    int ID_NOTIFICATION = 2;

    void showTime(Long t);
}
