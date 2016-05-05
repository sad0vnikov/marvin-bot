package net.sadovnikov.marvinbot.plugins;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.message.SentMessage;
import net.sadovnikov.marvinbot.core.message_sender.MessageSender;
import net.sadovnikov.marvinbot.core.plugin.Plugin;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;



public class EchoPlugin extends Plugin {

    @Inject MessageSender messageSender;

    public EchoPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public class MessageHandler extends EventHandler<MessageEvent> {

        public void handle(MessageEvent ev) {
            SentMessage message = new SentMessage();
            message.setText(ev.getMessage().getText());
            message.setRecepientId(ev.getMessage().getChatId());

            messageSender.sendMessage(message);
        }
    }

}
