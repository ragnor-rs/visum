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
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ServiceController;

import static io.reist.visum.ClientAssert.assertClientAttached;
import static io.reist.visum.ClientAssert.assertClientDetached;

/**
 * Created by Reist on 26.05.16.
 */
@RunWith(RobolectricTestRunner.class)
@Config(
        sdk = Build.VERSION_CODES.N,
        application = TestApplication.class
)
public class VisumClientTest extends VisumImplTest<VisumClientTest.TestComponent> {

    @Override
    protected TestComponent createComponent() {
        return Mockito.mock(VisumClientTest.TestComponent.class);
    }

    @Test
    public void visumBaseClient() {

        TestVisumBaseClient testClient = new TestVisumBaseClient(RuntimeEnvironment.application);

        testClient.onStartClient();
        assertClientAttached(getComponentCache(), testClient);

        Mockito.verify(getComponent(), Mockito.times(1)).inject(testClient);

        testClient.onStopClient();
        assertClientDetached(getComponentCache(), testClient);

    }

    @Test
    public void visumAndroidService() {

        ServiceController<TestVisumAndroidService> serviceController =
                Robolectric.buildService(TestVisumAndroidService.class);
        TestVisumAndroidService testClient = serviceController.get();

        serviceController.create();
        assertClientAttached(getComponentCache(), testClient);

        Mockito.verify(getComponent(), Mockito.times(1)).inject(testClient);

        serviceController.destroy();
        assertClientDetached(getComponentCache(), testClient);

    }

    @Test
    public void visumIntentService() {

        ServiceController<TestVisumIntentService> serviceController =
                Robolectric.buildService(TestVisumIntentService.class);
        TestVisumIntentService testClient = serviceController.get();

        serviceController.create();
        assertClientAttached(getComponentCache(), testClient);

        Mockito.verify(getComponent(), Mockito.times(1)).inject(testClient);

        serviceController.destroy();
        assertClientDetached(getComponentCache(), testClient);

    }

    public static class TestVisumBaseClient extends VisumBaseClient {

        public TestVisumBaseClient(Context context) {
            super(context);
        }

        @Override
        public void inject(@NonNull Object component) {
            ((TestComponent) component).inject(this);
        }

    }

    public static class TestVisumAndroidService extends VisumAndroidService {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void inject(@NonNull Object component) {
            ((TestComponent) component).inject(this);
        }

    }

    public static class TestVisumIntentService extends VisumIntentService {

        public TestVisumIntentService() {
            super(TestVisumIntentService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {}

        @Override
        public void inject(@NonNull Object component) {
            ((TestComponent) component).inject(this);
        }

    }

    protected interface TestComponent {

        void inject(TestVisumBaseClient testVisumBaseClient);

        void inject(TestVisumAndroidService testVisumAndroidService);

        void inject(TestVisumIntentService testVisumIntentService);

    }

    @Before
    public void start() throws Exception {
        setUp();
        register(
                TestVisumBaseClient.class,
                TestVisumAndroidService.class,
                TestVisumIntentService.class
        );
    }

    @After
    public void finish() {
        tearDown();
    }

}
