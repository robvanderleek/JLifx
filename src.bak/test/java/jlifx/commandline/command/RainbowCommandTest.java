package jlifx.commandline.command;

import static org.easymock.EasyMock.isA;

import java.awt.Color;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

import org.easymock.EasyMock;
import org.junit.Test;

public class RainbowCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testTimed() throws Exception {
        IBulb bulb = getMockedBulb();
        bulb.colorize(isA(Color.class), EasyMock.gt(0), EasyMock.eq(1.0F));
        EasyMock.expectLastCall().atLeastOnce();
        replayAll();
        
        executeCommand(new RainbowCommand(), bulb, "duration", "2");

        verifyAll();
    }

}