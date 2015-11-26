package io.reist.visum;

import android.content.Context;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reist.visum.model.Response;
import io.reist.visum.model.ResponseImpl;

@Module
public class BaseModule {

    public static final String RESPONSE_CLASS = "response_class";

    protected final Context context;
    protected final Visum visum;

    public BaseModule(Context context) {
        this.context = context;
        this.visum = new Visum.Builder().build();
    }

    @Provides
    Context context() {
        return context;
    }

    @Provides
    Visum visum() {
        return visum;
    }

}
