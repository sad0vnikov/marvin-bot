package net.sadovnikov.marvinbot.plugins;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.command.CommandExecutor;
import net.sadovnikov.marvinbot.core.command.CommandParser;
import net.sadovnikov.marvinbot.core.command.annotations.Command;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.message.SentMessage;
import net.sadovnikov.marvinbot.core.message_sender.MessageSender;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;



public class EchoPlugin extends Plugin {

    @Inject protected MessageSender messageSender;
    protected final Logger logger = LogManager.getLogger("core-logger");

    public EchoPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public class MessageHandler extends EventHandler<MessageEvent> {

        public void handle(MessageEvent ev) throws PluginException {

            CommandParser commandParser = injector.getInstance(CommandParser.class);
            boolean isEchoingOn = getGlobalOption("enableEcho", "off").equals("on");
            boolean isCommand   = commandParser.getIsCommand(ev.getMessage()); // is message a valid bot command
            if (isEchoingOn && !isCommand) {
                SentMessage message = new SentMessage();
                message.setText(ev.getMessage().getText());
                message.setRecepientId(ev.getMessage().getChatId());

                messageSender.sendMessage(message);
            }

        }
    }

    @Command("echo")
    @Extension
    public class SwitcherCommand extends CommandExecutor {


        @Override
        public void execute(net.sadovnikov.marvinbot.core.command.Command cmd, MessageEvent ev) throws PluginException {
            String[] args = cmd.getArgs();

            if (args.length == 0) {
                return;
            }

            if ( args[0].equals("on")) {
                setGlobalOption("enableEcho", "on");
                messageSender.reply(ev.getMessage(), "Echoing is turned on!");
            } else if ( args[0].equals("off")) {
                setGlobalOption("enableEcho", "off");
                messageSender.reply(ev.getMessage(), "Echoing is turned off");
            }
        }
    }

}
