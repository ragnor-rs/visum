package io.reist.sandbox.feed;

import javax.inject.Singleton;

import dagger.Subcomponent;
import io.reist.sandbox.feed.view.FeedDetailFragment;
import io.reist.sandbox.feed.view.FeedListFragment;

@Singleton
@Subcomponent
public interface FeedComponent {
    void inject(FeedListFragment feedListFragment);

    void inject(FeedDetailFragment feedDetailFragment);
}
