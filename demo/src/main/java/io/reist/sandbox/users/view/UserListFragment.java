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

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
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

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.loader)
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

    @Override
    public void inject(@NonNull Object component) {
        ((UsersComponent) component).inject(this);
    }

}
