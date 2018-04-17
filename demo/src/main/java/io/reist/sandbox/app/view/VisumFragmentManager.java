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

package io.reist.sandbox.app.view;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import io.reist.visum.view.VisumFragment;

/**
 * Use this class to manage your {@link VisumFragment}s.
 * <p>
 * Created by defuera on 29/01/2016.
 * <p>
 * TODO perhaps it should be removed from Visum
 */
public class VisumFragmentManager {

    /**
     * @param fragment - fragment to display
     * @param remove   - boolean, stays for whether current fragment should be thrown away or stay in a back stack.
     *                 false to stay in a back stack
     * @param resId    - id of view group to put fragment to
     */
    @SuppressWarnings("unused")
    public static void showFragment(FragmentManager fragmentManager, VisumFragment fragment, @IdRes int resId, boolean remove) {
        showFragment(fragmentManager, fragment, resId, remove, false);
    }

    /**
     * @param popBackStackInclusive all entries up to but not including that entry will be removed
     */
    public static void showFragment(
            FragmentManager fragmentManager,
            VisumFragment fragment,
            @IdRes int resId,
            boolean remove,
            boolean popBackStackInclusive
    ) {
        showFragment(fragmentManager, fragment, resId, remove, popBackStackInclusive, 0, 0, 0, 0);
    }

    public static void showFragment(
            FragmentManager fragmentManager,
            VisumFragment fragment,
            @IdRes int resId,
            boolean remove,
            boolean popBackStackInclusive,
            @AnimRes int animEnter,
            @AnimRes int animExit,
            @AnimRes int animPopEnter,
            @AnimRes int animPopExit

    ) {
        Fragment topmostFragment = findTopmostFragment(fragmentManager);
        replace(fragmentManager, topmostFragment, fragment, resId, remove, popBackStackInclusive, animEnter, animExit, animPopEnter, animPopExit);
    }

    private static void replace(
            FragmentManager fragmentManager,
            @Nullable Fragment what,
            VisumFragment with,
            @IdRes int resId,
            boolean remove,
            boolean popBackStackInclusive
    ) {
        replace(fragmentManager, what, with, resId, remove, popBackStackInclusive, 0, 0, 0, 0);
    }

    private static void replace(
            FragmentManager fragmentManager,
            @Nullable Fragment what,
            VisumFragment with,
            @IdRes int resId,
            boolean remove,
            boolean popBackStackInclusive,
            @AnimRes int animEnter,
            @AnimRes int animExit,
            @AnimRes int animPopEnter,
            @AnimRes int animPopExit
    ) {
        if (popBackStackInclusive && fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate(fragmentManager.getBackStackEntryAt(0).getName(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(animEnter, animExit, animPopEnter, animPopExit);

        commitFragmentTransaction(what, with, resId, remove, popBackStackInclusive, transaction);
    }

    private static void commitFragmentTransaction(@Nullable Fragment what,
                                                  VisumFragment with,
                                                  @IdRes int resId,
                                                  boolean remove,
                                                  boolean popBackStackInclusive,
                                                  FragmentTransaction transaction
    ) {
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
            transaction = transaction.add(resId, with, fragmentName);
        }

        transaction.addToBackStack(fragmentName).commit();
    }

    @Nullable
    public static Fragment findTopmostFragment(FragmentManager fragmentManager) {
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