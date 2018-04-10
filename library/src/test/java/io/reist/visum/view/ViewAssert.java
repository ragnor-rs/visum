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

import org.mockito.InOrder;
import org.mockito.Mockito;

import io.reist.visum.presenter.VisumPresenter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by Reist on 07.06.16.
 */
public class ViewAssert {

    public static void assertPresenterReattached(VisumView oldTestView, VisumView newTestView) {

        assertFalse("View has not been recreated", newTestView == oldTestView);

        VisumPresenter oldPresenter = oldTestView.getPresenter();
        VisumPresenter newPresenter = newTestView.getPresenter();

        assertNotNull("No presenter is attached to the new instance of the view", newPresenter);
        assertTrue("Presenter didn't survive", oldPresenter == newPresenter);

    }

    public static void assertPresenterAttachedAfterOnActivityResult(VisumResultReceiver view) {

        VisumResultReceiver dummy = view.getDummy();

        InOrder inOrder = Mockito.inOrder(dummy);
        inOrder.verify(dummy, Mockito.times(2)).attachPresenter();
        inOrder.verify(dummy, Mockito.times(1)).onActivityResult();

    }

}
