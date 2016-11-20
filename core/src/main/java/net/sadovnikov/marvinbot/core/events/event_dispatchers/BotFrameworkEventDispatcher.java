package net.sadovnikov.marvinbot.core.events.event_dispatchers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.domain.Command;
import net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.events.EventDispatcher;
import net.sadovnikov.marvinbot.core.events.event_types.MessageEvent;
import net.sadovnikov.marvinbot.core.service.CommandParser;
import net.sadovnikov.mbf4j.Bot;
import net.sadovnikov.mbf4j.activities.incoming.IncomingMessage;
import net.sadovnikov.mbf4j.events.EventTypes;

public class BotFrameworkEventDispatcher extends EventDispatcher {

    protected Bot bot;
    protected Injector injector;

    @Inject
    public BotFrameworkEventDispatcher(Bot bot, Injector injector) {
        this.bot = bot;
        this.injector = injector;
    }

    public void run() {

        bot.on(EventTypes.EVENT_TYPE_INCOMING_MESSAGE, (IncomingMessage event) -> {
            String chatId = event.conversation().id();
            String userName = event.from().name();
            String text = event.text().get();

            CommandParser parser = injector.getInstance(CommandParser.class);
            Command cmd = parser.parse(text);

            ReceivedMessage message = new ReceivedMessage(chatId, userName, text, cmd);
            dispatch(new MessageEvent(message));
        });

    }
}
