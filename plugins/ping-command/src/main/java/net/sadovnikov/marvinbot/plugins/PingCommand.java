package net.sadovnikov.marvinbot.plugins;


import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.service.CommandExecutor;
import net.sadovnikov.marvinbot.core.annotations.RequiredRole;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.domain.user.UserRole;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;

import java.util.Locale;
import java.util.ResourceBundle;

public class  PingCommand extends Plugin {

    protected Locale locale;
    protected Logger logger;

    public PingCommand(PluginWrapper wrapper) {
        super(wrapper);
    }



    @net.sadovnikov.marvinbot.core.annotations.Command("ping")
    @Extension
    @RequiredRole(UserRole.class)
    public class PingCommandHandler extends CommandExecutor {

        private ResourceBundle locData;


        @Override
        public void execute(Command cmd, MessageEvent ev) throws PluginException {
            String msgText = "pong";
            ReceivedMessage receivedMessage = ev.getMessage();
            try {
                marvin.message().reply(receivedMessage, msgText);
            } catch (MessageSenderException e) {
                logger.catching(e);
            }
        }

        public String getHelp() {
            locData = getLocaleBundle();
            return locData.getString("helpString");
        }

        public String getUsage() {
            return commandPrefix + "ping";
        }
    }
}
