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

package io.reist.visum;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import org.robolectric.RuntimeEnvironment;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * A test for components (IoC-containers) of the given type. Unlike {@link VisumTest}, this test
 * supports assertions for multiple clients operating simultaneously in a single component cache.
 *
 * On start-up, a new component is created via {@link #createComponent()}. Subclasses should
 * register component clients by calling {@link #register(Class[])} in the setUp method. A local
 * {@link io.reist.visum.ComponentCache.Listener} is registered and available via
 * {@link #onComponentCreated(Object)} and {@link #onComponentDestroyed(Object)}.
 *
 * Subclasses must call {@link #tearDown()} in tearDown methods to free resources.
 *
 * @see #getComponent()
 * @see #getComponentCache()
 *
 * @param <C>   root component class
 */
public abstract class VisumImplTest<C> implements ComponentCache.Listener {

    private C component;

    private final Func0<Object> componentFactory = new Func0<Object>() {

        @Override
        public C call() {
            return component;
        }

    };

    protected abstract C createComponent();

    @NonNull
    protected ComponentCache getComponentCache() {
        return ((TestApplication) RuntimeEnvironment.application).getComponentCache();
    }

    @SafeVarargs
    protected final void register(Class<? extends VisumClient>... clientClasses) {
        getComponentCache().register(componentFactory, clientClasses);
    }

    public C getComponent() {
        return component;
    }

    @CallSuper
    public void setUp() {
        getComponentCache().setListener(this);
        this.component = createComponent();
    }

    @CallSuper
    public void tearDown() {
        getComponentCache().setListener(null);
    }

    @Override
    public void onComponentDestroyed(Object component) {}

    @Override
    public void onComponentCreated(Object component) {}

    static {
        RxAndroidPlugins.getInstance().registerSchedulersHook(new RxAndroidSchedulersHook() {

            @Override
            public Scheduler getMainThreadScheduler() {
                return Schedulers.immediate();
            }

        });
    }

}
