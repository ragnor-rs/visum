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

package io.reist.sandbox.core;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.test.ActivityInstrumentationTestCase2;

import io.reist.sandbox.app.SandboxApplication;
import io.reist.sandbox.app.view.MainActivity;
import io.reist.visum.ComponentCache;

/**
 * Created by m039 on 11/30/15.
 */
public class ActivityInstrumentationTestCase<T extends MainActivity> extends ActivityInstrumentationTestCase2<T> {

    public ActivityInstrumentationTestCase(Class<T> clazz) {
        super(clazz);
    }

    private static ComponentCache sDefaultComponentCache = null;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
        injectInstrumentation(instrumentation);

        SandboxApplication sandboxApplication = (SandboxApplication) instrumentation
                .getTargetContext()
                .getApplicationContext();

        synchronized (ActivityInstrumentationTestCase.class) {
            if (sDefaultComponentCache == null) {
                sDefaultComponentCache = sandboxApplication.getComponentCache();
            }

            sandboxApplication.setComponentCache(sDefaultComponentCache);
        }
    }
}
