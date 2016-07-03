package net.sadovnikov.marvinbot.core.service.permissions;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.sadovnikov.marvinbot.core.domain.message.ReceivedMessage;
import net.sadovnikov.marvinbot.core.domain.user.AdminRole;
import net.sadovnikov.marvinbot.core.domain.user.ChatModeratorRole;
import net.sadovnikov.marvinbot.core.domain.user.Role;
import net.sadovnikov.marvinbot.core.domain.user.UserRole;

/**
 * Checks net.sadovnikov.marvinbot.core.domain.user permission level
 */
public abstract class PermissionChecker {

    protected String[] adminsList;

    @Inject
    public PermissionChecker(@Named("admins") String admins) {
        adminsList = admins.split(",");

        for (int i=0; i<adminsList.length; i++) {
            adminsList[i] = adminsList[i].trim();
        }
    }

    public boolean checkIsAdmin(String usernameToCheck) {
        for (String username : adminsList) {
            if (username.equals(usernameToCheck)) {
                return true;
            }
        }

        return false;
    }

    public boolean checkPermissionsByMessage(ReceivedMessage message, Role requiredRole)
    {
        String username = message.senderUsername();
        boolean isAdmin = checkIsAdmin(username);


        if (isAdmin && requiredRole.lte(AdminRole.class)) {
            return true;
        }

        boolean isChatModerator = checkIsChatModerator(message);
        if (isChatModerator && requiredRole.lte(ChatModeratorRole.class)) {
            return true;
        }

        boolean isUser = checkIsUser(username);
        if (isUser && requiredRole.lte(UserRole.class)) {
            return true;
        }

        return false;
    }

    public boolean checkIsUser(String username) {
        return true;
    }

    public boolean checkIsMessageSentByAdmin(ReceivedMessage message) {
        String username =  message.senderUsername();
        return checkIsAdmin(username);
    }

    public boolean checkIsChatModerator(ReceivedMessage message) {
        return checkIsChatModerator(message.senderUsername(), message.chatId());
    }

    public abstract boolean checkIsChatModerator(String username, String chatId);
}
