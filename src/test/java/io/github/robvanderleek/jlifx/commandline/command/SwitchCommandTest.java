package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.Bulb;
import org.junit.Test;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class SwitchCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testSwitchOn() throws Exception {
        Bulb bulb = getMockedBulb();

        executeCommand(new SwitchCommand(), bulb, "switch", "on");

        verify(bulb, times(1)).switchOn();
    }

}