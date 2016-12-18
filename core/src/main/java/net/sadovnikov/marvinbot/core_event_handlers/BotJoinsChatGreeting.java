package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.domain.message.MessageToSend;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.BotJoinedChatEvent;
import net.sadovnikov.marvinbot.core.misc.Utf8Control;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderService;
import net.sadovnikov.marvinbot.core.service.message.MessageSenderException;
import org.apache.logging.log4j.Logger;
import ro.fortsoft.pf4j.Extension;

import java.util.Locale;
import java.util.ResourceBundle;

@Extension
public class BotJoinsChatGreeting extends EventHandler<BotJoinedChatEvent> {

    private MessageSenderService messageSenderService;
    private ResourceBundle locData;
    private Logger logger;

    @Inject
    public BotJoinsChatGreeting(MessageSenderService messageSenderService, Locale locale, Logger logger) {
        this.messageSenderService = messageSenderService;
        this.locData = ResourceBundle.getBundle("loc_data_main", locale, new Utf8Control());
        this.logger  = logger;
    }

    public void handle(BotJoinedChatEvent ev) {
        String greeetingText = locData.getString("chatGreeting");
        MessageToSend message = new MessageToSend(greeetingText, ev.chat());

        try {
            messageSenderService.send(message);
        } catch (MessageSenderException e) {
            logger.catching(e);
        }
    }
}
