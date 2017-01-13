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

import javax.inject.Singleton;

import dagger.Component;
import io.reist.sandbox.feed.FeedComponent;
import io.reist.sandbox.repos.ReposComponent;
import io.reist.sandbox.result.ResultComponent;
import io.reist.sandbox.time.TimeComponent;
import io.reist.sandbox.users.UsersComponent;
import io.reist.sandbox.weather.WeatherComponent;

@Singleton
@Component(modules = SandboxModule.class)
public interface SandboxComponent {

    ReposComponent reposComponent();

    UsersComponent usersComponent();

    TimeComponent timeComponent();

    ResultComponent resultComponent();

    WeatherComponent weatherComponent();

    FeedComponent feedComponent();

}
