package io.reist.visum.presenter;

import io.reist.visum.view.VisumView;
import rx.Observable;
import rx.Observer;
import rx.Single;

/**
 * A notifier which updates multiple views on various events.
 *
 * Passes items emitted by {@link rx.Observable}s and {@link rx.Single}s to every view attached
 * to a presenter. Implement this interface in your method and pass the implementation to
 * subscription methods in {@link VisumPresenter}. For every view attached to the presenter, a
 * corresponding ViewNotifier method will be called.
 *
 * @param <V>   a type of receiving views
 * @param <T>   a type of emitted items
 *
 * @see VisumPresenter#subscribe(Observable, ViewNotifier)
 * @see VisumPresenter#subscribe(Single, ViewNotifier)
 *
 * Created by Reist on 25.05.16.
 */
public interface ViewNotifier<V extends VisumView, T> {

    /**
     * Updates the given view on completion.
     * Called on {@link Observer#onCompleted()} for every attached view.
     */
    void notifyCompleted(V v);

    /**
     * Updates the given view when an item is emitted.
     * Called on {@link Observer#onNext(Object)} for every attached view.
     */
    void notifyResult(V view, T t);

    /**
     * Updates the given view on error.
     * Called on {@link Observer#onError(Throwable)} for every attached view.
     */
    void notifyError(V view, Throwable e);

}
