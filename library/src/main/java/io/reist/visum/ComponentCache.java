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

package io.reist.visum;

import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import rx.functions.Func0;

/**
 * ComponentCache provides custom scopes for your components.
 * In other words, this is a scope manager for your clients.
 */
public class ComponentCache {

    private final List<ComponentEntry> componentEntries = new ArrayList<>();

    private Listener listener;

    @CallSuper
    public Object start(@NonNull VisumClient client) {

        ComponentEntry entry = findComponentEntryByClient(client);

        if (entry == null) {
            return null;
        }

        if (entry.component == null) {
            entry.component = entry.componentFactory.call();
            if (listener != null) {
                listener.onComponentCreated(entry.component);
            }
        }

        if (!entry.clients.contains(client)) {
            entry.clients.add(client);
        }

        return entry.component;

    }

    public boolean isClientAttached(@NonNull VisumClient client) {
        ComponentEntry entry = findComponentEntryByClientOrThrow(client);
        return entry.clients.contains(client);
    }

    protected final ComponentEntry findComponentEntryByClient(@NonNull VisumClient client) {
        return findComponentEntryByClientClass(client.getClass());
    }

    /**
     * @throws IllegalStateException    thrown if a type of the given client is not registered
     *                                  via {@link #register(Func0, Class[])}
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
            for (Class<? extends VisumClient> clientClass : componentEntry.clientClasses) {
                if (clientClass.isAssignableFrom(clazz)) {
                    return componentEntry;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unused")
    @SafeVarargs
    public final void register(@NonNull Func0<Object> componentFactory, Class<? extends VisumClient>... clientClasses) {

        if (clientClasses.length == 0) {
            throw new IllegalArgumentException("No classes specified");
        }

        for (Class<? extends VisumClient> clientClass : clientClasses) {
            if (findComponentEntryByClientClass(clientClass) != null) {
                throw new IllegalArgumentException(clientClass.getName() + " is already registered");
            }
        }

        componentEntries.add(new ComponentEntry(Arrays.asList(clientClasses), componentFactory));

    }

    @CallSuper
    public void stop(@NonNull VisumClient client, boolean retainComponent) {
        ComponentEntry entry = findComponentEntryByClientOrThrow(client);

        List<VisumClient> clients = entry.clients;

        if (!clients.remove(client)) {
            return;
        }

        if (retainComponent || !clients.isEmpty()) {
            return;
        }

        Object component = entry.component;
        entry.component = null;
        if (listener != null) {
            listener.onComponentDestroyed(component);
        }

    }

    protected void setListener(Listener listener) {
        this.listener = listener;
    }

    /**
     * Represents an entry of registered {@link VisumClient} types. Every entry is a combo of
     * client classes, client instances, a component which represents dependency injection graph
     * and a component factory.
     *
     * When a client type is registered via {@link #register(Func0, Class[])}, a new entry is
     * created. In this entry, {@link #component} is null and {@link #clients} is empty.
     *
     * To take advantage of dependency injection, a {@link VisumClient} may be started via
     * {@link #start(VisumClient)}. This method assigns a new component to the {@link #component} or
     * reuses the old one.
     *
     * If the client is at the end if its lifecycle, {@link #stop(VisumClient, boolean)} should be
     * called to free resources. The method destroys the component if it's not used by other clients
     * by de-referencing {@link #component}.
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

        Object getComponent() {
            return component;
        }

    }

    /**
     * Created by Reist on 01.06.16.
     */
    public interface Listener {

        void onComponentDestroyed(Object component);

        void onComponentCreated(Object component);

    }

}
