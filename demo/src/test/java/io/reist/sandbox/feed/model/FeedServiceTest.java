package io.reist.sandbox.feed.model;

import android.os.Build;

import com.pushtorefresh.storio.sqlite.StorIOSQLite;

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
import io.reist.sandbox.core.RobolectricTestCase;
import io.reist.sandbox.core.RobolectricTestRunner;
import io.reist.sandbox.feed.FeedModule;
import io.reist.sandbox.feed.model.local.Post;
import io.reist.sandbox.feed.model.local.StorIoPostService;
import io.reist.sandbox.feed.model.remote.FeedServerApi;
import io.reist.sandbox.feed.model.remote.RetrofitFeedService;
import rx.Observable;
import rx.observers.TestSubscriber;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Created by 4xes on 9/11/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class FeedServiceTest extends RobolectricTestCase {

    @Inject
    FeedService feedService;

    TestComponent testComponent;

    @Before
    public void initComponents() {
        SandboxApplication sandboxApplication = (SandboxApplication) RuntimeEnvironment.application;
        testComponent = DaggerFeedServiceTest_TestComponent
                .builder()
                .sandboxModule(new SandboxModule(sandboxApplication))
                .feedModule(new TestFeedModule())
                .build();

        testComponent.inject(this);
        assertThat(feedService).isNotNull();
    }

    @Singleton
    @Component(modules = SandboxModule.class)
    public interface TestComponent {
        void inject(FeedServiceTest userServiceTest);
    }

    public static class TestFeedModule extends FeedModule {

        @Override
        protected FeedService feedService(FeedServerApi ignored, StorIOSQLite storIOSQLite) {
            RetrofitFeedService retrofitFeedService = spy(new RetrofitFeedService(S_MOCKED_MVP_FEED_API));

            List<Post> posts = new ArrayList<>();

            Post post = new Post();
            post.id = POST_ID;

            posts.add(post);

            doReturn(Observable.just(posts))
                    .when(S_MOCKED_MVP_FEED_API)
                    .posts();

            doReturn(Observable.just(post))
                    .when(S_MOCKED_MVP_FEED_API)
                    .post(POST_ID);

            return new CachedFeedService(new StorIoPostService(storIOSQLite), retrofitFeedService);
        }

    }

    private static final FeedServerApi S_MOCKED_MVP_FEED_API = mock(FeedServerApi.class);
    public static final long POST_ID = (new Random().nextLong());

    @Test
    public void testPostById() {
        TestSubscriber<SandboxResponse<Post>> testSubscriber = new TestSubscriber<>();
        feedService.byId(POST_ID).subscribe(testSubscriber);

        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(500, TimeUnit.MILLISECONDS);

        assertThat(testSubscriber.getOnErrorEvents().isEmpty())
                .isTrue();
        assertThat(testSubscriber.getOnNextEvents().get(0).getResult().id).isEqualTo(POST_ID);
    }

    @Test
    public void testPosts() {
        TestSubscriber<SandboxResponse<List<Post>>> testSubscriber = new TestSubscriber<>();
        feedService.list().subscribe(testSubscriber);

        testSubscriber.awaitTerminalEventAndUnsubscribeOnTimeout(500, TimeUnit.MILLISECONDS);

        assertThat(testSubscriber.getOnErrorEvents().isEmpty())
                .isTrue();
        assertThat(testSubscriber.getOnNextEvents().get(0).getResult()).isNotEmpty();
    }


}
