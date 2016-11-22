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

import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.app.view.widget.LoaderView;
import io.reist.sandbox.feed.presenter.FeedListAdapter;
import io.reist.sandbox.feed.presenter.FeedListPresenter;

/**
 * Created by 4xes on 7/11/16.
 */
public class FeedListFragment extends BaseFragment<FeedListPresenter> implements FeedListView {

    @BindView(R.id.feed_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.loader)
    LoaderView loaderView;

    @Inject
    FeedListPresenter presenter;

    private FeedListAdapter adapter;

    public FeedListFragment() {
        super(R.layout.fragment_feed);
    }

    @Override
    public void attachPresenter() {

        super.attachPresenter();

        // setView this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // setView a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        loaderView.setOnRetryClickListener(v -> presenter.loadData());

    }

    @Override
    public FeedListPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showLoader(boolean show) {
        loaderView.showLoading(show);
    }

    @Override
    public void displayError(SandboxError error) {
        if (adapter == null || adapter.getItemCount() == 0) {
            loaderView.showNetworkError();
        } else {
            Snackbar
                    .make(recyclerView, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, v -> presenter.loadData())
                    .show();
        }
    }

    @Override
    public void displayData(List<Post> data) {
        loaderView.hide();
        adapter = new FeedListAdapter(data);
        adapter.setItemClickListener(post ->
                getFragmentController().showFragment(FeedDetailFragment.newInstance(post.id), false));
        recyclerView.setAdapter(adapter);
    }

}
