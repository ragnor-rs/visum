package io.reist.sandbox.feed.presenter;

import android.os.Build;

import org.junit.After;
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
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.core.RobolectricTestCase;
import io.reist.sandbox.core.RobolectricTestRunner;
import io.reist.sandbox.feed.model.FeedServiceTest;
import io.reist.sandbox.feed.model.local.Post;
import io.reist.sandbox.feed.view.FeedListView;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by 4xes on 9/11/16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.M)
public class FeedListPresenterTest extends RobolectricTestCase {

    @Inject
    FeedListPresenter feedListPresenter;

    @Before
    @Override
    public void setup() {
        super.setup();
        DaggerFeedListPresenterTest_TestComponent
                .builder()
                .feedModule(new FeedServiceTest.TestFeedModule())
                .sandboxModule(new SandboxModule(RuntimeEnvironment.application))
                .build()
                .inject(this);
    }

    @After
    @Override
    public void end() {
        super.end();
    }

    @Singleton
    @Component(modules = SandboxModule.class)
    public interface TestComponent {
        void inject(FeedListPresenterTest feedListPresenterTest);
    }

    @Test
    public void testPresenter() throws InterruptedException {
        assertThat(feedListPresenter).isNotNull();

        FeedListView feedListView = mock(FeedListView.class);
        feedListPresenter.setView(feedListView);

        verify(feedListView, times(1)).showLoader(true);
        verify(feedListView, never()).displayError(new SandboxError(new Throwable()));
        verify(feedListView, times(1)).displayData(anyListOf(Post.class));
    }

}
