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

package io.reist.sandbox.repos.presenter;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.presenter.ResponseObserver;
import io.reist.sandbox.repos.model.RepoService;
import io.reist.sandbox.repos.view.RepoEditView;
import io.reist.visum.presenter.VisumPresenter;
import rx.Subscriber;

/**
 * Created by defuera on 10/11/2015.
 */
@Singleton
public class RepoEditPresenter extends VisumPresenter<RepoEditView> {

    private RepoService repoService;
    private boolean mIsDataLoaded;
    private Repo repo;

    @Inject
    public RepoEditPresenter(RepoService repoService) {
        this.repoService = repoService;
    }

    @Override
    protected void onViewAttached() {
        mIsDataLoaded = false;

        RepoEditView view = view();
        long repoId = view.getRepoId();
        view.displayLoader(true);
        subscribe(repoService.byId(repoId), new ResponseObserver<Repo>() {

            @Override
            protected void onFail(SandboxError error) {
                RepoEditView view = view();
                view.displayLoader(false);
                view.displayError(error);
            }

            @Override
            protected void onSuccess(Repo result) {
                RepoEditView view = view();
                mIsDataLoaded = true;
                view.displayLoader(false);
                repo = result;
                view.displayData(result);
            }

        });
    }

    public boolean isDataLoaded() {
        return mIsDataLoaded;
    }

    public void saveRepo(String name, String author, String url) {
        repo.name = name;
        repo.owner.name = author;
        repo.url = url;

        subscribe(repoService.save(repo), new ResponseObserver<Repo>() {

            @Override
            protected void onFail(SandboxError error) {
                RepoEditView view = view();
                view.displayError(error);
            }

            @Override
            protected void onSuccess(Repo result) {
                RepoEditView view = view();
                view.displayEditSuccess();
            }

        });
    }

    public void deleteRepo() {
        subscribe(repoService.delete(repo.id), new Subscriber<SandboxResponse<Integer>>() {

            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(SandboxResponse<Integer> response) {
                RepoEditView view = view();
                view.back();
            }

        });
    }

}