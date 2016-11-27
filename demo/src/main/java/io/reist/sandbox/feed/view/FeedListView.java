package io.reist.sandbox.feed.view;

import java.util.List;

import io.reist.sandbox.app.model.Post;
import io.reist.sandbox.app.model.SandboxError;
import io.reist.sandbox.feed.presenter.FeedListPresenter;
import io.reist.visum.view.VisumView;

/**
 * Created by defuera on 05/11/2015.
 */
public interface FeedListView extends VisumView<FeedListPresenter> {

    void showLoader(boolean show);

    void displayError(SandboxError error);

    void displayData(List<Post> data);

}