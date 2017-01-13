/*
 * Copyright (C) 2017 Renat Sarymsakov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.reist.sandbox.time.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.time.model.TimeService;
import io.reist.sandbox.time.view.TimeNotification;
import io.reist.sandbox.time.view.TimeView;
import io.reist.visum.presenter.VisumPresenter;

/**
 * This class is a part of the example of single presenter - multiple views feature of Visum.
 * TimePresenter manages two {@link TimeView} identified by {@link #VIEW_ID_MAIN} and
 * {@link #VIEW_ID_NOTIFICATION}.
 *
 * @see io.reist.sandbox.time.view.TimeNotification
 * @see io.reist.sandbox.time.view.TimeFragment
 */
@Singleton
public class TimePresenter extends VisumPresenter<TimeView> {

    public static final int VIEW_ID_MAIN = 1;
    public static final int VIEW_ID_NOTIFICATION = 2;

    private final TimeService timeService;

    private TimeNotification timeNotification;

    @Inject
    public TimePresenter(TimeService timeService) {
        this.timeService = timeService;
    }

    @Override
    protected void onViewAttached(int id, @NonNull TimeView view) {
        subscribe(id, timeService.getTime(), view::showTime);
    }

    public void onShowTimeClicked(Context context) {
        if (timeNotification == null) {
            timeNotification = new TimeNotification(context);
        }

        if (!timeNotification.getComponentCache().isClientAttached(timeNotification)) {
            timeNotification.attachPresenter();
        }
    }

    public void onHideTimeClicked() {
        if (timeNotification == null) {
            return;
        }
        timeNotification.detachPresenter();
    }

}
