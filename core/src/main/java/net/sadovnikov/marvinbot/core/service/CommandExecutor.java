package net.sadovnikov.marvinbot.core.service;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import net.sadovnikov.marvinbot.core.annotations.RequiredRole;
import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.domain.user.Role;
import net.sadovnikov.marvinbot.core.domain.user.UserRole;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderService;
import net.sadovnikov.marvinbot.core.service.permissions.PermissionChecker;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;

import java.util.Locale;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;

/**
 * You can make bot to process commands by extending this class and adding @Extension annotation
 * and @Command('command') annotation where 'command' is a command to be processed
 */
public abstract class CommandExecutor extends EventHandler<MessageEvent> {

    private CommandParser parser;

    @Inject protected Injector injector;
    @Inject protected Locale locale;
    @Inject protected @Named("commandPrefix") String commandPrefix;
    @Inject protected Logger logger;

    public final void handle(MessageEvent ev) throws PluginException {
        CommandParser parser = injector.getInstance(CommandParser.class);
        Command cmd = parser.parse(ev.getMessage().text());
        String annotatedCommand = this.getClass().getAnnotation(net.sadovnikov.marvinbot.core.annotations.Command.class).value();
        Role requiredRole = new UserRole();
        RequiredRole roleAnnotation = this.getClass().getAnnotation(RequiredRole.class);
        if (roleAnnotation != null) {
            Class<? extends Role> requiredRoleClass = roleAnnotation.value();
            try {
                requiredRole = requiredRoleClass.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                logger.catching(e);
            }
        }
        PermissionChecker permissionChecker = injector.getInstance(PermissionChecker.class);
        boolean userHasRequiredRole = permissionChecker.checkPermissionsByMessage(ev.getMessage(), requiredRole);
        boolean commandDetected = cmd != null && annotatedCommand != null && cmd.getCommand().equals(annotatedCommand);
        if (commandDetected && userHasRequiredRole) {
            execute(cmd, ev);
        } else if (commandDetected && !userHasRequiredRole) {
            MessageSenderService messageSenderService = injector.getInstance(MessageSenderService.class);
            ResourceBundle locData = ResourceBundle.getBundle("loc_data_main", locale);
            String notEnoughPermissionsMessage = locData.getString("notEnoughPermissionsMessageToExecuteCommand");
            try {
                messageSenderService.reply(ev.getMessage(), notEnoughPermissionsMessage);
            } catch (MessageSenderException e) {
                logger.catching(e);
            }
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
