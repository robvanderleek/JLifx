package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.common.Color;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ColorCommandTest extends AbstractJLifxTestCase {

    @Test
    void testColorBulbDefaultBrightness() throws Exception {
        ColorCommand command = new ColorCommand();
        Bulb bulb = getMockedBulb();

        executeCommand(command, bulb, "color", "green");

        Mockito.verify(bulb).colorize(Color.GREEN, 3, 1.0F);
    }

    @Test
    void testColorBulbDefaultLowBrightness() throws Exception {
        ColorCommand command = new ColorCommand();
        Bulb bulb = getMockedBulb();

        executeCommand(command, bulb, "color", "green", "0.1");

        Mockito.verify(bulb).colorize(Color.GREEN, 3, 0.1F);
    }

}