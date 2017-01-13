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

}
