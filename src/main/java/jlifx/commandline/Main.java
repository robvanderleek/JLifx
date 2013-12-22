package jlifx.commandline;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Main {
	private static PrintStream OUT = System.out;
	private static Map<String, CommandLineCommand> commands = new HashMap<String, CommandLineCommand>();
	static {
		commands.put("scan", new ScanCommand());
		commands.put("switch", new SwitchCommand());
		commands.put("color", new ColorCommand());
		commands.put("blink", new BlinkCommand());
		commands.put("rainbow", new RainbowCommand());
	}

	private static void printUsage() {
		OUT.println("Usage:");
		OUT.println("  java -jar lifx.jar <command>");
		OUT.println("");
		OUT.println("Where command can be:");
		OUT.println("  scan:");
		OUT.println("    Scan local network for a gateway bulb");
		OUT.println("  switch  <mac-address|all> <on|off>:");
		OUT.println("    Switch selected bulb on/off");
		OUT.println("  color   <mac-address|all> <rgb>:");
		OUT.println("    Set selected bulb(s) to selected color (max. brightness)");
		OUT.println("  blink   <mac-address|all>:");
		OUT.println("    Blinks selected bulb(s) until program termination");
		OUT.println("  rainbow <mac-address|all>:");
		OUT.println("    Rainbow demo of selected bulb(s) until program termination");
		System.exit(1);
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 1) {
			printUsage();
		} else {
			CommandLineCommand command = commands.get(args[0].toLowerCase());
			if (command != null && command.execute(args, OUT)) {
				return;
			} else {
				printUsage();
			}
		}
	}
}