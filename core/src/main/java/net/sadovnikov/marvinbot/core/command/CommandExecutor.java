package net.sadovnikov.marvinbot.core.command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * You can make bot to process commands by extending this class and adding @Extension annotation
 * and @Command('command') annotation where 'command' is a command to be processed
 */
public abstract class CommandExecutor extends EventHandler<MessageEvent> {

    private CommandParser parser;

    @Inject protected Injector injector;
    @Inject Locale locale;

    public final void handle(MessageEvent ev) {
        CommandParser parser = injector.getInstance(CommandParser.class);
        Command cmd = parser.parse(ev.getMessage().getText());
        String annotatedCommand = this.getClass().getAnnotation(net.sadovnikov.marvinbot.core.command.annotations.Command.class).value();
        if (cmd != null && annotatedCommand != null && cmd.getCommand().equals(annotatedCommand)) {
            execute(cmd, ev);
        }
    }

    public abstract void execute(Command cmd, MessageEvent ev);

    public String getHelp() {
        ResourceBundle locData = ResourceBundle.getBundle("loc_data_main", locale);
        return locData.getString("noHelpAvailable");
    }

}
