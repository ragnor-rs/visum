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

import android.content.Context;
import android.support.annotation.NonNull;

/**
 * Visum framework addresses these tasks:
 * - providing inversion of control
 * - construction of MVP architecture
 *
 * To achieve that, Visum provides two interfaces:
 * - {@link VisumClient} - implement to take advantage of dependency injection
 * - {@link io.reist.visum.view.VisumView} - an extension to VisumClient which represents MVP views
 *
 * Visum recommends the following structure of application:
 * Views ({@link android.app.Activity}, {@link android.app.Fragment} or {@link android.view.View})
 * have Presenters injected into them.
 *
 * A configuration change view causes view a loss of data which hasn't been explicitly saved.
 * But the attached {@link io.reist.visum.presenter.VisumPresenter} survives configuration change
 * thus pertaining all the data stored inside of it.
 *
 * The presenter re-attachment is carried out by the {@link ComponentCache}
 * (e.g. <a href="https://github.com/ragnor-rs/mvp-sandbox/blob/develop/app/src/main/java/io/reist/sandbox/app/SandboxComponentCache.java">SandboxComponentCache</a>)
 * provided by local {@link android.app.Application} class. The client code also must provide Visum
 * with a singleton component and a singleton presenter (e.g, with means of Dagger framework).
 *
 * Injection is handled automatically via reflection during {@link VisumClientHelper#onCreate}.
 * A corresponding component must be public and declare a public 'inject' method which accepts this
 * client as a parameter.
 *
 * @see VisumClientHelper#INJECT_METHOD_NAME
 *
 * Created by defuera on 02/02/2016.
 */
public interface VisumClient {

    /**
     * Implementation must return local component cache. E.g.:
     * "return ((ComponentCacheProvider) context.getApplicationContext()).getComponentCache();"
     *
     * @return  local implementation of the {@link ComponentCache} class
     */
    ComponentCache getComponentCache();

    /**
     * Called when the client is to be created.
     */
    void onStartClient();

    /**
     * Called when the client is to be completely destroyed. Re-creation of the view should
     * not trigger this method. Implementations should destroy the component used by this
     * view.
     */
    void onStopClient();

    /**
     * Returns Android app context
     */
    @NonNull
    Context getContext();

}
