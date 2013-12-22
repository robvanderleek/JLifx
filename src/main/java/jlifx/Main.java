package jlifx;

import java.awt.Color;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class Main {
	private static final int PORT = 56700;

	private static PrintStream OUT = System.out;

	private static void printUsage() {
		OUT.println("Usage:");
		OUT.println("  java -jar lifx.jar <command>");
		OUT.println("");
		OUT.println("Where command can be:");
		OUT.println("  scan:");
		OUT.println("    Scan local network for a gateway bulb");
		OUT.println("  switch <mac-address> [on|off]:");
		OUT.println("    Switch selected bulb on/off");
		OUT.println("  color <mac-address> <rgb>:");
		OUT.println("    Set selected bulb to selected color (max. brightness)");
	}

	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			printUsage();
			System.exit(1);
		}
		if (args[0].equalsIgnoreCase("scan")) {
			new Main().scan();
		} else if (args[0].equalsIgnoreCase("switch")) {
			if (args.length != 3
					|| (!(args[2].equalsIgnoreCase("on") || args[2]
							.equalsIgnoreCase("off")))) {
				printUsage();
			} else {
				new Main().powerSwitchBulb(parseMacAddress(args[1]),
						args[2].equalsIgnoreCase("on") ? true : false);
			}
		} else if (args[0].equalsIgnoreCase("color")) {
			if (args.length != 3) {
				printUsage();
			} else {
				new Main().colorizeBulb(parseMacAddress(args[1]),
						Color.decode("#" + args[2]));
			}
		}
	}

	private static byte[] parseMacAddress(String macAddress) {
		byte[] result = new byte[6];
		result[0] = (byte) (Integer.parseInt(macAddress.substring(0, 2), 16));
		result[1] = (byte) (Integer.parseInt(macAddress.substring(3, 5), 16));
		result[2] = (byte) (Integer.parseInt(macAddress.substring(6, 8), 16));
		result[3] = (byte) (Integer.parseInt(macAddress.substring(9, 11), 16));
		result[4] = (byte) (Integer.parseInt(macAddress.substring(12, 14), 16));
		result[5] = (byte) (Integer.parseInt(macAddress.substring(15, 17), 16));
		return result;
	}

	private void scan() {
		Bulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
		if (gatewayBulb == null) {
			OUT.println("No LifX gateway bulb found!");
		} else {
			OUT.println("Found LifX gateway bulb!");
			OUT.println("  IP address: " + gatewayBulb.getInetAddress());
			OUT.println("  Mac address: " + gatewayBulb.getMacAddressAsString());
		}
	}

	private void powerSwitchBulb(byte[] macAddress, boolean on)
			throws IOException {
		Bulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
		PowerManagementPacket packet = new PowerManagementPacket(macAddress, on);
		Socket socket = new Socket(gatewayBulb.getInetAddress(), PORT);
		DataOutputStream dataOutputStream = new DataOutputStream(
				socket.getOutputStream());
		dataOutputStream.write(packet.toByteArray());
		socket.close();
	}

	private void colorizeBulb(byte[] macAddress, Color color)
			throws IOException {
		Bulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
		Packet packet = new ColorManagementPacket(macAddress, color);
		Socket socket = new Socket(gatewayBulb.getInetAddress(), PORT);
		DataOutputStream dataOutputStream = new DataOutputStream(
				socket.getOutputStream());
		dataOutputStream.write(packet.toByteArray());
		socket.close();
	}

}
