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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.users.model.UserService;
import io.reist.sandbox.users.view.UserListView;
import io.reist.visum.presenter.SingleViewPresenter;
import rx.Observer;

/**
 * Created by m039 on 11/12/15.
 */
@Singleton
public class UserListPresenter extends SingleViewPresenter<UserListView> {

    UserService mUserService;
    private boolean mIsDataLoaded = false;

    @Inject
    UserListPresenter(UserService userService) {
        mUserService = userService;
    }

    @Override
    protected void onViewAttached(@NonNull UserListView view) {
        mIsDataLoaded = false;
        view.showLoader(true);
        loadData();
    }

    public void loadData() {
        subscribe(mUserService.list(), new UsersObserver());
    }

    /**
     * Used in test only
     */
    public boolean isDataLoaded() {
        return mIsDataLoaded;
    }

    private class UsersObserver implements Observer<SandboxResponse<List<User>>> {

        @Override
        public void onNext(SandboxResponse<List<User>> response) {
            UserListView view = view();
            if (response.isSuccessful()) {
                mIsDataLoaded = true;
                view.displayData(response.getResult());
                view.showLoader(false);
            } else {
                view.displayError(response.getError());
            }

        }

        @Override
        public void onCompleted() {}

        @Override
        public void onError(Throwable e) {
            UserListView view = view();
            view.showLoader(false);
        }

    }
}
