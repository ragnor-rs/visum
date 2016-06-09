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

package io.reist.sandbox.users.presenter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.Repo;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.repos.model.RepoService;
import io.reist.sandbox.users.view.UserReposView;
import io.reist.visum.presenter.VisumPresenter;
import rx.Observer;

@Singleton
public class UserReposPresenter extends VisumPresenter<UserReposView> {

    private static final String TAG = UserReposPresenter.class.getName();

    private final RepoService repoService;

    @Inject
    public UserReposPresenter(RepoService repoService) {
        this.repoService = repoService;
    }

    @Override
    protected void onViewAttached() {
        UserReposView view = view();
        view.showLoader(true);
        loadData(view.getUserId());
    }

    public void like(Repo repo) {
        subscribe(repoService.like(repo), new LikeObserver(true));
    }

    public void unlike(Repo repo) {
        subscribe(repoService.unlike(repo), new LikeObserver(false));
    }

    public void loadData(Long userId) {
        subscribe(repoService.findReposByUserId(userId), new RepoListObserver());
    }

    private class LikeObserver implements Observer<SandboxResponse<Repo>> {

        final boolean like;

        LikeObserver(boolean like) {
            this.like = like;
        }

        @Override
        public void onNext(SandboxResponse<Repo> repo) {}

        @Override
        public void onCompleted() {}

        @Override
        public void onError(Throwable e) {
            UserReposView view = view();
            view.displayError(new SandboxError(e));
        }

    }

    private class RepoListObserver implements Observer<SandboxResponse<List<Repo>>> {

        @Override
        public void onNext(SandboxResponse<List<Repo>> response) {
            Log.i(TAG, "--- OBSERVED ON " + Thread.currentThread() + " ---");
            UserReposView view = view();
            if (response.isSuccessful()) {
                List<Repo> result = response.getResult();
                if (result == null) {
                    result = new ArrayList<>();
                }
                Log.d(TAG, "successfully loaded " + result.size() + " items");
                view.displayData(result);
                view.showLoader(false);
            } else {
                Log.w(TAG, "network error occurred");
                view.displayError(response.getError());
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "Error fetching data", e);
            UserReposView view = view();
            view.displayError(new SandboxError(e));
            view.showLoader(false);
        }

        @Override
        public void onCompleted() {}

    }

}
