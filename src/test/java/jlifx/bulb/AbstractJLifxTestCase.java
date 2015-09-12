package jlifx.bulb;

import java.io.PrintStream;
import java.util.Collections;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.easymock.EasyMockSupport;

import jlifx.commandline.AbstractBulbCommand;

public class AbstractJLifxTestCase extends EasyMockSupport {
    public static final byte[] TEST_MAC_ADDRESS_1 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
    public static final byte[] TEST_MAC_ADDRESS_2 = new byte[] {0x06, 0x05, 0x04, 0x03, 0x02, 0x01};
    private final IBulb mockedBulb = createMock(IBulb.class);

    protected IBulb getMockedBulb() {
        return mockedBulb;
    }

    protected PrintStream getPrintStream() {
        return new PrintStream(new ByteArrayOutputStream());
    }

    protected void executeCommand(AbstractBulbCommand command, IBulb bulb, String... commandArgs) throws Exception {
        command.execute(Collections.singletonList(bulb), commandArgs, getPrintStream());
    }

    protected void startTestBulb() {

    }

    protected void stopTestBulb() {

    }
}
