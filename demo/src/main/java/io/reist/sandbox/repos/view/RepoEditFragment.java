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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reist.sandbox.R;
import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.view.BaseFragment;
import io.reist.sandbox.app.view.widget.LoaderView;
import io.reist.sandbox.repos.ReposComponent;
import io.reist.sandbox.repos.presenter.RepoEditPresenter;
import io.reist.visum.view.VisumFragment;

/**
 * Created by defuera on 10/11/2015.
 */
public class RepoEditFragment extends BaseFragment<RepoEditPresenter> implements RepoEditView {

    private static final String EXTRA_REPO_ID = "io.reist.sandbox.extra_repo_id";

    @BindView(R.id.repo_name)
    TextView repoName;

    @BindView(R.id.author_name)
    TextView authorName;

    @BindView(R.id.repo_url)
    TextView repoUrl;

    @BindView(R.id.loader)
    LoaderView loaderView;

    @BindView(R.id.repo_container)
    ViewGroup repoContainer;

    @Inject
    RepoEditPresenter presenter;

    public RepoEditFragment() {
        super(R.layout.fragment_edit_repo);
    }

    public static VisumFragment newInstance(Long repoId) {
        RepoEditFragment fragment = new RepoEditFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(EXTRA_REPO_ID, repoId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public RepoEditPresenter getPresenter() {
        return presenter;
    }

    @OnClick(R.id.save)
    void onSaveButtonClick() {
        presenter.saveRepo(
                repoName.getText().toString(),
                authorName.getText().toString(),
                repoUrl.getText().toString());
    }

    @OnClick(R.id.delete)
    void onDeleteButtonClick() {
        presenter.deleteRepo();
    }

    @Override
    public void displayError(SandboxError error) {
        loaderView.showNetworkError();
    }

    @Override
    public void displayData(Repo repo) {
        repoName.setText(repo.name);
        authorName.setText(repo.owner.name);
        repoUrl.setText(repo.url);
    }

    @Override
    public void displayLoader(boolean show) {
        repoContainer.setVisibility(show ? View.GONE : View.VISIBLE);
        loaderView.showLoading(show);
    }

    @Override
    public void goBack() {
        getFragmentManager().popBackStackImmediate();
    }

    @Override
    public long getRepoId() {
        return getArguments().getLong(EXTRA_REPO_ID);
    }

    @Override
    public void inject(@NonNull Object component) {
        ((ReposComponent) component).inject(this);
    }

}
