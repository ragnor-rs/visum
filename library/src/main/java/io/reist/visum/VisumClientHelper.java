package io.reist.visum;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A helper class for implementations of {@link VisumClient}. It provides callback for typical
 * Android components such as {@link android.app.Service}.
 *
 * Created by Reist on 19.05.16.
 */
public final class VisumClientHelper<C extends VisumClient> {

    private static final String INJECT_METHOD_NAME = "inject";

    private static final String TAG = VisumClientHelper.class.getSimpleName();

    protected final C client;

    public VisumClientHelper(@NonNull C client) {
        this.client = client;
    }

    public C getClient() {
        return client;
    }

    @NonNull
    public ComponentCache getComponentCache() {
        return ((ComponentCacheProvider) client.getContext().getApplicationContext()).getComponentCache();
    }

    @SuppressWarnings({"unchecked", "TryWithIdenticalCatches"})
    public void onCreate() {

        Object component = client.getComponentCache().start(client);

        Class clientClass = client.getClass();

        try {

            Class componentClass = component.getClass();

            Log.d(TAG, String.format(
                    "onCreate: looking for '%s' methods in [%s] for [%s]",
                    INJECT_METHOD_NAME, componentClass.getSimpleName(), clientClass.getSimpleName()
            ));

            for (Method method : componentClass.getMethods()) {
                Class types[] = method.getParameterTypes();
                if (
                        method.getName().startsWith(INJECT_METHOD_NAME) &&
                                types != null && types.length == 1 &&
                                types[0].isAssignableFrom(clientClass)
                        ) {
                    method.invoke(component, client);
                    Log.d(TAG, String.format(
                            "onCreate: client [%s] was injected by [%s] with [%s]",
                            clientClass.getSimpleName(), componentClass.getSimpleName(), method.getName()
                    ));
                    return; // all ok
                }
            }

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        Log.w(TAG, "No inject methods for client [" + clientClass.getSimpleName() + "]");

    }

    public void onDestroy(boolean retainComponent) {
        client.getComponentCache().stop(client, retainComponent);
    }

    @NonNull
    public Context getContext() {
        return client.getContext();
    }

}
