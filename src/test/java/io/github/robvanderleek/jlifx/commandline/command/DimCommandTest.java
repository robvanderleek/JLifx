package io.github.robvanderleek.jlifx.commandline.command;

import io.github.robvanderleek.jlifx.bulb.AbstractJLifxTestCase;
import io.github.robvanderleek.jlifx.bulb.Bulb;
import org.junit.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.atLeastOnce;

public class DimCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testDimBulb() throws Exception {
        DimCommand command = new DimCommand();

        Bulb bulb = getMockedBulb();

        executeCommand(command, bulb, "dim", "0.5");

        Mockito.verify(bulb, atLeastOnce()).setDim(0.5F);
    }

}
