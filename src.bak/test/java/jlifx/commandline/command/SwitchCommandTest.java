package jlifx.commandline.command;

import static org.easymock.EasyMock.expectLastCall;
import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

import org.junit.Test;

public class SwitchCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testSwitchOn() throws Exception {
        IBulb bulb = getMockedBulb();
        bulb.switchOn();
        expectLastCall().once();
        replayAll();

        executeCommand(new SwitchCommand(), bulb, "switch", "on");

        verifyAll();
    }
}
