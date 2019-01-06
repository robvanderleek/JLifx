package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.Bulb;
import org.junit.Test;
import org.mockito.Mockito;

import java.awt.*;

public class ColorCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testColorBulbDefaultBrightness() throws Exception {
        ColorCommand command = new ColorCommand();
        Bulb bulb = getMockedBulb();

        executeCommand(command, bulb, "color", "green");

        Mockito.verify(bulb).colorize(Color.GREEN, 3, 1.0F);
    }

    @Test
    public void testColorBulbDefaultLowBrightness() throws Exception {
        ColorCommand command = new ColorCommand();
        Bulb bulb = getMockedBulb();

        executeCommand(command, bulb, "color", "green", "0.1");

        Mockito.verify(bulb).colorize(Color.GREEN, 3, 0.1F);
    }

}
