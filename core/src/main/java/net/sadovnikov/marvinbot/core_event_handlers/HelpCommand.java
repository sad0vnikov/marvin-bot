package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.command.CommandExecutor;
import net.sadovnikov.marvinbot.core.command.annotations.Command;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.main.PluginLoader;
import net.sadovnikov.marvinbot.core.message_sender.MessageSender;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginManager;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

@Command("help")
@Extension
public class HelpCommand extends CommandExecutor {

    MessageSender sender;
    ResourceBundle locData;
    PluginManager pluginManager;

    @Inject
    public HelpCommand(MessageSender sender, PluginLoader pluginLoader, Locale locale) {
        this.sender = sender;
        this.locData = ResourceBundle.getBundle("loc_data_main", locale);
        this.pluginManager = pluginLoader.getPluginManager();
    }

    @Override
    public void execute(net.sadovnikov.marvinbot.core.command.Command cmd, MessageEvent evt) {
        List<CommandExecutor> cmdExecutors = pluginManager.getExtensions(CommandExecutor.class);

        StringBuilder help = new StringBuilder();

        help.append(locData.getString("helpHeader"));
        help.append("\n");
        for (CommandExecutor executor : cmdExecutors) {
            if (executor.getClass().isAnnotationPresent(Command.class)) {
                help.append(getHelpStringFromCommandExecutor(executor));
                help.append("\n");
            }
        }

        if (cmdExecutors.size() == 0) {
            help.append(locData.getString("noAvailableCommands"));
        }

        sender.reply(evt.getMessage(), help.toString());
    }

    protected String getHelpStringFromCommandExecutor(CommandExecutor executor) {
        StringBuilder helpString = new StringBuilder();
        helpString.append("!");
        helpString.append(executor.getClass().getAnnotation(Command.class).value());
        helpString.append(" — ");
        helpString.append(executor.getHelp());
        return helpString.toString();
    }

    public String getHelp() {
        return locData.getString("helpCommandHelp");
    }
}
