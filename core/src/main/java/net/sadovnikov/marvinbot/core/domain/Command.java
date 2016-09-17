package net.sadovnikov.marvinbot.core.domain;

import java.util.Arrays;
import java.util.Optional;

public class Command {

    private String[] args;
    private String command;

    public Command(String command, String[] args) {
        this.command = command;
        this.args    = args;
    }

    public String getCommand() {
        return this.command;
    }

    public String[] getArgs() {
        return this.args;
    }

    public Optional<String> action() {
        Optional<String> action = Optional.empty();
        String[] args = getArgs();
        if (args.length > 0) {
            action = Optional.of(args[0]);
        }

        return action;
    }

    public Optional<String[]> actionArgs() {
        Optional<String[]> optionArgs = Optional.empty();
        String[] args = getArgs();
        if (args.length > 1) {
            optionArgs = Optional.of(Arrays.copyOfRange(args, 1, args.length));
        }

        return optionArgs;
    }
}
