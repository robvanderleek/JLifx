package jlifx.commandline;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import jlifx.commandline.command.BlinkCommand;
import jlifx.commandline.command.ColorCommand;
import jlifx.commandline.command.RainbowCommand;
import jlifx.commandline.command.ScanCommand;
import jlifx.commandline.command.StatusCommand;
import jlifx.commandline.command.SwitchCommand;
import jlifx.packet.PacketService;

public final class Main {
	private static final PrintStream OUT = System.out;
	private static Map<String, CommandLineCommand> commands = new HashMap<String, CommandLineCommand>();
	static {
		commands.put("scan", new ScanCommand());
		commands.put("status", new StatusCommand());
		commands.put("switch", new SwitchCommand());
		commands.put("color", new ColorCommand());
		commands.put("blink", new BlinkCommand());
		commands.put("rainbow", new RainbowCommand());
	}

	private Main() {
	}

	private static void printUsage() {
		OUT.println("Usage:");
		OUT.println("  java -jar jlifx.jar <command>");
		OUT.println("");
		OUT.println("Where command can be:");
		OUT.println("  scan");
		OUT.println("  status  <mac-address|all>");
		OUT.println("  switch  <mac-address|all> <on|off>");
		OUT.println("  color   <mac-address|all> <color-name|rgb-hex-value>");
		OUT.println("  blink   <mac-address|all> [times]");
		OUT.println("  rainbow <mac-address|all>");
		OUT.println("");
		OUT.println("Examples:");
		OUT.println("  java -jar jlifx.jar switch all off");
		OUT.println("  java -jar jlifx.jar blink AA:BB:CC:DD:EE:FF 3");
		OUT.println("  java -jar jlifx.jar rainbow all");
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			printUsage();
		} else {
			CommandLineCommand command = commands.get(args[0].toLowerCase());
			if (command != null && command.execute(args, OUT)) {
				PacketService.closeSocket();
				return;
			} else {
				printUsage();
			}
		}
	}
}