package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.BotJoinedChatEvent;
import net.sadovnikov.marvinbot.core.message.SentMessage;
import net.sadovnikov.marvinbot.core.message_sender.MessageSender;
import ro.fortsoft.pf4j.Extension;

import java.util.Locale;
import java.util.ResourceBundle;

@Extension
public class BotJoinsChatGreeting extends EventHandler<BotJoinedChatEvent> {

    private MessageSender messageSender;
    private ResourceBundle locData;

    @Inject
    public BotJoinsChatGreeting(MessageSender messageSender, Locale locale) {
        this.messageSender = messageSender;
        this.locData = ResourceBundle.getBundle("loc_data_main", locale);
    }

    public void handle(BotJoinedChatEvent ev) {
        SentMessage message = new SentMessage();
        message.setRecepientId(ev.getChatId());
        String greeetingText = locData.getString("chatGreeting");


        message.setText(greeetingText);
        messageSender.sendMessage(message);
    }
}
