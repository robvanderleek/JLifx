package jlifx;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DiscoveryService {
	private static final Log LOG = LogFactory.getLog(DiscoveryService.class);
	private static final int PORT = 56700;

	public static Bulb discoverGatewayBulb() {
		List<InetAddress> networkBroadcastAddresses = getNetworkBroadcastAddresses();
		for (InetAddress broadcastAddress : networkBroadcastAddresses) {
			Bulb result = discoverGatewayBulb(broadcastAddress);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	private static Bulb discoverGatewayBulb(InetAddress broadcastAddress) {
		Bulb result = null;
		Packet packet = new Packet();
		byte[] byteArray = packet.toByteArray();
		try {
			DatagramSocket socket = new DatagramSocket(PORT);
			DatagramPacket datagramPacket = new DatagramPacket(byteArray, 0,
					byteArray.length, broadcastAddress, PORT);
			socket.setSoTimeout(1000);
			boolean gatewayResponse = false;
			int retries = 3;
			socket.send(datagramPacket);
			while (!gatewayResponse && retries > 0) {
				DatagramPacket answer = new DatagramPacket(byteArray,
						byteArray.length);
				try {
					socket.receive(answer);
				} catch (SocketTimeoutException e) {
					retries--;
					continue;
				}
				InetAddress inetAddress = answer.getAddress();
				if (inetAddress.isAnyLocalAddress()
						|| inetAddress.isLoopbackAddress()
						|| NetworkInterface.getByInetAddress(inetAddress) != null) {
					retries--;
					continue;
				} else {
					gatewayResponse = true;
					Packet answerLifxPacket = Packet.fromDatagramPacket(answer);
					result = new Bulb(answer.getAddress(),
							answerLifxPacket.getGatewayMac());
				}
			}
		} catch (SocketException e) {
		} catch (IOException e) {
		}
		return result;
	}

	static List<InetAddress> getNetworkBroadcastAddresses() {
		List<InetAddress> result = new ArrayList<InetAddress>();
		try {
			List<NetworkInterface> networkInterfaces = Collections
					.list(NetworkInterface.getNetworkInterfaces());
			for (NetworkInterface networkInterface : networkInterfaces) {
				if (!networkInterface.isLoopback() && networkInterface.isUp()) {
					List<InterfaceAddress> interfaceAddresses = networkInterface
							.getInterfaceAddresses();
					for (InterfaceAddress interfaceAddress : interfaceAddresses) {
						InetAddress broadcast = interfaceAddress.getBroadcast();
						if (broadcast != null) {
							result.add(broadcast);
						}
					}
				}
			}
		} catch (SocketException e) {
			LOG.error("Could not retreive broadcast addresses from available network interfaces");
		}
		return result;
	}
}