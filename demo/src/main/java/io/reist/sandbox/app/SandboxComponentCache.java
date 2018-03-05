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

package io.reist.sandbox.app;

import android.content.Context;

import io.reist.sandbox.feed.view.FeedDetailFragment;
import io.reist.sandbox.feed.view.FeedListFragment;
import io.reist.sandbox.food.view.RestaurantFragment;
import io.reist.sandbox.food.view.RestaurantInfoFragment;
import io.reist.sandbox.food.view.RestaurantListFragment;
import io.reist.sandbox.food.view.RestaurantMapFragment;
import io.reist.sandbox.repos.view.RepoEditFragment;
import io.reist.sandbox.repos.view.RepoListFragment;
import io.reist.sandbox.result.view.ResultActivity;
import io.reist.sandbox.time.view.TimeFragment;
import io.reist.sandbox.time.view.TimeNotification;
import io.reist.sandbox.users.view.UserListFragment;
import io.reist.sandbox.users.view.UserReposFragment;
import io.reist.sandbox.weather.view.WeatherFragment;
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

        register(
                sandboxComponent::weatherComponent,
                WeatherFragment.class
        );

        register(
                sandboxComponent::feedComponent,
                FeedListFragment.class, FeedDetailFragment.class
        );
        register(
                sandboxComponent::restaurantComponent,
                RestaurantListFragment.class,
                RestaurantFragment.class,
                RestaurantMapFragment.class,
                RestaurantInfoFragment.class
        );

    }

}
