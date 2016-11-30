package io.reist.sandbox.feed.presenter;

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
    protected void onViewAttached() {
        FeedListView view = view();
        view.showLoader(true);
        loadData();
    }

    public void loadData() {
        subscribe(feedService.list(), new FeedListObserver());
    }

    private class FeedListObserver implements Observer<SandboxResponse<List<Post>>> {

        @Override
        public void onNext(SandboxResponse<List<Post>> response) {
            FeedListView view = view();
            if (response.isSuccessful()) {
                List<Post> result = response.getResult();
                if (result == null) {
                    result = new ArrayList<>();
                }
                view.displayData(result);
                view.showLoader(false);
            } else {
                view.displayError(response.getError());
            }
        }

        @Override
        public void onError(Throwable e) {
            FeedListView view = view();
            view.displayError(new SandboxError(e));
            view.showLoader(false);
        }

        @Override
        public void onCompleted() {
        }

    }

}