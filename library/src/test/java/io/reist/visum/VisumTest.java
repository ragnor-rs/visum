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

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func0;
import rx.plugins.RxJavaHooks;
import rx.plugins.RxJavaPlugins;
import rx.schedulers.Schedulers;

import static io.reist.visum.ClientAssert.assertClientClassesRegistered;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * A basic test for Visum clients. Unlike {@link VisumImplTest}, this test doesn't support
 * assertions for multiple clients operating simultaneously in a single component cache.
 *
 * Set up your test with {@link #setUp(Func0, Class[])}. This method should be called in a setUp
 * method to enable component instantiation for the registered sub-classes of {@link VisumClient}.
 * The component and the first client (determined by {@link #createClient()}) will be created. A
 * corresponding {@link io.reist.visum.ComponentCache.ComponentEntry} will be available for test
 * cases as well as the instance of {@link ComponentCache}. Also, a local component cache listener
 * is registered to intercept component fabrication events {@link #onComponentCreated(Object)}) and
 * {@link #onComponentDestroyed(Object)}.
 *
 * Subclasses must call {@link #tearDown()} in tearDown methods to free resources.
 *
 * @see #getClient()
 * @see #getComponentCache()
 * @see #getComponentEntry()
 *
 * @param <C> client type
 */
public abstract class VisumTest<C extends VisumClient> implements ComponentCache.Listener {

    public ComponentCache getComponentCache() {
        return componentCache;
    }

    public ComponentCache.ComponentEntry getComponentEntry() {
        return componentEntry;
    }

    public C getClient() {
        return client;
    }

    private ComponentCache componentCache;

    private ComponentCache.ComponentEntry componentEntry;

    private C client;

    @SafeVarargs
    protected final void setUp(Func0<Object> componentFactory, Class<? extends C>... clientClasses) {

        componentCache = new ComponentCache();
        componentCache.register(componentFactory, clientClasses);
        componentCache.setListener(this);
        client = createClient();
        componentEntry = componentCache.findComponentEntryByClient(client);

        assertClientClassesRegistered(componentCache, clientClasses);

        assertNotNull("ComponentEntry has not been created via ComponentCache.register()", componentEntry);
        assertEquals("ComponentEntry factory doesn't match the supplied Func0", componentFactory, componentEntry.componentFactory);

        assertTrue("No clients should be attached till ComponentCache.start() is called", componentEntry.clients.isEmpty());
        assertNull("No components should be created until the very first call of ComponentCache.start()", componentEntry.component);

    }

    protected void tearDown() {
        componentCache.setListener(null);
        componentCache = null;
        client = null;
        componentEntry = null;
    }

    protected abstract C createClient();

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
