package jlifx.commandline.command;

import static org.easymock.EasyMock.expectLastCall;

import org.junit.Test;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

public class BlinkCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testBlinkTwoTimes() throws Exception {
        IBulb bulb = getMockedBulb();
        bulb.switchOn();
        expectLastCall().times(2);
        bulb.switchOff();
        expectLastCall().times(2);
        replayAll();
        
        executeCommand(new BlinkCommand(), bulb, "times", "2");

        verifyAll();
    }

}