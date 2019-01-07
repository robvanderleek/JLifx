package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.Bulb;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;

public class BlinkCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testBlinkTwoTimes() throws Exception {
        Bulb bulb = getMockedBulb();

        executeCommand(new BlinkCommand(), bulb, "times", "2");

        Mockito.verify(bulb, times(2)).switchOn();
        Mockito.verify(bulb, times(2)).switchOff();
    }

}