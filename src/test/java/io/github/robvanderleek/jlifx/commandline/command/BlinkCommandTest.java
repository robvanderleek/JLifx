package io.github.robvanderleek.jlifx.commandline.command;

import static org.mockito.Mockito.times;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.IBulb;
import org.junit.Test;
import org.mockito.Mockito;

public class BlinkCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testBlinkTwoTimes() throws Exception {
        IBulb bulb = getMockedBulb();
        
        executeCommand(new BlinkCommand(), bulb, "times", "2");

        Mockito.verify(bulb, times(2)).switchOn();
        Mockito.verify(bulb, times(2)).switchOff();
    }

}