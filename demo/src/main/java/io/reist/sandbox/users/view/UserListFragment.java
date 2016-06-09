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

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.app.view.widget.LoaderView;
import io.reist.sandbox.users.UsersComponent;
import io.reist.sandbox.users.presenter.UserListAdapter;
import io.reist.sandbox.users.presenter.UserListPresenter;

/**
 * Created by m039 on 11/12/15.
 */
public class UserListFragment extends BaseFragment<UserListPresenter> implements UserListView {

    @Inject
    UserListPresenter mPresenter;

    @Bind(R.id.recycler)
    RecyclerView mRecyclerView;

    @Bind(R.id.loader)
    LoaderView mLoaderView;

    UserListAdapter mAdapter;

    public UserListFragment() {
        super(R.layout.fragment_users);
    }

    @Override
    public void attachPresenter() {

        super.attachPresenter();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter = new UserListAdapter());

        mAdapter.setOnUserClickListener(user ->
                getFragmentController().showFragment(UserReposFragment.newInstance(user.id), true));

    }

    @NonNull
    @Override
    public UserListPresenter getPresenter() {
        return mPresenter;
    }

    @Override
    public void inject(@NonNull Object from) {
        ((UsersComponent) from).inject(this);
    }

    @Override
    public void displayData(List<User> users) {
        mAdapter.setUsers(users);
        mLoaderView.hide();
    }

    @Override
    public void displayError(SandboxError error) {
        if (mAdapter == null || mAdapter.getItemCount() == 0) {
            mLoaderView.showNetworkError();
        } else {
            Snackbar
                    .make(mRecyclerView, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, v -> mPresenter.loadData())
                    .show();
        }
    }

    @Override
    public void showLoader(boolean show) {
        mLoaderView.showLoading(show);
    }
}
