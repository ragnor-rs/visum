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

import android.os.Build;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.multidex.ShadowMultiDex;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import io.reist.sandbox.BuildConfig;
import io.reist.sandbox.app.SandboxModule;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.app.model.remote.SandboxApi;
import io.reist.sandbox.core.RobolectricTestCase;
import io.reist.sandbox.users.UsersModule;
import io.reist.sandbox.users.model.UserService;
import io.reist.sandbox.users.view.UserListView;
import rx.Observable;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Created by m039 on 11/25/15.
 */
@RunWith(org.robolectric.RobolectricTestRunner.class)
@Config(shadows = ShadowMultiDex.class)
public class UserListPresenterTest extends RobolectricTestCase {

    @Inject
    UserListPresenter mUserListPresenter;

    @Before
    public void initComponents() {
        DaggerUserListPresenterTest_TestComponent
                .builder()
                .usersModule(new TestUsersModule())
                .sandboxModule(new SandboxModule(RuntimeEnvironment.application))
                .build()
                .inject(this);
    }

    @Singleton
    @Component(modules = SandboxModule.class)
    public interface TestComponent {
        void inject(UserListPresenterTest userListPresenterTest);
    }

    public static class TestUsersModule extends UsersModule {

        @Override
        protected UserService userService(SandboxApi sandboxApi, StorIOSQLite storIOSQLite) {
            UserService mockedUserService = mock(UserService.class);

            List<User> users = new ArrayList<>();

            User user1 = new User();
            user1.id = 1L;
            user1.name = "Alfred";
            users.add(user1);

            User user2 = new User();
            user2.id = 2L;
            user2.name = "Frank";
            users.add(user2);

            doReturn(Observable.just(new SandboxResponse<>(users))).when(mockedUserService).list();

            return mockedUserService;
        }

    }

    @Test
    public void testPresenter() throws InterruptedException {
        assertNotNull(mUserListPresenter);

        mUserListPresenter.setView(mock(UserListView.class));

        Thread.sleep(1000);

        assertTrue(mUserListPresenter.isDataLoaded());
    }

}
