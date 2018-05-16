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

package io.reist.sandbox.time.view;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import java.util.Date;

import javax.inject.Inject;

import io.reist.sandbox.R;
import io.reist.sandbox.app.view.MainActivity;
import io.reist.sandbox.time.presenter.TimePresenter;
import io.reist.visum.view.VisumBaseView;

/**
 * This class is a part of the example of single presenter - multiple views feature of Visum.
 *
 * @see TimeFragment
 * @see TimePresenter
 *
 * Created by Reist on 20.05.16.
 */
public class TimeNotification extends VisumBaseView<TimePresenter> implements TimeView {

    private static final int NOTIFICATION_ID = 1;

    @Inject
    TimePresenter presenter;

    public TimeNotification(Context context) {
        super(TimePresenter.VIEW_ID_NOTIFICATION, context);
    }

    @Override
    public void showTime(Long t) {

        Context context = getContext();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(context)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.current_time, new Date(t)))
                .setContentIntent(pendingIntent)
                .setOngoing(true)
                .setSmallIcon(android.R.drawable.ic_menu_agenda)
                .build();
        notificationManager.notify(NOTIFICATION_ID, notification);

    }

    @Override
    public TimePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void detachPresenter() {

        super.detachPresenter();

        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(NOTIFICATION_ID);

    }

}
