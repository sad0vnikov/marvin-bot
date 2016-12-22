package net.sadovnikov.marvinbot.core_event_handlers;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.core.events.EventHandler;
import net.sadovnikov.marvinbot.core.events.event_types.BotJoinedChatEvent;
import net.sadovnikov.marvinbot.core.events.event_types.BotLeftChatEvent;
import net.sadovnikov.marvinbot.core.plugin.PluginException;
import net.sadovnikov.marvinbot.core.service.chat.Chat;
import net.sadovnikov.marvinbot.core.service.contact.CachingContactManager;
import ro.fortsoft.pf4j.Extension;

/**
 * In Microsoft Bot Framework Bot's username is different for every channel and we can't know it advance.
 * So, we'll try to get it from RECEPIENT field of message.
 * Than the bot needs to remember he belongs to this chat, since there is not way to get active chats list from API
 */
public class ChatCachingOnChatEvents {

    @Extension
    public static class SaveChatOnJoin extends EventHandler<BotJoinedChatEvent> {

        private CachingContactManager contactManager;

        @Inject
        public SaveChatOnJoin(CachingContactManager contactManager) {
            this.contactManager = contactManager;
        }


        @Override
        public void handle(BotJoinedChatEvent ev) throws PluginException {
            contactManager.setAsActive(new Chat(ev.channel(), ev.getChatId()));
        }
    }

    @Extension
    public static class RemoveChatOnLeave extends EventHandler<BotLeftChatEvent> {

        private CachingContactManager contactManager;

        @Inject
        public RemoveChatOnLeave(CachingContactManager contactManager) {
            this.contactManager = contactManager;
        }

        @Override
        public void handle(BotLeftChatEvent ev) throws PluginException {
            contactManager.removeFromActive(new Chat(ev.channel(), ev.getChatId()));
        }
    }
}
