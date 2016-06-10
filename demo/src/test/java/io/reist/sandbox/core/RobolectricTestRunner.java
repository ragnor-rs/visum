package io.reist.sandbox.core;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricGradleTestRunner;

/**
 * Created by m039 on 12/1/15.
 */
public class RobolectricTestRunner extends RobolectricGradleTestRunner {

    public RobolectricTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

}
