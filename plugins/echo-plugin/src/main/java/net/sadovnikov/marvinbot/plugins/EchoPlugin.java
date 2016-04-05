package net.sadovnikov.marvinbot.plugins;

import com.google.inject.Inject;
import ro.fortsoft.pf4j.Plugin;
import net.sadovnikov.marvinbot.core.events.eventHandlers.MessageEventHandler;
import net.sadovnikov.marvinbot.core.events.eventTypes.MessageEvent;
import net.sadovnikov.marvinbot.core.message.SentMessage;
import net.sadovnikov.marvinbot.core.messageSender.MessageSender;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;



public class EchoPlugin extends Plugin {

    public EchoPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class MessageHandler extends MessageEventHandler {

        MessageSender messageSender;
        @Inject
        public MessageHandler(MessageSender messageSender) {
            this.messageSender = messageSender;
        }

        public void handle(MessageEvent ev) {
            SentMessage message = new SentMessage();
            message.setText(ev.getMessage().getText());
            message.setRecepientId(ev.getMessage().getChatId());

            messageSender.sendMessage(message);
        }
    }


}
