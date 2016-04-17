package net.sadovnikov.marvinbot.plugins;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import ro.fortsoft.pf4j.Plugin;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.message.SentMessage;
import net.sadovnikov.marvinbot.core.message_sender.MessageSender;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;



public class EchoPlugin extends Plugin {

    public EchoPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class MessageHandler extends EventHandler<MessageEvent> {

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
