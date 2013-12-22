package jlifx.commandline;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;

public class Main {
    private static PrintStream OUT = System.out;
    private static Map<String, CommandLineCommand> commands = new HashMap<String, CommandLineCommand>();
    static {
        ScanCommand scanCommand = new ScanCommand();
        commands.put(scanCommand.getCommandName(), scanCommand);
        SwitchCommand switchCommand = new SwitchCommand();
        commands.put(switchCommand.getCommandName(), switchCommand);
        ColorCommand colorCommand = new ColorCommand();
        commands.put(colorCommand.getCommandName(), colorCommand);
    }

    private static void printUsage() {
        OUT.println("Usage:");
        OUT.println("  java -jar lifx.jar <command>");
        OUT.println("");
        OUT.println("Where command can be:");
        OUT.println("  scan:");
        OUT.println("    Scan local network for a gateway bulb");
        OUT.println("  switch <mac-address|all> <on|off>:");
        OUT.println("    Switch selected bulb on/off");
        OUT.println("  color <mac-address|all> <rgb>:");
        OUT.println("    Set selected bulb to selected color (max. brightness)");
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