/*
 * Copyright (c) 2015  Zvooq LTD.
 * Authors: Renat Sarymsakov, Dmitriy Mozgin, Denis Volyntsev.
 *
 * This file is part of Visum.
 *
 * Visum is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Visum is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Visum.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.reist.visum.view;

import io.reist.visum.VisumClient;
import io.reist.visum.presenter.VisumPresenter;

/**
 * A MVP view which is capable of handling presenters' lifecycle.
 *
 * Created by Reist on 10/15/15.
 *
 * @param <P>   a class of a MVP presenter to be attached
 */
public interface VisumView<P extends VisumPresenter> extends VisumClient {

    /**
     * @return  an injected presenter
     */
    P getPresenter();

    /**
     * Attaches a view to a presenter returned with {@link #getPresenter()}.
     *
     * TODO should accept the presenter as a parameter
     */
    void attachPresenter();

    /**
     * Detaches a view from a presenter returned with {@link #getPresenter()}.
     *
     * TODO should accept the presenter as a parameter
     */
    void detachPresenter();

    int getViewId();

}
