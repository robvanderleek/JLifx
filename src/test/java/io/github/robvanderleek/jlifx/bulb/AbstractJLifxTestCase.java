package io.github.robvanderleek.jlifx.bulb;

import io.github.robvanderleek.jlifx.packet.MacAddress;
import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;
import io.github.robvanderleek.jlifx.packet.PacketService;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractJLifxTestCase {
    public static final MacAddress TEST_MAC_ADDRESS_1 = new MacAddress(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06});
    public static final MacAddress TEST_MAC_ADDRESS_2 = new MacAddress(new byte[]{0x06, 0x05, 0x04, 0x03, 0x02, 0x01});
    private final IBulb mockedBulb = mock(IBulb.class);

    protected IBulb getMockedBulb() {
        return mockedBulb;
    }

    protected GatewayBulb getMockedGatewayBulb() {
        GatewayBulb result = mock(GatewayBulb.class);
        when(result.getMacAddress()).thenReturn(TEST_MAC_ADDRESS_1);
        when(result.getGatewayBulb()).thenReturn(result);
        return result;
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