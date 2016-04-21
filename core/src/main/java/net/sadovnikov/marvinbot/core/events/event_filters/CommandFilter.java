package net.sadovnikov.marvinbot.core.events.event_filters;


import net.sadovnikov.marvinbot.core.events.Event;
import net.sadovnikov.marvinbot.core.events.EventFilter;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.annotations.Command;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;

public class CommandFilter extends EventFilter<MessageEvent> {

    private final String COMMAND_PREFIX = "!";

    public boolean filter(MessageEvent evt, EventHandler handler) {

        Command cmdAnnotation = handler.getClass().getAnnotation(Command.class);
        String command = cmdAnnotation.value();

        String text = evt.getMessage().getText();
        String firstWord = text.split(" ")[0];
        command = COMMAND_PREFIX + ((String) command);
        if (firstWord.equals(command)) {
            return true;
        }
        return false;
    }
}
