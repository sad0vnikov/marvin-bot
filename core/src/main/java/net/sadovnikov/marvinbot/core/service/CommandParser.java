package net.sadovnikov.marvinbot.core.service;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage;
import org.apache.logging.log4j.LogManager;

import java.util.Arrays;

/**
 * Takes net.sadovnikov.marvinbot.core.domain.message contents and returns Command object if a net.sadovnikov.marvinbot.core.domain.message syntax contains command
 */
public class CommandParser {

    private char commandPrefix;

    @Inject
    public CommandParser(@Named("commandPrefix") String commandPrefix) {
        if (commandPrefix.length() != 1) {
            LogManager.getLogger("core-logger").warn("Command prefix must be a single char!");
        }

        this.commandPrefix = commandPrefix.charAt(0);
    }

    public Command parse(String message) {

        if (message.charAt(0) == commandPrefix) {
            String[] splittedMsg = message.split(" ");
            String command = splittedMsg[0].replaceFirst(String.valueOf(commandPrefix), "");
            String[] arguments = Arrays.copyOfRange(splittedMsg, 1, splittedMsg.length);

            return new Command(command, arguments);
        }

        return null;
    }

    public boolean getIsCommand(String message) {
        return parse(message) != null;
    }

    public boolean getIsCommand(ReceivedMessage msg) {
        return getIsCommand(msg.text());
    }
}
