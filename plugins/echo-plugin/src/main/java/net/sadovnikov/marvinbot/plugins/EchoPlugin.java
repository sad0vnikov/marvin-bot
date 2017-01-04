package net.sadovnikov.marvinbot.plugins;

import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.service.CommandExecutor;
import net.sadovnikov.marvinbot.core.annotations.Command;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.domain.user.ChatModeratorRole;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;



public class EchoPlugin extends Plugin {

    protected final Logger logger = LogManager.getLogger("core-logger");

    public EchoPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public class MessageHandler extends EventHandler<MessageEvent> {

        public void handle(MessageEvent ev) throws PluginException {

            AbstractChat chat  = ev.getMessage().chat();
            String msgText = ev.getMessage().text();
            try {
                boolean isEchoingOn = marvin.pluginOptions().chat(chat).get("enableEcho", "off").equals("on");
                boolean isCommand   = ev.getMessage().isCommand(); // is message a valid bot command
                if (isEchoingOn && !isCommand) {
                    MessageToSend message = new MessageToSend(msgText, chat);
                    marvin.message().send(message);
                }
            } catch (DbException | MessageSenderException e) {
                logger.catching(e);
            }


        }
    }

    @Command("echo")
    @Extension
    public class SwitcherCommand extends CommandExecutor {


        @Override
        public void execute(net.sadovnikov.marvinbot.core.domain.Command cmd, MessageEvent ev) throws PluginException {
            String[] args = cmd.getArgs();

            if (args.length == 0) {
                return;
            }
            AbstractChat chat = ev.getMessage().chat();

            try {
                if ( args[0].equals("on")) {
                    marvin.pluginOptions().chat(chat).set("enableEcho", "on");
                    marvin.message().reply(ev.getMessage(), getLocaleBundle().getString("echoTurnedOn"));
                } else if ( args[0].equals("off")) {
                    marvin.pluginOptions().chat(chat).set("enableEcho", "off");
                    marvin.message().reply(ev.getMessage(), getLocaleBundle().getString("echoTurnedOff"));
                }
            } catch (MessageSenderException | DbException e) {
                logger.catching(e);
            }


        }

        public String getHelp() {
            return getLocaleBundle().getString("helpString");
        }

    }


}
