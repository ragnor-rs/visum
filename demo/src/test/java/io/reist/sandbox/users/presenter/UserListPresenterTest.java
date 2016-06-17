package io.reist.sandbox.users.presenter;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

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
import io.reist.sandbox.core.RobolectricTestRunner;
import io.reist.sandbox.users.UsersModule;
import io.reist.sandbox.users.model.UserService;
import io.reist.sandbox.users.view.UserListView;
import rx.Observable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import android.os.Build;

/**
 * Created by m039 on 11/25/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class UserListPresenterTest extends RobolectricTestCase {

    @Inject
    UserListPresenter mUserListPresenter;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        DaggerUserListPresenterTest_TestComponent
                .builder()
                .usersModule(new TestUsersModule())
                .sandboxModule(new SandboxModule(RuntimeEnvironment.application))
                .build()
                .inject(this);
    }

    @After
    @Override
    public void tearDown() {
        super.tearDown();
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
        assertThat(mUserListPresenter).isNotNull();

        mUserListPresenter.setView(mock(UserListView.class));

        Thread.sleep(1000);

        assertThat(mUserListPresenter.isDataLoaded()).isTrue();
    }

}
