package io.github.robvanderleek.jlifx.commandline.command;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.IBulb;
import org.junit.Test;

public class SwitchCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testSwitchOn() throws Exception {
        IBulb bulb = getMockedBulb();

        executeCommand(new SwitchCommand(), bulb, "switch", "on");

        verify(bulb, times(1)).switchOn();
    }

}