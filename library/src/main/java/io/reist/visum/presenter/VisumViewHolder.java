package io.reist.visum.presenter;

import rx.subscriptions.CompositeSubscription;

class VisumViewHolder<V> {

    V view;
    CompositeSubscription subscriptions;

    VisumViewHolder(V view) {
        this.view = view;
        this.subscriptions = new CompositeSubscription();
    }
}