package io.github.robvanderleek.jlifx.commandline;

import io.github.robvanderleek.jlifx.commandline.command.BlinkCommand;
import io.github.robvanderleek.jlifx.commandline.command.BoblightDaemonCommand;
import io.github.robvanderleek.jlifx.commandline.command.ColorCommand;
import io.github.robvanderleek.jlifx.commandline.command.RainbowCommand;
import io.github.robvanderleek.jlifx.commandline.command.ScanCommand;
import io.github.robvanderleek.jlifx.commandline.command.StatusCommand;
import io.github.robvanderleek.jlifx.commandline.command.SwitchCommand;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public final class Main {
    private static final PrintStream OUT = System.out;
    private static final Map<String, CommandLineCommand> commands = new HashMap<>();

    static {
        commands.put("daemon", new BoblightDaemonCommand());
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
        OUT.println();
        OUT.println("Where command can be:");
        OUT.println("  daemon (starts Boblight daemon)");
        OUT.println("  scan");
        OUT.println("  status");
        OUT.println("  switch  <ip-address|bulb name|all> <on|off>");
        OUT.println("  color   <ip-address|bulb name|all> [brightness (0.0 - 1.0)]");
        OUT.println("  blink   <ip-address|bulb name|all> [times]");
        OUT.println("  rainbow <ip-address|bulb name|all> [duration (sec)]");
        OUT.println();
        printExamples();
    }

    private static void printExamples() {
        OUT.println("Examples:");
        OUT.println("  java -jar jlifx.jar switch all off");
        OUT.println("  java -jar jlifx.jar color all red");
        OUT.println("  java -jar jlifx.jar color livingroom red");
        OUT.println("  java -jar jlifx.jar blink 192.168.178.41 3");
        OUT.println("  java -jar jlifx.jar rainbow all");
        OUT.println("  java -jar jlifx.jar rainbow all 30");
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 1) {
            printUsage();
        } else {
            CommandLineCommand command = commands.get(args[0].toLowerCase());
            if (command == null || !command.execute(args, OUT)) {
                printUsage();
            }
        }
    }
}