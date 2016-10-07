package jlifx.bulb;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Collections;

import org.apache.commons.io.output.ByteArrayOutputStream;

import jlifx.commandline.AbstractBulbCommand;
import jlifx.packet.PacketService;

public class AbstractJLifxTestCase {
    public static final byte[] TEST_MAC_ADDRESS_1 = new byte[] {0x01, 0x02, 0x03, 0x04, 0x05, 0x06};
    public static final byte[] TEST_MAC_ADDRESS_2 = new byte[] {0x06, 0x05, 0x04, 0x03, 0x02, 0x01};
    private final IBulb mockedBulb = mock(IBulb.class);

    protected IBulb getMockedBulb() {
        return mockedBulb;
    }

    protected GatewayBulb getMockedGatewayBulb() {
        return mock(GatewayBulb.class);
    }

    protected PacketService getMockedPacketService() {
        return mock(PacketService.class);
    }

    protected PrintStream getPrintStream() {
        return new PrintStream(new ByteArrayOutputStream());
    }

    protected void executeCommand(AbstractBulbCommand command, IBulb bulb, String... commandArgs) throws Exception {
        command.execute(Collections.singletonList(bulb), commandArgs, getPrintStream());
    }

    protected int getFreeLocalPort() {
        int result;
        try {
            ServerSocket serverSocket = new ServerSocket(0);
            result = serverSocket.getLocalPort();
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}