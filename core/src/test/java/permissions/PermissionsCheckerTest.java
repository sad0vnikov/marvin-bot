package permissions;


import net.sadovnikov.marvinbot.core.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.permissions.PermissionChecker;
import net.sadovnikov.marvinbot.core.permissions.Role;
import net.sadovnikov.marvinbot.core.permissions.StubbedPermissionChecker;
import org.junit.Test;

import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.assertFalse;

public class PermissionsCheckerTest {


    @Test
    public void testRolesDetection()
    {
        String username = "user";
        String adminName = "admin";
        String moderatorName = "moderator";
        String notUserName = "notUser";

        String chatId   = "chat";

        ReceivedMessage message = new ReceivedMessage(chatId, username, "msgText");
        ReceivedMessage adminMessage = new ReceivedMessage(chatId, adminName, "msgText");
        ReceivedMessage moderatorMessage = new ReceivedMessage(chatId, moderatorName, "msgText");
        ReceivedMessage notUserMessage = new ReceivedMessage(chatId, notUserName, "msgText");


        PermissionChecker checker = new StubbedPermissionChecker(adminName);

        assertTrue(checker.checkPermissionsByMessage(adminMessage, Role.ADMIN));
        assertTrue(checker.checkPermissionsByMessage(adminMessage, Role.CHAT_MODERATOR));
        assertTrue(checker.checkPermissionsByMessage(adminMessage, Role.USER));

        assertFalse(checker.checkPermissionsByMessage(moderatorMessage, Role.ADMIN));
        assertTrue(checker.checkPermissionsByMessage(moderatorMessage, Role.CHAT_MODERATOR));
        assertTrue(checker.checkPermissionsByMessage(moderatorMessage, Role.USER));

        assertFalse(checker.checkPermissionsByMessage(message, Role.ADMIN));
        assertFalse(checker.checkPermissionsByMessage(message, Role.CHAT_MODERATOR));
        assertTrue(checker.checkPermissionsByMessage(message, Role.USER));

        assertFalse(checker.checkPermissionsByMessage(notUserMessage, Role.ADMIN));
        assertFalse(checker.checkPermissionsByMessage(notUserMessage, Role.CHAT_MODERATOR));
        assertFalse(checker.checkPermissionsByMessage(notUserMessage, Role.USER));
    }

}

