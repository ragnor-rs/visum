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

package io.reist.visum;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import io.reist.visum.view.BaseView;

/**
 * ComponentCache let you to create and destroy components manually
 * thereby providing custom scopes for yor view.
 * In other words this is scope manager for your views.
 */
public abstract class ComponentCache {

    private final AtomicLong idSequence = new AtomicLong();

    private final Map<Long, Object> componentMap = new HashMap<>();

    /**
     * Retrieves component from cache for given view or
     * creates a new component
     * @param view - BaseView with provides an identifier to retrieve component from cache
     */
    public Object getComponentFor(BaseView view) {

        Long componentId = view.getComponentId();

        if (componentId == null) {
            componentId = idSequence.incrementAndGet();
            view.setComponentId(componentId);
        }

        Object component = componentMap.get(componentId);

        if (component == null) {
            component = buildComponentFor(view.getClass());
            componentMap.put(componentId, component);
        }

        return component;

    }

    /**
     * Util method for creating new view component, every Component should be registered here
     */
    protected abstract Object buildComponentFor(Class<? extends BaseView> viewClass);

    /**
     * Destroys component for the given view.
     * If there's no component for the view nothing will happen.
     */
    public void invalidateComponentFor(BaseView view) {
        Long componentId = view.getComponentId();
        if (componentId == null) {
            return;
        }
        componentMap.remove(componentId);
    }

}
