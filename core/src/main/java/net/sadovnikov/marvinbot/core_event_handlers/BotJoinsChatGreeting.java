package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.BotJoinedChatEvent;
import net.sadovnikov.marvinbot.core.domain.message.SentMessage;
import net.sadovnikov.marvinbot.core.service.message_sender.MessageSender;
import net.sadovnikov.marvinbot.core.service.message_sender.MessageSenderException;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.Extension;

import java.util.Locale;
import java.util.ResourceBundle;

@Extension
public class BotJoinsChatGreeting extends EventHandler<BotJoinedChatEvent> {

    private MessageSender messageSender;
    private ResourceBundle locData;
    private Logger logger;

    @Inject
    public BotJoinsChatGreeting(MessageSender messageSender, Locale locale, Logger logger) {
        this.messageSender = messageSender;
        this.locData = ResourceBundle.getBundle("loc_data_main", locale);
        this.logger  = logger;
    }

    public void handle(BotJoinedChatEvent ev) {
        String chatId = ev.getChatId();
        String greeetingText = locData.getString("chatGreeting");
        MessageToSend message = new MessageToSend(greeetingText, chatId);

        try {
            messageSender.sendMessage(message);
        } catch (MessageSenderException e) {
            logger.catching(e);
        }
    }
}
