package jlifx.commandline;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

import jlifx.commandline.command.BlinkCommand;
import jlifx.commandline.command.BoblightDaemonCommand;
import jlifx.commandline.command.ColorCommand;
import jlifx.commandline.command.RainbowCommand;
import jlifx.commandline.command.ScanCommand;
import jlifx.commandline.command.StatusCommand;
import jlifx.commandline.command.SwitchCommand;

public final class Main {
    private static final PrintStream OUT = System.out;
    private static Map<String, CommandLineCommand> commands = new HashMap<String, CommandLineCommand>();
    static {
        commands.put("daemon", new BoblightDaemonCommand());
        commands.put("scan", new ScanCommand());
        commands.put("status", new StatusCommand());
        commands.put("switch", new SwitchCommand());
        commands.put("color", new ColorCommand());
        // commands.put("dim", new DimCommand());
        commands.put("blink", new BlinkCommand());
        commands.put("rainbow", new RainbowCommand());
    }

    private Main() {}

    private static void printUsage() {
        OUT.println("Usage:");
        OUT.println("  java -jar jlifx.jar <command>");
        OUT.println("");
        OUT.println("Where command can be:");
        OUT.println("  daemon (starts Boblight daemon)");
        OUT.println("  scan");
        OUT.println("  status");
        OUT.println("  switch  <mac-address|gateway|all> <on|off>");
        OUT.println("  color   <mac-address|gateway|all> [brightness (0.0 - 1.0)]");
        // OUT.println("  dim     [-gw <ip-address> <mac-address>] <mac-address|gateway|all> <0.0 - 1.0>");
        OUT.println("  blink   <mac-address|gateway|all> [times]");
        OUT.println("  rainbow <mac-address|gateway|all> [duration (sec)]");
        OUT.println("");
        printExamples();
    }

    private static void printExamples() {
        OUT.println("Examples:");
        OUT.println("  java -jar jlifx.jar switch all off");
        OUT.println("  java -jar jlifx.jar color all red");
        // OUT.println("  java -jar jlifx.jar dim all 0.5");
        OUT.println("  java -jar jlifx.jar blink gateway");
        OUT.println("  java -jar jlifx.jar blink AA:BB:CC:DD:EE:FF 3");
        OUT.println("  java -jar jlifx.jar rainbow all");
        OUT.println("  java -jar jlifx.jar rainbow all 30");
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