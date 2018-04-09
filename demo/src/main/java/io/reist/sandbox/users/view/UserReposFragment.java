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

package io.reist.sandbox.users.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.app.view.widget.LoaderView;
import io.reist.sandbox.users.UsersComponent;
import io.reist.sandbox.users.presenter.UserReposPresenter;

/**
 * Created by Reist on 10/13/15.
 */
public class UserReposFragment extends BaseFragment<UserReposPresenter> implements UserReposView {

    private static final String ARG_USER = "arg_user";

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.loader)
    LoaderView loaderView;

    @Inject
    UserReposPresenter presenter;

    private UserReposAdapter adapter;

    public UserReposFragment() {
        super(R.layout.fragment_user_repos);
    }

    public static UserReposFragment newInstance(Long userId) {
        UserReposFragment f = new UserReposFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ARG_USER, userId);
        f.setArguments(bundle);
        return f;
    }

    @Override
    public void attachPresenter() {

        super.attachPresenter();

        // setView this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // setView a linear layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(adapter = new UserReposAdapter());

        adapter.setOnLikeRepoClickListener(new UserReposAdapter.OnLikeRepoClickListener() {

            @Override
            public void onLikeRepoClick(Repo repo) {
                presenter.like(repo);
            }

            @Override
            public void onUnlikeRepoClick(Repo repo) {
                presenter.unlike(repo);
            }

        });

        loaderView.setOnRetryClickListener(v -> presenter.loadData(getUserId()));

    }

    @NonNull
    @Override
    public UserReposPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void showLoader(boolean show) {
        loaderView.showLoading(show);
    }

    @Override
    public Long getUserId() {
        return getArguments().getLong(ARG_USER);
    }

    @Override
    public void displayError(SandboxError error) {
        if (adapter == null || adapter.getItemCount() == 0) {
            loaderView.showNetworkError();
        } else {
            Snackbar
                    .make(mRecyclerView, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, v -> presenter.loadData(getUserId()))
                    .show();
        }
    }

    @Override
    public void displayData(List<Repo> data) {
        loaderView.hide();
        adapter.setRepos(data);
    }

    @Override
    public void inject(@NonNull Object component) {
        ((UsersComponent) component).inject(this);
    }

}
