package jlifx.commandline.command;

import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.awt.Color;

import org.junit.Test;
import org.mockito.Mockito;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

public class RainbowCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testTimed() throws Exception {
        IBulb bulb = getMockedBulb();
        
        executeCommand(new RainbowCommand(), bulb, "duration", "2");

        verify(bulb, atLeastOnce()).colorize(Mockito.isA(Color.class), gt(0), Mockito.eq(1.0F));
    }

}