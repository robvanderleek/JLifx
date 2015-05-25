package jlifx.commandline.command;

import java.io.PrintStream;
import java.util.Collections;

import jlifx.bulb.IBulb;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.easymock.EasyMock;
import org.easymock.EasyMockSupport;
import org.junit.Test;

public class BlinkCommandTest extends EasyMockSupport {

    @Test
    public void testBlinkTwoTimes() throws Exception {
        BlinkCommand command = new BlinkCommand();
        IBulb bulb = createMock(IBulb.class);
        bulb.switchOn();
        EasyMock.expectLastCall().times(2);
        bulb.switchOff();
        EasyMock.expectLastCall().times(2);
        replayAll();
        
        command.execute(Collections.singletonList(bulb), new String[] {"-times", "2"}, new PrintStream(
            new ByteArrayOutputStream()));

        verifyAll();
    }

}