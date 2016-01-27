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

package io.reist.visum.model;

import java.util.List;

import rx.Observable;

/**
 * Created by Reist on 12/2/15.
 */
public interface VisumService<T> {

    Observable<? extends VisumResponse<List<T>>> list();

    // todo cur this is not true. Api will not wrap simple model into a wrapper
    Observable<? extends VisumResponse<T>> byId(Long id);

    Observable<? extends VisumResponse<List<T>>> save(List<T> list);

    Observable<? extends VisumResponse<T>> save(T t);

    Observable<? extends VisumResponse<Integer>> delete(Long id);

    VisumResponse<List<T>> saveSync(List<T> list);

    VisumResponse<T> saveSync(T t);

}
