package jlifx.commandline.command;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

import org.easymock.EasyMock;
import org.junit.Test;

public class BlinkCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testBlinkTwoTimes() throws Exception {
        IBulb bulb = getMockedBulb();
        bulb.switchOn();
        EasyMock.expectLastCall().times(2);
        bulb.switchOff();
        EasyMock.expectLastCall().times(2);
        replayAll();
        
        executeCommand(new BlinkCommand(), bulb, "times", "2");

        verifyAll();
    }

}