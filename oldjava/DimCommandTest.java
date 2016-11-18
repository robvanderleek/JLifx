package jlifx.commandline.command;

import static org.easymock.EasyMock.expectLastCall;
import jlifx.bulb.AbstractJLifxTestCase;
import jlifx.bulb.IBulb;

import org.junit.Test;

public class DimCommandTest extends AbstractJLifxTestCase {

    @Test
    public void testDimBulb() throws Exception {
        DimCommand command = new DimCommand();
        IBulb bulb = getMockedBulb();
        bulb.setDim(0.5F);
        expectLastCall().once();
        replayAll();

        executeCommand(command, bulb, "dim", "0.5");

        verifyAll();
    }

}
