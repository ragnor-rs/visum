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

package io.reist.sandbox.repos.presenter;

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.presenter.ResponseObserver;
import io.reist.sandbox.repos.model.RepoService;
import io.reist.sandbox.repos.view.RepoEditView;
import io.reist.visum.presenter.SingleViewPresenter;
import rx.Subscriber;

/**
 * Created by defuera on 10/11/2015.
 */
@Singleton
public class RepoEditPresenter extends SingleViewPresenter<RepoEditView> {

    private RepoService repoService;
    private boolean mIsDataLoaded;
    private Repo repo;

    @Inject
    public RepoEditPresenter(RepoService repoService) {
        this.repoService = repoService;
    }

    @Override
    protected void onViewAttached(@NonNull RepoEditView view) {
        mIsDataLoaded = false;
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

        view().displayLoader(true);

        repo.name = name;
        repo.owner.name = author;
        repo.url = url;

        subscribe(repoService.save(repo), new ResponseObserver<Repo>() {

            @Override
            protected void onFail(SandboxError error) {
                view().displayLoader(false);
                view().displayError(error);
            }

            @Override
            protected void onSuccess(Repo result) {
                view().displayLoader(false);
                view().goBack();
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
                view().goBack();
            }

        });
    }

}