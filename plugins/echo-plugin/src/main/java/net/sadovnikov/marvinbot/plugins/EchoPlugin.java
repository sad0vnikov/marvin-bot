package net.sadovnikov.marvinbot.plugins;

import ro.fortsoft.pf4j.Plugin;
import events.eventHandlers.MessageEventHandler;
import events.eventTypes.MessageEvent;
import message.SentMessage;
import messageSender.MessageSender;
import ro.fortsoft.pf4j.Extension;
import ro.fortsoft.pf4j.PluginWrapper;



public class EchoPlugin extends Plugin {

    public EchoPlugin(PluginWrapper wrapper) {
        super(wrapper);
    }

    @Extension
    public static class MessageHandler extends MessageEventHandler {

        public void handle(MessageEvent ev) {
            MessageSender sender = this.getMessageSender();
            SentMessage message = new SentMessage();
            message.setText(ev.getMessage().getText());
            message.setRecepientId(ev.getMessage().getChatId());

            sender.sendMessage(message);
        }
    }


}
