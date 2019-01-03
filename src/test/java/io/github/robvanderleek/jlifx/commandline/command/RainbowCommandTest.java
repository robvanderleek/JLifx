package io.github.robvanderleek.jlifx.commandline.command;

import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.awt.Color;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.IBulb;
import org.junit.Test;
import org.mockito.Mockito;

public class RainbowCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testTimed() throws Exception {
        IBulb bulb = getMockedBulb();
        
        executeCommand(new RainbowCommand(), bulb, "duration", "2");

        verify(bulb, atLeastOnce()).colorize(Mockito.isA(Color.class), gt(0), Mockito.eq(1.0F));
    }

}