package jlifx.bulb;

import java.io.PrintStream;
import java.util.Collections;

import jlifx.commandline.AbstractBulbCommand;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.easymock.EasyMockSupport;

public class AbstractJLifxTestCase extends EasyMockSupport {
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
}
