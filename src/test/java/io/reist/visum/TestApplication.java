package io.reist.visum;

import android.app.Application;
import android.support.annotation.NonNull;

/**
 * Created by Reist on 26.05.16.
 */
public class TestApplication extends Application implements ComponentCacheProvider {

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
