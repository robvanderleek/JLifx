package jlifx.commandline.command;

import java.io.PrintStream;

import jlifx.boblightd.BoblightDaemon;
import jlifx.commandline.CommandLineCommand;

public class BoblightDaemonCommand implements CommandLineCommand {

    @Override
    public boolean execute(String[] args, PrintStream out) throws Exception {
        new BoblightDaemon().run();
        return true;
    }

}
