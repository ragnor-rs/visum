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

package io.reist.sandbox.feed.presenter;

import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.multidex.ShadowMultiDex;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Component;
import io.reist.sandbox.BuildConfig;
import io.reist.sandbox.app.SandboxModule;
import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.core.RobolectricTestCase;
import io.reist.sandbox.feed.model.FeedServiceTest;
import io.reist.sandbox.feed.view.FeedDetailView;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by 4xes on 9/11/16.
 */
@RunWith(org.robolectric.RobolectricTestRunner.class)
@Config(shadows = ShadowMultiDex.class)
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
    public void testPresenter() {
        assertNotNull(feedDetailPresenter);

        FeedDetailView feedDetailView = mock(FeedDetailView.class);

        doReturn(FeedServiceTest.POST_ID).when(feedDetailView).getPostId();
        feedDetailPresenter.setView(feedDetailView);

        verify(feedDetailView, times(1)).displayLoader(true);
        verify(feedDetailView, never()).displayError(new SandboxError(new Throwable()));
        verify(feedDetailView, times(1)).displayData(any(Post.class));
    }

}
