package net.sadovnikov.marvinbot.botframework;

import static junit.framework.Assert.*;

import com.google.inject.Inject;
import net.sadovnikov.marvinbot.TestBase;
import net.sadovnikov.marvinbot.core.db.DbService;
import net.sadovnikov.marvinbot.core.db.repository.ActiveChatsRepository;
import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.domain.ChannelTypes;
import net.sadovnikov.marvinbot.core.events.event_types.BotJoinedChatEvent;
import net.sadovnikov.marvinbot.core.events.event_types.BotLeftChatEvent;
import net.sadovnikov.marvinbot.core.exceptions.SkypeApiException;
import net.sadovnikov.marvinbot.core.service.chat.AbstractChat;
import net.sadovnikov.marvinbot.core.service.chat.Chat;
import net.sadovnikov.marvinbot.core.service.chat.GroupChat;
import net.sadovnikov.marvinbot.core.service.contact.BotFrameworkContactManager;
import net.sadovnikov.marvinbot.core.service.contact.CachingContactManager;
import org.junit.Test;

import java.util.Collection;

public class CachingContactManagerTest extends TestBase {

    @Test
    public void testSavingActiveChat() throws SkypeApiException {
        ActiveChatsRepository chatsRep = injector.getInstance(ActiveChatsRepository.class);
        CachingContactManager contactManager = new BotFrameworkContactManager(chatsRep);

        Chat firstChat = new Chat(new Channel(ChannelTypes.SKYPE), "chat_1");
        GroupChat secondChat = new GroupChat(new Channel(ChannelTypes.SKYPE), "chat_2");

        contactManager.setAsActive(firstChat);
        contactManager.setAsActive(secondChat);

        Collection<AbstractChat> activeChats = contactManager.getAllChats();
        assertTrue(activeChats.contains(firstChat));
        assertTrue(activeChats.contains(secondChat));

        Collection<AbstractChat> groupChats = contactManager.getGroupChats();
        assertFalse(groupChats.contains(firstChat));
        assertTrue(groupChats.contains(secondChat));

        contactManager.removeFromActive(secondChat);
        assertTrue(contactManager.getGroupChats().isEmpty());
        assertTrue(contactManager.getAllChats().contains(firstChat));
        assertFalse(contactManager.getAllChats().contains(secondChat));

        contactManager.removeFromActive(firstChat);
        assertTrue(contactManager.getAllChats().isEmpty());
        assertTrue(contactManager.getGroupChats().isEmpty());

    }

    /**
     * Active chats list should be renewed on BotJoinedChatEvent and BotLeftChatEvent
     * @throws SkypeApiException
     */
    @Test
    public void testCachingChatsOnEvents() throws SkypeApiException {
        FakeBotframeworkEventDispatcher eventDispatcher = new FakeBotframeworkEventDispatcher(
                new MockedBot(),
                injector
        );
        injector.injectMembers(eventDispatcher);
        ActiveChatsRepository chatsRep = injector.getInstance(ActiveChatsRepository.class);
        CachingContactManager contactManager = new BotFrameworkContactManager(chatsRep);

        Chat chat = new Chat(new Channel(ChannelTypes.SKYPE), "chat_1");
        eventDispatcher.dispatch(new BotJoinedChatEvent(chat, null));
        Collection<AbstractChat> chats = contactManager.getAllChats();
        assertTrue(chats.contains(chat));

        eventDispatcher.dispatch(new BotLeftChatEvent(chat, null));
        assertFalse(contactManager.getAllChats().contains(chat));
    }
}
