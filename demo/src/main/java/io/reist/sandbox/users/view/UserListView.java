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

package io.reist.sandbox.users.view;

import java.util.List;

import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.users.presenter.UserListPresenter;
import io.reist.visum.view.VisumView;

/**
 * Created by m039 on 11/12/15.
 */
public interface UserListView extends VisumView<UserListPresenter> {

    void showLoader(boolean show);

    void displayError(SandboxError error);

    void displayData(List<User> users);

}
