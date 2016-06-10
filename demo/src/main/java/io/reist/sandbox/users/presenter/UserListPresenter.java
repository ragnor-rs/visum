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

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.users.model.UserService;
import io.reist.sandbox.users.view.UserListView;
import io.reist.visum.presenter.VisumPresenter;
import rx.Observer;

/**
 * Created by m039 on 11/12/15.
 */
@Singleton
public class UserListPresenter extends VisumPresenter<UserListView> {

    UserService mUserService;
    private boolean mIsDataLoaded = false;

    @Inject
    UserListPresenter(UserService userService) {
        mUserService = userService;
    }

    @Override
    protected void onViewAttached() {
        mIsDataLoaded = false;

        UserListView view = view();
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
