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

import android.support.annotation.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.presenter.ResponseObserver;
import io.reist.sandbox.feed.model.FeedService;
import io.reist.sandbox.feed.view.FeedDetailView;
import io.reist.visum.presenter.SingleViewPresenter;

/**
 * Created by defuera on 10/11/2015.
 */
@Singleton
public class FeedDetailPresenter extends SingleViewPresenter<FeedDetailView> {

    private final FeedService feedService;

    @Inject
    public FeedDetailPresenter(FeedService feedService) {
        this.feedService = feedService;
    }

    @Override
    protected void onViewAttached(@NonNull FeedDetailView view) {
        long postId = view.getPostId();
        view.displayLoader(true);
        subscribe(feedService.byId(postId), new FeedPostObserver());

    }

    private class FeedPostObserver extends ResponseObserver<Post> {

        @Override
        protected void onFail(SandboxError error) {
            withView(
                    view -> {
                        view.displayLoader(false);
                        view.displayError(error);
                    }
            );
        }

        @Override
        protected void onSuccess(Post result) {
            withView(
                    view -> {
                        view.displayLoader(false);
                        view.displayData(result);
                    }
            );
        }
    }

}