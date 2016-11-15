package io.reist.sandbox.feed.presenter;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import io.reist.sandbox.BuildConfig;
import io.reist.sandbox.app.SandboxModule;
import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.core.RobolectricTestCase;
import io.reist.sandbox.core.RobolectricTestRunner;
import io.reist.sandbox.feed.model.FeedServiceTest;
import io.reist.sandbox.feed.view.FeedDetailView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by 4xes on 9/11/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class FeedDetailPresenterTest extends RobolectricTestCase {

    @Inject
    FeedDetailPresenter feedDetailPresenter;

    @Before
    public void initComponents() {
        DaggerFeedDetailPresenterTest_TestComponent
                .builder()
                .feedModule(new FeedServiceTest.TestFeedModule())
                .sandboxModule(new SandboxModule(RuntimeEnvironment.application))
                .build()
                .inject(this);
    }

    @Singleton
    @Component(modules = SandboxModule.class)
    public interface TestComponent {
        void inject(FeedDetailPresenterTest feedDetailPresenterTest);
    }

    @Test
    public void testPresenter() throws InterruptedException {
        assertThat(feedDetailPresenter).isNotNull();

        FeedDetailView feedDetailView = mock(FeedDetailView.class);

        doReturn(FeedServiceTest.POST_ID).when(feedDetailView).getPostId();
        feedDetailPresenter.setView(feedDetailView);

        verify(feedDetailView, times(1)).displayLoader(true);
        verify(feedDetailView, never()).displayError(new SandboxError(new Throwable()));
        verify(feedDetailView, times(1)).displayData(any(Post.class));
    }

}
