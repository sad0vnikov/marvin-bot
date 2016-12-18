package net.sadovnikov.marvinbot.permissions;


import net.sadovnikov.marvinbot.core.domain.Channel;
import net.sadovnikov.marvinbot.core.domain.ChannelTypes;
import net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.domain.user.AdminRole;
import net.sadovnikov.marvinbot.core.domain.user.ChatModeratorRole;
import net.sadovnikov.marvinbot.core.domain.user.UserRole;
import net.sadovnikov.marvinbot.core.service.chat.Chat;
import net.sadovnikov.marvinbot.core.service.permissions.PermissionChecker;
import net.sadovnikov.marvinbot.core.service.permissions.StubbedPermissionChecker;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

public class PermissionsCheckerTest {


    @Test
    public void testRolesDetection()
    {
        String username = "net.sadovnikov.marvinbot.core.domain.user";
        String adminName = "admin";
        String moderatorName = "moderator";
        String notUserName = "notUser";

        String chatId   = "chat";
        Chat chat = new Chat(new Channel(ChannelTypes.SKYPE), chatId);

        ReceivedMessage message = new ReceivedMessage(chat, username, "msgText");
        ReceivedMessage adminMessage = new ReceivedMessage(chat, adminName, "msgText");
        ReceivedMessage moderatorMessage = new ReceivedMessage(chat, moderatorName, "msgText");
        ReceivedMessage notUserMessage = new ReceivedMessage(chat, notUserName, "msgText");


        PermissionChecker checker = new StubbedPermissionChecker(adminName);

        assertTrue(checker.checkPermissionsByMessage(adminMessage, new AdminRole()));
        assertTrue(checker.checkPermissionsByMessage(adminMessage, new ChatModeratorRole()));
        assertTrue(checker.checkPermissionsByMessage(adminMessage, new UserRole()));

        assertFalse(checker.checkPermissionsByMessage(moderatorMessage, new AdminRole()));
        assertTrue(checker.checkPermissionsByMessage(moderatorMessage, new ChatModeratorRole()));
        assertTrue(checker.checkPermissionsByMessage(moderatorMessage, new UserRole()));

        assertFalse(checker.checkPermissionsByMessage(message, new AdminRole()));
        assertFalse(checker.checkPermissionsByMessage(message, new ChatModeratorRole()));
        assertTrue(checker.checkPermissionsByMessage(message, new UserRole()));

        assertFalse(checker.checkPermissionsByMessage(notUserMessage, new AdminRole()));
        assertFalse(checker.checkPermissionsByMessage(notUserMessage, new ChatModeratorRole()));
        assertFalse(checker.checkPermissionsByMessage(notUserMessage, new UserRole()));
    }

}

