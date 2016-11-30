package io.reist.visum.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by Reist on 16.06.16.
 */
public class VisumFragmentUtils {

    protected static void detachPresenterInChildFragments(FragmentManager childFragmentManager) {
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (fragments == null) {
            return;
        }
        for (Fragment fragment : fragments) {
            if (fragment instanceof VisumFragment) {
                ((VisumFragment) fragment).detachPresenter();
            } else if (fragment instanceof VisumDialogFragment) {
                ((VisumDialogFragment) fragment).detachPresenter();
            } else if (fragment instanceof VisumBottomSheetDialogFragment) {
                ((VisumBottomSheetDialogFragment) fragment).detachPresenter();
            }
        }
    }

    protected static void attachPresenterInChildFragments(FragmentManager childFragmentManager) {
        List<Fragment> fragments = childFragmentManager.getFragments();
        if (fragments == null) {
            return;
        }
        for (Fragment fragment : fragments) {
            if (fragment instanceof VisumFragment) {
                ((VisumFragment) fragment).attachPresenter();
            } else if (fragment instanceof VisumDialogFragment) {
                ((VisumDialogFragment) fragment).attachPresenter();
            } else if (fragment instanceof VisumBottomSheetDialogFragment) {
                ((VisumBottomSheetDialogFragment) fragment).attachPresenter();
            }
        }
    }

}
