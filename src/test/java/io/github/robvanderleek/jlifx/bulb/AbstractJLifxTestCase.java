package io.github.robvanderleek.jlifx.bulb;

import io.github.robvanderleek.jlifx.commandline.AbstractBulbCommand;
import io.github.robvanderleek.jlifx.common.MacAddress;
import io.github.robvanderleek.jlifx.packet.PacketService;
import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AbstractJLifxTestCase {
    public static final InetAddress TEST_INET_ADDRESS = InetAddress.getLoopbackAddress();
    public static final MacAddress TEST_MAC_ADDRESS = new MacAddress(new byte[]{0x01, 0x02, 0x03, 0x04, 0x05, 0x06});

    protected Bulb getMockedBulb() {
        Bulb mock = mock(Bulb.class);
        when(mock.getMacAddress()).thenReturn(TEST_MAC_ADDRESS);
        return mock;
    }

    PacketService getMockedPacketService() {
        return mock(PacketService.class);
    }

    private PrintStream getPrintStream() {
        return new PrintStream(new ByteArrayOutputStream());
    }

    protected void executeCommand(AbstractBulbCommand command, Bulb bulb, String... commandArgs) throws Exception {
        command.execute(Collections.singletonList(bulb), commandArgs, getPrintStream());
    }

    int getFreeLocalPort() {
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