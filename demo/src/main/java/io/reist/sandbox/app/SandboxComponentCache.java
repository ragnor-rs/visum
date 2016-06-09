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

package io.reist.sandbox.app;

import android.content.Context;

import io.reist.sandbox.repos.view.RepoEditFragment;
import io.reist.sandbox.repos.view.RepoListFragment;
import io.reist.sandbox.result.view.ResultActivity;
import io.reist.sandbox.time.view.TimeFragment;
import io.reist.sandbox.time.view.TimeNotification;
import io.reist.sandbox.users.view.UserListFragment;
import io.reist.sandbox.users.view.UserReposFragment;
import io.reist.visum.ComponentCache;

/**
 * Created by Reist on 29.11.15.
 */
public class SandboxComponentCache extends ComponentCache {

    public SandboxComponentCache(Context context) {
        this(DaggerSandboxComponent.builder().sandboxModule(new SandboxModule(context)).build());
    }

    public SandboxComponentCache(SandboxComponent sandboxComponent) {

        register(
                sandboxComponent::reposComponent,
                RepoListFragment.class, RepoEditFragment.class
        );

        register(
                sandboxComponent::usersComponent,
                UserListFragment.class, UserReposFragment.class
        );

        register(
                sandboxComponent::timeComponent,
                TimeFragment.class, TimeNotification.class
        );

        register(
                sandboxComponent::resultComponent,
                ResultActivity.class
        );

    }

}
