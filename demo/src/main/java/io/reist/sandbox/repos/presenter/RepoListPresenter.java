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
import android.util.Log;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.app.presenter.ResponseObserver;
import io.reist.sandbox.repos.model.RepoService;
import io.reist.sandbox.repos.view.RepoListView;
import io.reist.visum.presenter.SingleViewPresenter;

@Singleton
public class RepoListPresenter extends SingleViewPresenter<RepoListView> {

    private static final String TAG = RepoListPresenter.class.getName();

    private final RepoService repoService;
    private boolean mIsDataLoaded = false;

    @Inject
    public RepoListPresenter(RepoService repoService) {
        this.repoService = repoService;
    }

    @Override
    protected void onViewAttached(@NonNull RepoListView view) {
        mIsDataLoaded = false;
        view.showLoader(true);
        loadData();
    }

    public boolean isDataLoaded() {
        return mIsDataLoaded;
    }

    public void loadData() {
        subscribe(repoService.list(), new ResponseObserver<List<Repo>>() {

            @Override
            protected void onFail(SandboxError error) {
                RepoListView view = view();
                view.showLoader(false);
                view.displayError(error);
            }

            @Override
            protected void onSuccess(List<Repo> result) {
                mIsDataLoaded = true;
                RepoListView view = view();
                view.showLoader(false);
                view.displayData(result); // need to check if view detached or crash can occur
            }

        });
    }

    public void createRepo() {
        view().showLoader(true);

        Random rand = new Random();
        Repo object = new Repo();

        object.id = (long) rand.nextInt(1000);
        object.name = "name_" + object.id;
        object.url = "url";

        User owner = new User();
        owner.id = (long) rand.nextInt(1000);
        owner.login = owner.name = "Vasy_" + owner.id;
        object.owner = owner;

        subscribe(repoService.save(object), new AddRepoSubscriber());
    }

    private class AddRepoSubscriber extends ResponseObserver<Repo> {

        @Override
        protected void onFail(SandboxError error) {
            Log.e(TAG, "Error saving data" + error.getMessage());
            RepoListView view = view();
            view.displayError(error);
            view.showLoader(false);
        }

        @Override
        protected void onSuccess(Repo result) {
            Log.i(TAG, "success add repo subscriber");
            RepoListView view = view();
            view.displaySuccess();
            view.showLoader(false);
        }

    }

}