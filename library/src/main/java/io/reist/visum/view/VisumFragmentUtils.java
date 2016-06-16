package io.reist.visum.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Reist on 16.06.16.
 */
public class VisumFragmentUtils {

    protected static void detachPresenterInChildFragments(FragmentManager childFragmentManager) {
        for (Fragment fragment : childFragmentManager.getFragments()) {
            if (fragment instanceof VisumFragment) {
                ((VisumFragment) fragment).detachPresenter();
            } else if (fragment instanceof VisumDialogFragment) {
                ((VisumDialogFragment) fragment).detachPresenter();
            }
        }
    }

    protected static void attachPresenterInChildFragments(FragmentManager childFragmentManager) {
        for (Fragment fragment : childFragmentManager.getFragments()) {
            if (fragment instanceof VisumFragment) {
                ((VisumFragment) fragment).attachPresenter();
            } else if (fragment instanceof VisumDialogFragment) {
                ((VisumDialogFragment) fragment).attachPresenter();
            }
        }
    }

}
