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

package io.reist.sandbox.users.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
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

    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;

    @Bind(R.id.loader)
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

    @Override
    public void inject(@NonNull Object from) {
        ((UsersComponent) from).inject(this);
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

}
