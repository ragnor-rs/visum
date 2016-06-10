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
import io.reist.visum.presenter.VisumPresenter;

@Singleton
public class RepoListPresenter extends VisumPresenter<RepoListView> {

    private static final String TAG = RepoListPresenter.class.getName();

    private final RepoService repoService;
    private boolean mIsDataLoaded = false;

    @Inject
    public RepoListPresenter(RepoService repoService) {
        this.repoService = repoService;
    }

    @Override
    protected void onViewAttached() {
        mIsDataLoaded = false;
        RepoListView view = view();
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
                view.displayData(result); //cur need to check if view detached or crash can occure
            }
        });
    }

    public void createRepo() {
        RepoListView view = view();
        view.showLoader(true);
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