package net.sadovnikov.marvinbot.plugins;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.sadovnikov.marvinbot.core.command.Command;
import net.sadovnikov.marvinbot.core.command.CommandExecutor;
import net.sadovnikov.marvinbot.core.command.annotations.RequiredRole;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.message_sender.MessageSender;
import net.sadovnikov.marvinbot.core.permissions.Role;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;

import java.util.Locale;
import java.util.ResourceBundle;

public class  PingCommand extends Plugin {

    protected MessageSender messageSender;
    protected Locale locale;

    public PingCommand(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Inject
    public void setLocale(Locale locale) {
        this.locale = locale;

    }

    @Inject
    public void setMessageSender(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @net.sadovnikov.marvinbot.core.command.annotations.Command("ping")
    @Extension
    @RequiredRole(Role.USER)
    public class PingCommandHandler extends CommandExecutor {

        private ResourceBundle locData;


        @Override
        public void execute(Command cmd, MessageEvent ev) throws PluginException {
            String msgText = "pong";
            ReceivedMessage receivedMessage = ev.getMessage();
            messageSender.reply(receivedMessage, msgText);
        }

        public String getHelp() {
            locData = ResourceBundle.getBundle("loc_data_main", locale);
            return locData.getString("helpString");
        }

        public String getUsage() {
            return "Usage: " + commandPrefix + "ping";
        }
    }
}
