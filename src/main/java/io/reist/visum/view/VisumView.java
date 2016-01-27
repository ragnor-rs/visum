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

import io.reist.visum.presenter.VisumPresenter;

/**
 * Created by Reist on 10/15/15.
 */
public interface VisumView<P extends VisumPresenter> {

    Long getComponentId();

    void setComponentId(Long componentId);

    Object getComponent();

    void inject(Object from);

    P getPresenter();

    /**
     * this is called once view is inflated and ready
     * Put your initialization code here instead of in onViewCreated()
     */
    void ready();

}
