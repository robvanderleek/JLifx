package jlifx.commandline.command;

import static org.easymock.EasyMock.expectLastCall;

import java.awt.Color;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

import org.junit.Test;

public class ColorCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testColorBulbDefaultBrightness() throws Exception {
        ColorCommand command = new ColorCommand();
        IBulb bulb = getMockedBulb();
        bulb.colorize(Color.GREEN, 3, 1.0F);
        expectLastCall().once();
        replayAll();

        executeCommand(command, bulb, "color", "green");

        verifyAll();
    }

    @Test
    public void testColorBulbDefaultLowBrightness() throws Exception {
        ColorCommand command = new ColorCommand();
        IBulb bulb = getMockedBulb();
        bulb.colorize(Color.GREEN, 3, 0.1F);
        expectLastCall().once();
        replayAll();

        executeCommand(command, bulb, "color", "green", "0.1");

        verifyAll();
    }

}
