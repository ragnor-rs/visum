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

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func0;

/**
 * ComponentCache provides custom scopes for your components.
 * In other words, this is a scope manager for your clients.
 */
public abstract class ComponentCache {

    private final List<ComponentEntry> componentEntries = new ArrayList<>();

    @CallSuper
    public Object onStartClient(@NonNull VisumClient client) {
        ComponentEntry entry = findComponentEntryByClientOrThrow(client);
        if (entry.component == null) {
            entry.component = entry.componentFactory.call();
        }
        entry.referenceCount++;
        return entry.component;
    }

    @NonNull
    private ComponentEntry findComponentEntryByClientOrThrow(@NonNull VisumClient client) {
        Class<? extends VisumClient> clientClass = client.getClass();
        ComponentEntry entry = findComponentEntryByClientClass(clientClass);
        if (entry == null) {
            throw new IllegalStateException(clientClass.getName() + " is not registered");
        }
        return entry;
    }

    private ComponentEntry findComponentEntryByClientClass(@NonNull Class<? extends VisumClient> clazz) {
        for (ComponentEntry componentEntry : componentEntries) {
            if (componentEntry.clientClasses.contains(clazz)) {
                return componentEntry;
            }
        }
        return null;
    }

    protected final void register(@NonNull List<Class<? extends VisumClient>> clientClasses, @NonNull Func0<Object> componentFactory) {

        if (clientClasses.isEmpty()) {
            throw new IllegalArgumentException("No classes specified");
        }

        for (Class<? extends VisumClient> clientClass : clientClasses) {
            if (findComponentEntryByClientClass(clientClass) != null) {
                throw new IllegalArgumentException(clientClass.getName() + " is already registered");
            }
        }

        componentEntries.add(new ComponentEntry(clientClasses, componentFactory));

    }

    @CallSuper
    public void onStopClient(@NonNull VisumClient client) {
        ComponentEntry entry = findComponentEntryByClientOrThrow(client);
        entry.referenceCount--;
        if (entry.referenceCount == 0) {
            entry.component = null;
        } else if (entry.referenceCount < 0) {
            throw new IllegalStateException(client + " is already detached");
        }
    }

    private static class ComponentEntry {

        private final List<Class<? extends VisumClient>> clientClasses;
        private final Func0<Object> componentFactory;

        private Object component;

        private int referenceCount;

        private ComponentEntry(List<Class<? extends VisumClient>> clientClasses, Func0<Object> componentFactory) {
            this.clientClasses = clientClasses;
            this.componentFactory = componentFactory;
        }

    }

}
