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
import java.util.Collections;
import java.util.List;

import rx.functions.Func0;

/**
 * ComponentCache provides custom scopes for your components.
 * In other words, this is a scope manager for your clients.
 */
public class ComponentCache {

    private final List<ComponentEntry> componentEntries = new ArrayList<>();

    @CallSuper
    public Object start(@NonNull VisumClient client) {
        ComponentEntry entry = findComponentEntryByClientOrThrow(client);
        if (entry.component == null) {
            entry.component = entry.componentFactory.call();
        }
        if (entry.clients.contains(client)) {
            throw new IllegalStateException(client + " is already attached");
        }
        entry.clients.add(client);
        return entry.component;
    }

    protected final ComponentEntry findComponentEntryByClient(@NonNull VisumClient client) {
        return findComponentEntryByClientClass(client.getClass());
    }

    /**
     * @throws IllegalStateException    thrown if a type of the given client is not registered
     *                                  via {@link #register(Class, Func0)} or
     *                                  {@link #register(List, Func0)}
     */
    @NonNull
    protected final ComponentEntry findComponentEntryByClientOrThrow(@NonNull VisumClient client) {
        ComponentEntry entry = findComponentEntryByClient(client);
        if (entry == null) {
            throw new IllegalStateException(client.getClass() + " is not registered");
        }
        return entry;
    }

    protected final ComponentEntry findComponentEntryByClientClass(@NonNull Class<? extends VisumClient> clazz) {
        for (ComponentEntry componentEntry : componentEntries) {
            for(Class<? extends VisumClient> clientClass : componentEntry.clientClasses) {
                if (clientClass.isAssignableFrom(clazz)) {
                    return componentEntry;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    protected final void register(@NonNull List<? extends Class<? extends VisumClient>> clientClasses, @NonNull Func0<Object> componentFactory) {

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

    @SuppressWarnings("unused")
    protected final void register(@NonNull Class<? extends VisumClient> clientClass, @NonNull Func0<Object> componentFactory) {
        register(Collections.singletonList(clientClass), componentFactory);
    }

    @CallSuper
    public void stop(@NonNull VisumClient client) {
        ComponentEntry entry = findComponentEntryByClientOrThrow(client);
        List<VisumClient> clients = entry.clients;
        if (clients.remove(client)) {
            if (clients.isEmpty()) {
                entry.component = null;
            }
        } else  {
            throw new IllegalStateException(client + " is already detached");
        }
    }

    /**
     * Represents an entry of registered {@link VisumClient} types. Every entry is a combo of
     * client classes, client instances, a component which represents dependency injection graph
     * and a component factory.
     *
     * When a client type is registered via {@link #register(List, Func0)} or
     * {@link #register(Class, Func0)}, a new entry is created. In this entry, {@link #component}
     * is null and {@link #clients} is empty.
     *
     * To take advantage of dependency injection, a {@link VisumClient} may be started via
     * {@link #start(VisumClient)}. This method assigns a new component to the {@link #component} or
     * reuses the old one.
     *
     * If the client is at the end if its lifecycle, {@link #stop(VisumClient)} should be called to
     * free resources. The method destroys the component if it's not used by other clients by
     * de-referencing {@link #component}.
     */
    static class ComponentEntry {

        final List<? extends Class<? extends VisumClient>> clientClasses;
        final Func0<Object> componentFactory;

        final List<VisumClient> clients = new ArrayList<>();

        Object component;

        private ComponentEntry(List<? extends Class<? extends VisumClient>> clientClasses, Func0<Object> componentFactory) {
            this.clientClasses = clientClasses;
            this.componentFactory = componentFactory;
        }

    }

}
