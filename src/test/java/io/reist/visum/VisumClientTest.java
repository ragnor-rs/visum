package io.reist.visum;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

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

    public VisumClientTest() {
        super(VisumClientTest.TestComponent.class);
        register(
                TestVisumBaseClient.class,
                TestVisumAndroidService.class,
                TestVisumIntentService.class
        );
    }

    @Test
    public void visumBaseClient() {

        TestVisumBaseClient testClient = new TestVisumBaseClient(RuntimeEnvironment.application);

        testClient.onStartClient();
        checkClientAttached(testClient);

        Mockito.verify(getComponent(), Mockito.times(1)).inject(testClient);

        testClient.onStopClient();
        checkClientDetached(testClient);

    }

    @Test
    public void visumAndroidService() {

        TestVisumAndroidService testClient = new TestVisumAndroidService();

        testClient.onCreate();
        checkClientAttached(testClient);

        Mockito.verify(getComponent(), Mockito.times(1)).inject(testClient);

        testClient.onDestroy();
        checkClientDetached(testClient);

    }

    @Test
    public void visumIntentService() {

        TestVisumIntentService testClient = new TestVisumIntentService();

        testClient.onCreate();
        checkClientAttached(testClient);

        Mockito.verify(getComponent(), Mockito.times(1)).inject(testClient);

        testClient.onDestroy();
        checkClientDetached(testClient);

    }

    private static class TestVisumBaseClient extends VisumBaseClient {

        public TestVisumBaseClient(Context context) {
            super(context);
        }

        @Override
        public void inject(@NonNull Object from) {
            ((TestComponent) from).inject(this);
        }

    }

    private static class TestVisumAndroidService extends VisumAndroidService {

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

    private static class TestVisumIntentService extends VisumIntentService {

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

}
