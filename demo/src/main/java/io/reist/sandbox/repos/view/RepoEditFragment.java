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

package io.reist.sandbox.repos.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.Bind;
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

    @Bind(R.id.repo_name)
    TextView repoName;

    @Bind(R.id.author_name)
    TextView authorName;

    @Bind(R.id.repo_url)
    TextView repoUrl;

    @Bind(R.id.loader)
    LoaderView loaderView;

    @Bind(R.id.repo_container)
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
    public void inject(@NonNull Object from) {
        ((ReposComponent) from).inject(this);
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
    public void back() {
        getFragmentManager().popBackStackImmediate();
    }

    @Override
    public long getRepoId() {
        return getArguments().getLong(EXTRA_REPO_ID);
    }

    @Override
    public void displayEditSuccess() {
        Toast.makeText(getActivity(), R.string.repo_saved, Toast.LENGTH_SHORT).show();
    }

}
