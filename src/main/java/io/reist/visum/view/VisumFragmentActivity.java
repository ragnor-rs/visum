package io.reist.visum.view;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import io.reist.visum.R;

/**
 * Created by defuera on 29/01/2016.
 * This Activity helps you handle your fragments
 */
public class VisumFragmentActivity extends AppCompatActivity implements VisumFragment.FragmentController{

    /**
     * @param fragment - fragment to display
     * @param remove   - boolean, stays for whether current fragment should be thrown away or stay in a back stack.
     *                 false to stay in a back stack
     */
    @Override
    public void showFragment(VisumFragment fragment, boolean remove) {
        showFragment(fragment, remove, false);
    }

    /**
     * @param popBackStackInclusive all entries up to but not including that entry will be removed
     * @see VisumFragmentActivity#showFragment
     */
    protected void showFragment(VisumFragment fragment, boolean remove, boolean popBackStackInclusive) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment topmostFragment = findTopmostFragment(fragmentManager);
        if (topmostFragment != null && fragment.getName().equals(topmostFragment.getTag())) {
            return;
        }
        replace(fragmentManager, topmostFragment, fragment, remove, popBackStackInclusive);
    }

    private static void replace(FragmentManager fragmentManager, Fragment what, VisumFragment with, boolean remove, boolean popBackStackInclusive) {
        if (popBackStackInclusive && fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate(fragmentManager.getBackStackEntryAt(0).getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if (what != null && !popBackStackInclusive) {
            if (remove) {
                transaction = transaction.remove(what);
            } else {
                transaction = transaction.hide(what);
            }
        }

        String fragmentName = with.getName();

        if (with.isAdded()) {
            transaction = transaction.show(with);
        } else {
            transaction = transaction.add(R.id.fragment_container, with, fragmentName);
        }

        transaction.addToBackStack(fragmentName).commit();
    }

    @Nullable
    protected static Fragment findTopmostFragment(FragmentManager fragmentManager) {
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
