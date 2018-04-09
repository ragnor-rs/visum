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

package io.reist.sandbox.feed.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import io.reist.dali.Dali;
import io.reist.dali.ScaleMode;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.app.view.widget.LoaderView;
import io.reist.sandbox.feed.FeedComponent;
import io.reist.sandbox.feed.presenter.FeedDetailPresenter;
import io.reist.visum.view.VisumFragment;

/**
 * Created by 4xes on 8/11/16.
 */
public class FeedDetailFragment extends BaseFragment<FeedDetailPresenter> implements FeedDetailView {

    private static final String EXTRA_POST_ID = "io.reist.sandbox.extra_post_id";

    @BindView(R.id.post_detail_image)
    ImageView image;

    @BindView(R.id.post_detail_title)
    TextView title;

    @BindView(R.id.post_detail_body)
    TextView body;

    @BindView(R.id.post_detail_container)
    ViewGroup postContainer;

    @BindView(R.id.loader)
    LoaderView loaderView;

    @Inject
    FeedDetailPresenter presenter;

    public FeedDetailFragment() {
        super(R.layout.fragment_post_detail);
    }

    public static VisumFragment newInstance(Long postId) {
        FeedDetailFragment fragment = new FeedDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_POST_ID, postId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public FeedDetailPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void displayError(SandboxError error) {
        loaderView.showNetworkError();
    }

    @Override
    public void displayData(Post post) {
        title.setText(post.title);
        body.setText(post.body);

        Dali.with(this)
            .load(post.image)
            .scaleMode(ScaleMode.CENTER_CROP)
            .into(image, false);
    }

    @Override
    public void displayLoader(boolean show) {
        postContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        loaderView.showLoading(show);
    }

    @Override
    public void back() {
        getFragmentManager().popBackStackImmediate();
    }

    @Override
    public long getPostId() {
        return getArguments().getLong(EXTRA_POST_ID);
    }

    @Override
    public void inject(@NonNull Object component) {
        ((FeedComponent) component).inject(this);
    }

}
