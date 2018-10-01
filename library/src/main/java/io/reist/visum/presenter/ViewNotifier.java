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

package io.reist.visum.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    void notifyCompleted(@NonNull V view);

    /**
     * Updates the given view when an item is emitted.
     * Called on {@link Observer#onNext(Object)} for every attached view.
     */
    void notifyResult(@NonNull V view, @Nullable T t);

    /**
     * Updates the given view on error.
     * Called on {@link Observer#onError(Throwable)} for every attached view.
     */
    void notifyError(@NonNull V view, @NonNull Throwable e);

}
