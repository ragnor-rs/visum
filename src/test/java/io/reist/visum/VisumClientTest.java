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
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.util.ServiceController;

/**
 * Created by Reist on 26.05.16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = {Build.VERSION_CODES.JELLY_BEAN},
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
        assertClientAttached(testClient);

        Mockito.verify(getComponent(), Mockito.times(1)).inject(testClient);

        testClient.onStopClient();
        assertClientDetached(testClient);

    }

    @Test
    public void visumAndroidService() {

        ServiceController<TestVisumAndroidService> serviceController =
                Robolectric.buildService(TestVisumAndroidService.class);
        TestVisumAndroidService testClient = serviceController.get();

        serviceController.create();
        assertClientAttached(testClient);

        Mockito.verify(getComponent(), Mockito.times(1)).inject(testClient);

        serviceController.destroy();
        assertClientDetached(testClient);

    }

    @Test
    public void visumIntentService() {

        ServiceController<TestVisumIntentService> serviceController =
                Robolectric.buildService(TestVisumIntentService.class);
        TestVisumIntentService testClient = serviceController.get();

        serviceController.create();
        assertClientAttached(testClient);

        Mockito.verify(getComponent(), Mockito.times(1)).inject(testClient);

        serviceController.destroy();
        assertClientDetached(testClient);

    }

    public static class TestVisumBaseClient extends VisumBaseClient {

        public TestVisumBaseClient(Context context) {
            super(context);
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestComponent) from).inject(this);
        }

    }

    public static class TestVisumAndroidService extends VisumAndroidService {

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestComponent) from).inject(this);
        }

    }

    public static class TestVisumIntentService extends VisumIntentService {

        public TestVisumIntentService() {
            super(TestVisumIntentService.class.getSimpleName());
        }

        @Override
        protected void onHandleIntent(Intent intent) {}

        @Override
        public void inject(@NonNull Object from) {
            ((TestComponent) from).inject(this);
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
