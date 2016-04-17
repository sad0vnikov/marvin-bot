package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.BotJoinedChatEvent;
import net.sadovnikov.marvinbot.core.message.SentMessage;
import net.sadovnikov.marvinbot.core.message_sender.MessageSender;
import ro.fortsoft.pf4j.Extension;

@Extension
public class BotJoinsChatGreeting extends EventHandler<BotJoinedChatEvent> {

    private MessageSender messageSender;

    @Inject
    public BotJoinsChatGreeting(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    public void handle(BotJoinedChatEvent ev) {
        SentMessage message = new SentMessage();
        message.setRecepientId(ev.getChatId());
        message.setText("Hello! I'm Marvin!");
        messageSender.sendMessage(message);
    }
}
