package jlifx.commandline.command;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

public class SwitchCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testSwitchOn() throws Exception {
        IBulb bulb = getMockedBulb();

        executeCommand(new SwitchCommand(), bulb, "switch", "on");

        verify(bulb, times(1)).switchOn();
    }

}