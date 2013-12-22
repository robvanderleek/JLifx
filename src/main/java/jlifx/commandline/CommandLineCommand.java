package jlifx.commandline;

import java.io.PrintStream;

public interface CommandLineCommand {
    String getCommandName();

    boolean execute(String[] args, PrintStream out) throws Exception;
}