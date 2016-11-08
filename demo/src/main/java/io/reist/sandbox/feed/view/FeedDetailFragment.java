/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of MVP-Sandbox.
 *
 * MVP-Sandbox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MVP-Sandbox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MVP-Sandbox.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.reist.sandbox.feed.view;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.app.view.widget.LoaderView;
import io.reist.sandbox.feed.model.local.Post;
import io.reist.sandbox.feed.presenter.FeedDetailPresenter;
import io.reist.visum.view.VisumFragment;

/**
 * Created by 4xes on 8/11/16.
 */
public class FeedDetailFragment extends BaseFragment<FeedDetailPresenter> implements FeedDetailView {

    private static final String EXTRA_POST_ID = "io.reist.sandbox.extra_post_id";

    @BindView(R.id.post_title)
    TextView title;

    @BindView(R.id.post_body)
    TextView body;

    @BindView(R.id.post_container)
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


}
