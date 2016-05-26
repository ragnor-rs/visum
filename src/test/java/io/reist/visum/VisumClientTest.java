package io.reist.visum;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Arrays;

import rx.functions.Func0;

/**
 * Created by Reist on 26.05.16.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(
        constants = BuildConfig.class,
        sdk = {Build.VERSION_CODES.JELLY_BEAN},
        application = VisumClientTest.TestApplication.class
)
public class VisumClientTest {

    private TestVisumBaseClient testVisumBaseClient;
    private TestVisumAndroidService testVisumAndroidService;
    private TestVisumIntentService testVisumIntentService;

    private TestComponent testComponent;

    @Before
    public void start() {

        getComponentCache().register(
                Arrays.asList(TestVisumBaseClient.class, TestVisumAndroidService.class, TestVisumIntentService.class),
                new Func0<Object>() {

                    @Override
                    public Object call() {
                        return testComponent = Mockito.mock(TestComponent.class);
                    }

                }
        );

        testVisumBaseClient = new TestVisumBaseClient(RuntimeEnvironment.application);
        testVisumAndroidService = new TestVisumAndroidService();
        testVisumIntentService = new TestVisumIntentService();

    }

    @NonNull
    public ComponentCache getComponentCache() {
        return ((TestApplication) RuntimeEnvironment.application).getComponentCache();
    }

    @Test
    public void visumBaseClient() {

        testVisumBaseClient.onStartClient();
        checkClientStart(testVisumBaseClient);

        Mockito.verify(testComponent, Mockito.times(1)).inject(testVisumBaseClient);

        testVisumBaseClient.onStopClient();
        checkClientStop(testVisumBaseClient);

    }

    @Test
    public void visumAndroidService() {

        testVisumAndroidService.onCreate();
        checkClientStart(testVisumAndroidService);

        Mockito.verify(testComponent, Mockito.times(1)).inject(testVisumAndroidService);

        testVisumAndroidService.onDestroy();
        checkClientStop(testVisumAndroidService);

    }

    @Test
    public void visumIntentService() {

        testVisumIntentService.onCreate();
        checkClientStart(testVisumIntentService);

        Mockito.verify(testComponent, Mockito.times(1)).inject(testVisumIntentService);

        testVisumIntentService.onDestroy();
        checkClientStop(testVisumIntentService);

    }

    private void checkClientStop(VisumClient client) {
        ComponentCache.ComponentEntry componentEntry = getComponentCache().findComponentEntryByClient(client);
        Assert.assertTrue("Requested client stop but there are still some clients attached", componentEntry.clients.isEmpty());
        Assert.assertNull("ComponentCache should have removed the unused component", componentEntry.component);
    }

    private void checkClientStart(VisumClient client) {
        ComponentCache.ComponentEntry componentEntry = getComponentCache().findComponentEntryByClient(client);
        Assert.assertTrue(
                "Only one client should be registered here",
                componentEntry.clients.size() == 1 && componentEntry.clients.contains(client)
        );
        Assert.assertNotNull("No component has been created for the client", componentEntry.component);
    }

    @After
    public void finish() {
        testVisumBaseClient = null;
        testVisumAndroidService = null;
        testVisumIntentService = null;
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

    private interface TestComponent {

        void inject(TestVisumBaseClient testVisumBaseClient);

        void inject(TestVisumAndroidService testVisumAndroidService);

        void inject(TestVisumIntentService testVisumIntentService);

    }

    public static class TestApplication extends Application implements ComponentCacheProvider {

        protected ComponentCache componentCache;

        @NonNull
        @Override
        public ComponentCache getComponentCache() {
            if (componentCache == null) {
                componentCache = new ComponentCache();
            }
            return componentCache;
        }

    }

}
