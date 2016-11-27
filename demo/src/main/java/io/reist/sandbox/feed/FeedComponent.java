package io.reist.sandbox.feed;

import dagger.Subcomponent;
import io.reist.sandbox.feed.view.FeedDetailFragment;
import io.reist.sandbox.feed.view.FeedListFragment;

@Subcomponent
public interface FeedComponent {
    void inject(FeedListFragment feedListFragment);

    void inject(FeedDetailFragment feedDetailFragment);
}
