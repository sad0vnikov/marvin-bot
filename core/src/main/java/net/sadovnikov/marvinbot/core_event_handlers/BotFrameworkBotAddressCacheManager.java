package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import com.google.inject.Injector;
import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.domain.ChannelTypes;
import net.sadovnikov.marvinbot.core.service.botframework.BotSelfAddressCachingService;
import net.sadovnikov.marvinbot.core.service.client.BotFrameworkClient;
import net.sadovnikov.mbf4j.activities.incoming.IncomingActivity;
import net.sadovnikov.mbf4j.events.EventTypes;

public class BotFrameworkBotAddressCacheManager {

    private Injector injector;

    @Inject
    public BotFrameworkBotAddressCacheManager(Injector injector) {
        this.injector = injector;
    }

    public void initCaching(BotFrameworkClient bfClient) {

        bfClient.getBotInstance().on(EventTypes.EVENT_TYPE_CONVERSATION_UPDATE, this::cacheBotId);
        bfClient.getBotInstance().on(EventTypes.EVENT_TYPE_INCOMING_MESSAGE, this::cacheBotId);
        bfClient.getBotInstance().on(EventTypes.EVENT_TYPE_CONTACT_RELACTION, this::cacheBotId);
    }

    protected void cacheBotId(IncomingActivity ev) {
        BotSelfAddressCachingService cachingService = injector.getInstance(BotSelfAddressCachingService.class);

        ChannelTypes channelType = ChannelTypes.valueOf(ev.channel().type().name());
        Channel channel = new Channel(channelType);
        cachingService.saveBotAddressForChannel(channel, ev.recipient());
    }
}
