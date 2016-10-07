package jlifx.commandline.command;

import static org.mockito.Mockito.atLeastOnce;

import org.junit.Test;
import org.mockito.Mockito;

import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

public class DimCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testDimBulb() throws Exception {
        DimCommand command = new DimCommand();

        IBulb bulb = getMockedBulb();

        executeCommand(command, bulb, "dim", "0.5");

        Mockito.verify(bulb, atLeastOnce()).setDim(0.5F);
    }

}
