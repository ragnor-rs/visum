package io.reist.visum.presenter;

/**
 * Created by Reist on 25.05.16.
 */
public interface ViewNotifier<V, T> {

    void notifyCompleted(V v);

    void notifyResult(V view, T t);

    void notifyError(V view, Throwable e);

}
