package io.reist.visum.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;

import io.reist.visum.R;

/**
 * Created by m039 on 11/13/15.
 */
public class BaseActivity extends AppCompatActivity
        implements BaseFragment.FragmentController {

    /**
     * @param fragment - fragment to display
     * @param remove   - boolean, stays for whether current fragment should be thrown away or stay in a back stack.
     *                 false to stay in a back stack
     */
    @Override
    public void showFragment(BaseFragment fragment, boolean remove) {
        showFragment(fragment, remove, false);
    }

    protected void showFragment(BaseFragment fragment, boolean remove, boolean popBackStackInclusive) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment topmostFragment = findTopmostFragment(fragmentManager);
        if (topmostFragment != null && fragment.getName().equals(topmostFragment.getTag())) {
            return;
        }
        replace(fragmentManager, topmostFragment, fragment, remove, popBackStackInclusive);
    }

    private static void replace(FragmentManager fragmentManager, Fragment what, BaseFragment with, boolean remove, boolean popBackStackInclusive) {
        if (popBackStackInclusive && fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate(fragmentManager.getBackStackEntryAt(0).getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (what != null) {
            if (remove) {
                transaction.remove(what);
            } else {
                transaction.hide(what);
            }
        }

        String fragmentName = with.getName();

        if (with.isAdded()) {
            transaction.show(with);
        } else {
            transaction.add(R.id.fragment_container, with, fragmentName);
        }

        transaction.show(with).addToBackStack(fragmentName).commit();
    }

    @Nullable
    private static Fragment findTopmostFragment(FragmentManager fragmentManager) {
        int backStackEntryCount = fragmentManager.getBackStackEntryCount();

        Fragment topmostFragment;
        if (backStackEntryCount > 0) {
            String fragmentName = fragmentManager.getBackStackEntryAt(backStackEntryCount - 1).getName();
            topmostFragment = fragmentManager.findFragmentByTag(fragmentName);
        } else {
            topmostFragment = null;
        }
        return topmostFragment;
    }
}
