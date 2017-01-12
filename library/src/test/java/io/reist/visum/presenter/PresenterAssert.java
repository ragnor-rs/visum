package io.reist.visum.presenter;

import org.junit.Assert;

import io.reist.visum.view.VisumView;
import rx.Single;
import rx.functions.Action1;

/**
 * Created by Reist on 07.06.16.
 */
public class PresenterAssert {

    public interface AssertTestPresenter {
        void assertPresenterAttached(int viewId, VisumView view);
        void assertPresenterDetached(int viewId, VisumView view);
    }

    public static void assertPresenterDetached(AssertTestPresenter presenter, int viewId, VisumView view) {
        presenter.assertPresenterDetached(viewId, view);
    }

    public static void assertPresenterAttached(AssertTestPresenter presenter, int viewId, VisumView view) {
        presenter.assertPresenterAttached(viewId, view);
    }

    public static void assertViewSubscribe(int viewId, TestPresenter presenter, boolean expected) {
        try {
            presenter.subscribe(viewId, Single.just(true), new Action1<Boolean>() {

                @Override
                public void call(Boolean aBoolean) {}

            });
            Assert.assertTrue("subscribe(view) shouldn't work here", expected);
        } catch (Exception e) {
            Assert.assertFalse("subscribe(view) should work here", expected);
        }
    }

    public static void assertGlobalSubscribe(TestPresenter presenter, boolean expected) {
        try {
            presenter.subscribe(Single.just(true), new ViewNotifier<VisumView, Boolean>() {

                @Override
                public void notifyCompleted(VisumView visumView) {}

                @Override
                public void notifyResult(VisumView view, Boolean aBoolean) {}

                @Override
                public void notifyError(VisumView view, Throwable e) {}

            });
            Assert.assertTrue("subscribe() should work here", expected);
        } catch (Exception e) {
            Assert.assertFalse("subscribe() shouldn't work here", expected);
        }
    }

}
