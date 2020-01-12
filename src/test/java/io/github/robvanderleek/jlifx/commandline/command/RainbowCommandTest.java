package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.Bulb;
import io.github.robvanderleek.jlifx.common.Color;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.AdditionalMatchers.gt;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

class RainbowCommandTest extends AbstractJLifxTestCase {

    @Test
    void testTimed() throws Exception {
        Bulb bulb = getMockedBulb();

        executeCommand(new RainbowCommand(), bulb, "duration", "2");

        verify(bulb, atLeastOnce()).colorize(Mockito.isA(Color.class), gt(0), Mockito.eq(1.0F));
    }

}