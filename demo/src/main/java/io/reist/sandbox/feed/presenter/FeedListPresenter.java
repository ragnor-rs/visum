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

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.app.model.SandboxResponse;
import io.reist.sandbox.feed.model.FeedService;
import io.reist.sandbox.feed.view.FeedListView;
import io.reist.visum.presenter.SingleViewPresenter;
import rx.Observer;

/**
 * Created by 4xes on 7/11/16.
 */
@Singleton
public class FeedListPresenter extends SingleViewPresenter<FeedListView> {

    private final FeedService feedService;

    @Inject
    public FeedListPresenter(FeedService feedService) {
        this.feedService = feedService;
    }

    @Override
    protected void onViewAttached(@NonNull FeedListView view) {
        view.showLoader(true);
        loadData();
    }

    public void loadData() {
        subscribe(feedService.list(), new FeedListObserver());
    }

    private class FeedListObserver implements Observer<SandboxResponse<List<Post>>> {

        @Override
        public void onNext(SandboxResponse<List<Post>> response) {
            if (response.isSuccessful()) {
                List<Post> result = response.getResult();
                withView(
                        view -> {
                            view.displayData(result == null ? new ArrayList<>() : result);
                            view.showLoader(false);
                        }
                );
            } else {
                withView(view -> view.displayError(response.getError()));
            }
        }

        @Override
        public void onError(Throwable e) {
            withView(
                    view -> {
                        view.displayError(new SandboxError(e));
                        view.showLoader(false);
                    }
            );
        }

        @Override
        public void onCompleted() {}

    }

}