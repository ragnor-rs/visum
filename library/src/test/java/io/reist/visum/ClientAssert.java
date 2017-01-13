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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Created by Reist on 07.06.16.
 */
public class ClientAssert {

    public static void assertClientClassesRegistered(ComponentCache componentCache, Class<? extends VisumClient>[] clientClasses) {
        for (Class<? extends VisumClient> clientClass : clientClasses) {
            if (componentCache.findComponentEntryByClientClass(clientClass) == null) {
                fail("ComponentEntry client classes don't match the supplied ones via ComponentCache.register()");
            }
        }
    }

    public static void assertClientStopped(ComponentCache componentCache, VisumClient client) {
        assertTrue("Requested client stop but there are still some clients attached", componentCache.findComponentEntryByClient(client).clients.isEmpty());
    }

    public static void assertClientStoppedAndComponentRemoved(ComponentCache componentCache, VisumClient client) {
        assertClientStopped(componentCache, client);
        ComponentCache.ComponentEntry componentEntry = componentCache.findComponentEntryByClientClass(client.getClass());
        assertNotNull("Entry is null", componentEntry);
        assertNull("ComponentCache should have removed the unused component", componentEntry.getComponent());
    }

    public static void assertClientStoppedAndComponentRetained(ComponentCache componentCache, VisumClient client) {
        assertClientStopped(componentCache, client);
        ComponentCache.ComponentEntry componentEntry = componentCache.findComponentEntryByClientClass(client.getClass());
        assertNotNull("Entry is null", componentEntry);
        assertNotNull("The component should have been retained", componentEntry.getComponent());
    }

    public static void assertClientStarted(ComponentCache componentCache, VisumClient client) {
        ComponentCache.ComponentEntry componentEntry = componentCache.findComponentEntryByClient(client);
        assertNotNull("Client has not been attached", componentEntry);
        assertNotNull("No component has been created for the client", componentEntry.component);
    }

    public static void assertClientDetached(ComponentCache componentCache, VisumClient client) {
        ComponentCache.ComponentEntry componentEntry = componentCache.findComponentEntryByClient(client);
        assertFalse("There are still some clients attached", componentEntry.clients.contains(client));
        assertNull("ComponentCache should have removed the unused component", componentEntry.component);
    }

    public static void assertClientAttached(ComponentCache componentCache, VisumClient client) {
        ComponentCache.ComponentEntry componentEntry = componentCache.findComponentEntryByClient(client);
        assertTrue("The client is not attached", componentEntry.clients.contains(client));
        assertNotNull("No component has been created for the client", componentEntry.component);
    }

}
