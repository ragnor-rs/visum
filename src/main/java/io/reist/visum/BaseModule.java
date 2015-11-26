package io.reist.visum;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@Module
public class BaseModule {

    public static final String RESPONSE_CLASS = "response_class";

    protected final Context context;

    public BaseModule(Context context) {
        this.context = context;
    }

    @Provides
    Context context() {
        return context;
    }

}
