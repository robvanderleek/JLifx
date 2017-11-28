package jlifx.commandline.command;

import static org.mockito.Mockito.times;

import org.junit.Test;
import org.mockito.Mockito;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

public class BlinkCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testBlinkTwoTimes() throws Exception {
        IBulb bulb = getMockedBulb();
        
        executeCommand(new BlinkCommand(), bulb, "times", "2");

        Mockito.verify(bulb, times(2)).switchOn();
        Mockito.verify(bulb, times(2)).switchOff();
    }

}