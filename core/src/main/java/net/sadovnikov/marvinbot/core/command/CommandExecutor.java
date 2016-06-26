package net.sadovnikov.marvinbot.core.command;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import net.sadovnikov.marvinbot.core.command.annotations.RequiredRole;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.message_sender.MessageSender;
import net.sadovnikov.marvinbot.core.permissions.PermissionChecker;
import net.sadovnikov.marvinbot.core.permissions.Role;
import net.sadovnikov.marvinbot.core.plugin.PluginException;

import java.security.Permission;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * You can make bot to process commands by extending this class and adding @Extension annotation
 * and @Command('command') annotation where 'command' is a command to be processed
 */
public abstract class CommandExecutor extends EventHandler<MessageEvent> {

    private CommandParser parser;

    @Inject protected Injector injector;
    @Inject protected Locale locale;
    @Inject protected @Named("commandPrefix") String commandPrefix;

    public final void handle(MessageEvent ev) throws PluginException {
        CommandParser parser = injector.getInstance(CommandParser.class);
        Command cmd = parser.parse(ev.getMessage().getText());
        String annotatedCommand = this.getClass().getAnnotation(net.sadovnikov.marvinbot.core.command.annotations.Command.class).value();
        int requiredRole = Role.USER;
        RequiredRole roleAnnotation = this.getClass().getAnnotation(RequiredRole.class);
        if (roleAnnotation != null) {
            requiredRole = roleAnnotation.value();
        }
        PermissionChecker permissionChecker = injector.getInstance(PermissionChecker.class);
        boolean userHasRequiredRole = permissionChecker.checkPermissionsByMessage(ev.getMessage(), requiredRole);
        boolean commandDetected = cmd != null && annotatedCommand != null && cmd.getCommand().equals(annotatedCommand);
        if (commandDetected && userHasRequiredRole) {
            execute(cmd, ev);
        } else if (commandDetected && !userHasRequiredRole) {
            MessageSender messageSender = injector.getInstance(MessageSender.class);
            ResourceBundle locData = ResourceBundle.getBundle("loc_data_main", locale);
            String notEnoughPermissionsMessage = locData.getString("notEnoughPermissionsMessageToExecuteCommand");
            messageSender.reply(ev.getMessage(), notEnoughPermissionsMessage);
        }
    }

    public abstract void execute(Command cmd, MessageEvent ev) throws PluginException;

    public String getHelp() {
        ResourceBundle locData = ResourceBundle.getBundle("loc_data_main", locale);
        return locData.getString("noHelpAvailableForCommand");
    }

    /**
     * Returns usage example for command
     */
    public String getUsage() {
        return "There is no usage example for this command.";
    }

}
