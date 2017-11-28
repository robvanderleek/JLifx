package jlifx.commandline.command;

import java.awt.Color;

import org.junit.Test;
import org.mockito.Mockito;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

public class ColorCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testColorBulbDefaultBrightness() throws Exception {
        ColorCommand command = new ColorCommand();
        IBulb bulb = getMockedBulb();
        
        executeCommand(command, bulb, "color", "green");

        Mockito.verify(bulb).colorize(Color.GREEN, 3, 1.0F);
    }

    @Test
    public void testColorBulbDefaultLowBrightness() throws Exception {
        ColorCommand command = new ColorCommand();
        IBulb bulb = getMockedBulb();

        executeCommand(command, bulb, "color", "green", "0.1");

        Mockito.verify(bulb).colorize(Color.GREEN, 3, 0.1F);
    }

}
