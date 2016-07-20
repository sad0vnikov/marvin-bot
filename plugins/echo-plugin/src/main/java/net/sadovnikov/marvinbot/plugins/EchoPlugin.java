package net.sadovnikov.marvinbot.plugins;

import net.sadovnikov.marvinbot.core.db.DbException;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.service.CommandExecutor;
import net.sadovnikov.marvinbot.core.service.CommandParser;
import net.sadovnikov.marvinbot.core.annotations.Command;
import net.sadovnikov.marvinbot.core.annotations.RequiredRole;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.domain.user.ChatModeratorRole;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
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

            CommandParser commandParser = injector.getInstance(CommandParser.class);
            String chatId  = ev.getMessage().chatId();
            String msgText = ev.getMessage().text();
            try {
                boolean isEchoingOn = marvin.pluginOptions().chat(chatId).get("enableEcho", "off").equals("on");
                boolean isCommand   = commandParser.getIsCommand(ev.getMessage()); // is net.sadovnikov.marvinbot.core.domain.message a valid bot command
                if (isEchoingOn && !isCommand) {
                    MessageToSend message = new MessageToSend(msgText, chatId);
                    marvin.message().send(message);
                }
            } catch (DbException | MessageSenderException e) {
                logger.catching(e);
            }


        }
    }

    @Command("echo")
    @RequiredRole(ChatModeratorRole.class)
    @Extension
    public class SwitcherCommand extends CommandExecutor {


        @Override
        public void execute(net.sadovnikov.marvinbot.core.domain.Command cmd, MessageEvent ev) throws PluginException {
            String[] args = cmd.getArgs();

            if (args.length == 0) {
                return;
            }
            String chatId = ev.getMessage().chatId();

            try {
                if ( args[0].equals("on")) {
                    marvin.pluginOptions().chat(chatId).set("enableEcho", "on");
                    marvin.message().reply(ev.getMessage(), "Echoing is turned on!");
                } else if ( args[0].equals("off")) {
                    marvin.pluginOptions().chat(chatId).set("enableEcho", "off");
                    marvin.message().reply(ev.getMessage(), "Echoing is turned off");
                }
            } catch (MessageSenderException | DbException e) {
                logger.catching(e);
            }


        }
    }

}
