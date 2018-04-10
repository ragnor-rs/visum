/*
 * Copyright (C) 2017 Renat Sarymsakov.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.reist.visum.view;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

/**
 * Created by Reist on 16.06.16.
 */
public class VisumFragmentUtils {

    @SuppressWarnings("RestrictedApi")
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

    @SuppressWarnings("RestrictedApi")
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
