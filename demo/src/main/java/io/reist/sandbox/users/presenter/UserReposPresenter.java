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

package io.reist.sandbox.users.presenter;

import android.support.annotation.NonNull;
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
import io.reist.visum.presenter.SingleViewPresenter;
import rx.Observer;

@Singleton
public class UserReposPresenter extends SingleViewPresenter<UserReposView> {

    private static final String TAG = UserReposPresenter.class.getName();

    private final RepoService repoService;

    @Inject
    public UserReposPresenter(RepoService repoService) {
        this.repoService = repoService;
    }

    @Override
    protected void onViewAttached(@NonNull UserReposView view) {
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
            withView(v -> v.displayError(new SandboxError(e)));
        }

    }

    private class RepoListObserver implements Observer<SandboxResponse<List<Repo>>> {

        @Override
        public void onNext(SandboxResponse<List<Repo>> response) {
            Log.i(TAG, "--- OBSERVED ON " + Thread.currentThread() + " ---");
            if (response.isSuccessful()) {
                List<Repo> result = response.getResult();
                Log.d(TAG, "successfully loaded " + result.size() + " items");
                withView(
                        v -> {
                            v.displayData(result == null ? new ArrayList<>() : result);
                            v.showLoader(false);
                        }
                );
            } else {
                Log.w(TAG, "network error occurred");
                withView(v -> v.displayError(response.getError()));
            }
        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "Error fetching data", e);
            withView(
                    view -> {
                        view.displayError(new SandboxError(e));
                        view.showLoader(false);
                    }
            );
        }

        @Override
        public void onCompleted() {}

    }

}
