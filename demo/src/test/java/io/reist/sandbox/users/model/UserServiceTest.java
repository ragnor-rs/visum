package io.reist.sandbox.users.model;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import io.reist.sandbox.BuildConfig;
import io.reist.sandbox.app.SandboxApplication;
import io.reist.sandbox.app.SandboxModule;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.app.model.User;
import io.reist.sandbox.app.model.remote.SandboxApi;
import io.reist.sandbox.core.RobolectricTestCase;
import io.reist.sandbox.core.RobolectricTestRunner;
import io.reist.sandbox.users.UsersModule;
import io.reist.sandbox.users.model.local.StorIoUserService;
import io.reist.sandbox.users.model.remote.RetrofitUserService;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Created by m039 on 11/27/15.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class UserServiceTest extends RobolectricTestCase {

    @Inject
    UserService userService;

    TestComponent testComponent;

    @Before
    @Override
    public void setUp() {
        super.setUp();
        SandboxApplication sandboxApplication = (SandboxApplication) RuntimeEnvironment.application;

        testComponent = DaggerUserServiceTest_TestComponent
                .builder()
                .sandboxModule(new SandboxModule(sandboxApplication))
                .usersModule(new TestUsersModule())
                .build();

        testComponent.inject(this);
        assertThat(userService).isNotNull();
    }

    @After
    @Override
    public void tearDown() {
        super.tearDown();
    }

    @Singleton
    @Component(modules = SandboxModule.class)
    public interface TestComponent {

        void inject(UserServiceTest userServiceTest);

    }

    public static class TestUsersModule extends UsersModule {

        @Override
        protected UserService userService(SandboxApi ignored, StorIOSQLite storIOSQLite) {
            RetrofitUserService retrofitUserService = spy(new RetrofitUserService(S_MOCKED_MVP_SANDBOX_API));

            doReturn(Observable.empty()).when(retrofitUserService).byId(any());

            return new CachedUserService(new StorIoUserService(storIOSQLite), retrofitUserService);
        }

    }

    private static final SandboxApi S_MOCKED_MVP_SANDBOX_API = mock(SandboxApi.class);
    private static final long USER_ID = -(new Random().nextLong());

    @Test
    public void testUserService() {
        firstTestCase();
        testIfUsersExist();
        testIfUserWithUserIdExist();

        secondTestCase();
        testIfUserWithUserIdExist();
    }

    void testIfUsersExist() {
        TestSubscriber<SandboxResponse<List<User>>> testSubscriber = new TestSubscriber<>();
        userService.list().subscribe(testSubscriber);

        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(500, TimeUnit.MILLISECONDS);

        assertThat(testSubscriber.getOnNextEvents().get(0).getResult().isEmpty())
                .isFalse();
    }

    void testIfUserWithUserIdExist() {
        TestSubscriber<SandboxResponse<User>> testSubscriber = new TestSubscriber<>();
        userService.byId(USER_ID).subscribe(testSubscriber);

        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(500, TimeUnit.MILLISECONDS);

        assertThat(testSubscriber.getOnNextEvents().get(0).getResult().id)
                .isEqualTo(USER_ID);
    }

    void firstTestCase() {
        List<User> users = new ArrayList<>();

        User user = new User();
        user.id = USER_ID;

        users.add(user);

        doReturn(Observable.just(new SandboxResponse<>(users)))
                .when(S_MOCKED_MVP_SANDBOX_API)
                .listUsers();
    }

    void secondTestCase() {
        when(S_MOCKED_MVP_SANDBOX_API.listUsers())
                .thenReturn(Observable.empty());
    }

}
