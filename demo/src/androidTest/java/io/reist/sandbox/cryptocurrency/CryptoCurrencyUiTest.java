package io.reist.sandbox.cryptocurrency;

import android.support.annotation.IdRes;
import android.support.test.espresso.contrib.DrawerActions;
import android.support.test.espresso.contrib.NavigationViewActions;
import android.support.test.runner.AndroidJUnit4;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.reist.sandbox.R;
import io.reist.sandbox.app.view.MainActivity;
import io.reist.sandbox.core.ActivityInstrumentationTestCase;
import io.reist.sandbox.core.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class CryptoCurrencyUiTest extends ActivityInstrumentationTestCase<MainActivity> {

    private MainActivity mMainActivity;

    public CryptoCurrencyUiTest() {
        super(MainActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
    }

    @Test
    public void testLike() throws InterruptedException {

        onView(isRoot()).perform(TestUtils.waitId(R.id.drawer_layout, TestUtils.ACTION_TIMEOUT));

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.nav_cryptocurrency));

        onView(isRoot()).perform(TestUtils.waitId(R.id.item_cryptocurrency_label, TestUtils.ACTION_TIMEOUT));

        Thread.sleep(60000);

        assertNotEquals(hasPrice(R.id.cryptocurrency_recycler_view), "");

    }

    private String hasPrice(@IdRes int recyclerViewId) {

        TextView price = ((RecyclerView) mMainActivity.findViewById(recyclerViewId))
                .findViewHolderForAdapterPosition(0)
                .itemView
                .findViewById(R.id.item_cryptocurrency_price);

        return price.getText().toString();

    }

}