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

package io.reist.sandbox.repos.view;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.app.view.widget.LoaderView;
import io.reist.sandbox.repos.ReposComponent;
import io.reist.sandbox.repos.presenter.RepoListAdapter;
import io.reist.sandbox.repos.presenter.RepoListPresenter;

/**
 * Created by Reist on 10/13/15.
 */
public class RepoListFragment extends BaseFragment<RepoListPresenter> implements RepoListView {

    @BindView(R.id.daggertest_repo_recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.loader)
    LoaderView loaderView;

    @Inject
    RepoListPresenter presenter;

    private RepoListAdapter adapter;

    public RepoListFragment() {
        super(R.layout.github_fragment);
    }

    public static RepoListFragment newInstance() {
        return new RepoListFragment();
    }

    @Override
    public void attachPresenter() {

        super.attachPresenter();

        // setView this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // setView a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        loaderView.setOnRetryClickListener(v -> presenter.loadData());

    }

    @OnClick(R.id.create_repo_button)
    void onCreateRepoClicked() {
        presenter.createRepo();
    }

    @Override
    public RepoListPresenter getPresenter() {
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
                    .make(mRecyclerView, R.string.network_error, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.retry, v -> presenter.loadData())
                    .show();
        }
    }

    @Override
    public void displayData(List<Repo> data) {
        loaderView.hide();
        adapter = new RepoListAdapter(data);
        adapter.setItemClickListener(repo -> getFragmentController().showFragment(RepoEditFragment.newInstance(repo.id), false));
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void displaySuccess() {
        Toast.makeText(getActivity(), R.string.repo_saved, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRepoUpdated(Repo r) {
        adapter.onItemUpdated(r);
    }

    @Override
    public void inject(@NonNull Object component) {
        ((ReposComponent) component).inject(this);
    }

}
