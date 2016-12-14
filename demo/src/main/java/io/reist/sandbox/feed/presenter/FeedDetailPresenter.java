/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of MVP-Sandbox.
 *
 * MVP-Sandbox is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MVP-Sandbox is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MVP-Sandbox.  If not, see <http://www.gnu.org/licenses/>.
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
import io.reist.visum.presenter.VisumViewPresenter;

/**
 * Created by defuera on 10/11/2015.
 */
@Singleton
public class FeedDetailPresenter extends VisumViewPresenter<FeedDetailView> {

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
            FeedDetailView view = view();
            view.displayLoader(false);
            view.displayError(error);
        }

        @Override
        protected void onSuccess(Post result) {
            FeedDetailView view = view();
            view.displayLoader(false);
            view.displayData(result);
        }
    }

}