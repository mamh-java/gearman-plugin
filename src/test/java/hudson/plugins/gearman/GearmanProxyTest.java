/*
 *
 * Copyright 2013 Hewlett-Packard Development Company, L.P.
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
 *
 */

package hudson.plugins.gearman;

import hudson.slaves.DumbSlave;

import org.junit.Test;
import org.jvnet.hudson.test.HudsonTestCase;

/**
 * Test for the {@link ExecutorWorkerThread} class.
 *
 * @author Khai Do
 */
public class GearmanProxyTest extends HudsonTestCase {

    GearmanProxy gp = GearmanProxy.getInstance();

    @Test
    public void testGetNumExecutors() throws Exception {

        DumbSlave slave = createSlave();

        assertEquals(0, gp.getNumExecutors());

        gp.getGewtHandles().add(new ExecutorWorkerThread("localhost", 4730, "test-exec-0", slave));
        gp.getGewtHandles().add(new ExecutorWorkerThread("localhost", 4730, "test-exec-1", slave));
        gp.getGewtHandles().add(new ExecutorWorkerThread("localhost", 4730, "test-exec-2", slave));

        assertEquals(3, gp.getNumExecutors());

        gp.getGewtHandles().add(new ManagementWorkerThread("localhost", 4730, "manage-exec-2"));

        assertEquals(4, gp.getNumExecutors());

        gp.getGewtHandles().clear();
    }

    @Test
    public void testCreateManagementWorker() {

        assertEquals(0, gp.getGmwtHandles().size());

        gp.createManagementWorker();

        assertEquals(1, gp.getGmwtHandles().size());
        assertTrue(gp.getGmwtHandles().get(0).isAlive());
        gp.getGmwtHandles().clear();
    }

    @Test
    public void testCreateExecutorWorkersOnNode() throws Exception {

        DumbSlave slave = createSlave();

        assertEquals(0, gp.getGewtHandles().size());

        gp.createExecutorWorkersOnNode(slave.toComputer());
        assertEquals(1, gp.getGewtHandles().size());

        gp.getGewtHandles().clear();
    }

    @Test
    public void testInitWorkers() {

        gp.initWorkers();

        assertEquals(2, gp.getGewtHandles().size());

        gp.getGmwtHandles().clear();
        gp.getGewtHandles().clear();
    }

    @Test
    public void testInitWorkers2() throws Exception {

        DumbSlave slave = createSlave();
        gp.initWorkers();

        assertEquals(3, gp.getGewtHandles().size());

        gp.getGmwtHandles().clear();
        gp.getGewtHandles().clear();
    }
}