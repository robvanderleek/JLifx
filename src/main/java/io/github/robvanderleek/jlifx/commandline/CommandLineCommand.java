package io.github.robvanderleek.jlifx.commandline;

import java.io.PrintStream;

public interface CommandLineCommand {
	boolean execute(String[] args, PrintStream out) throws Exception;
}