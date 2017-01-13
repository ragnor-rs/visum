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

package io.reist.sandbox.app.view.widget;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reist.sandbox.R;

/**
 * Created by defuera on 10/11/2015.
 */
public class LoaderView extends FrameLayout {

    @BindView(R.id.progress_bar)
    View progressBar;

    @BindView(R.id.message)
    View message;

    private OnClickListener retryClickListener;

    public LoaderView(Context context) {
        super(context);
        init();
    }

    public LoaderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_loader, this);
        ButterKnife.bind(this);
    }

    public void showLoading(boolean show) {
        setVisibility(show ? VISIBLE : GONE);
        message.setVisibility(GONE);
        setOnClickListener(null);
    }

    public void hide() {
        setVisibility(GONE);
        setOnClickListener(null);
    }

    public void showNetworkError() {
        setVisibility(VISIBLE);
        progressBar.setVisibility(GONE);
        message.setVisibility(VISIBLE);
        setOnClickListener(v -> {
            if (retryClickListener != null) {
                if (!isNetworkAvailable()) {
                    Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
                } else {
                    showLoading(true);
                    retryClickListener.onClick(v);
                }
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();

    }

    public void setOnRetryClickListener(OnClickListener retryClickListener) {
        this.retryClickListener = retryClickListener;
    }
}
