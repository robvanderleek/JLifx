package io.github.robvanderleek.jlifx.commandline.command;

import java.io.PrintStream;

import io.github.robvanderleek.jlifx.boblightd.BoblightDaemon;
import io.github.robvanderleek.jlifx.commandline.CommandLineCommand;

public class BoblightDaemonCommand implements CommandLineCommand {

    @Override
    public boolean execute(String[] args, PrintStream out) throws Exception {
        new BoblightDaemon().run();
        return true;
    }

}
