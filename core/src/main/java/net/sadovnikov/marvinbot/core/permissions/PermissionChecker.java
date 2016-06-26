package net.sadovnikov.marvinbot.core.permissions;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import net.sadovnikov.marvinbot.core.message.ReceivedMessage;

/**
 * Checks user permission level
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

    public boolean checkPermissionsByMessage(ReceivedMessage message, int requiredRole)
    {
        String username = message.getSenderUsername();
        boolean isAdmin = checkIsAdmin(username);
        if (isAdmin && requiredRole <= Role.ADMIN) {
            return true;
        }

        boolean isChatModerator = checkIsChatModerator(message);
        if (isChatModerator && requiredRole <= Role.CHAT_MODERATOR) {
            return true;
        }

        boolean isUser = checkIsUser(username);
        if (isUser && requiredRole == Role.USER) {
            return true;
        }

        return false;
    }

    public boolean checkIsUser(String username) {
        return true;
    }

    public boolean checkIsMessageSentByAdmin(ReceivedMessage message) {
        String username =  message.getSenderUsername();
        return checkIsAdmin(username);
    }

    public boolean checkIsChatModerator(ReceivedMessage message) {
        return checkIsChatModerator(message.getSenderUsername(), message.getChatId());
    }

    public abstract boolean checkIsChatModerator(String username, String chatId);
}
