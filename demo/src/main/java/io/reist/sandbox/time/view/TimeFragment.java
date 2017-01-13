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

import android.annotation.SuppressLint;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reist.sandbox.R;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.time.presenter.TimePresenter;

/**
 * This class is a part of the example of single presenter - multiple views feature of Visum.
 *
 * @see TimeNotification
 * @see TimePresenter
 *
 * Created by Reist on 20.05.16.
 */
public class TimeFragment extends BaseFragment<TimePresenter> implements TimeView {

    @BindView(R.id.time_text)
    TextView timeText;

    @Inject
    TimePresenter presenter;

    public TimeFragment() {
        super(TimePresenter.VIEW_ID_MAIN, R.layout.fragment_time);
    }

    @Override
    public TimePresenter getPresenter() {
        return presenter;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void showTime(Long t) {
        timeText.setText(Long.toString(t));
    }

    @OnClick(R.id.show_time_btn)
    void onShowTimeClicked() {
        presenter.onShowTimeClicked(getActivity());
    }

    @OnClick(R.id.hide_time_btn)
    void onHideTimeClicked() {
        presenter.onHideTimeClicked();
    }

}
