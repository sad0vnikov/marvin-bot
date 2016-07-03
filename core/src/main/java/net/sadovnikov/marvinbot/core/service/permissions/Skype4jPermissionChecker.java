package net.sadovnikov.marvinbot.core.service.permissions;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.samczsun.skype4j.Skype;
import com.samczsun.skype4j.chat.Chat;
import com.samczsun.skype4j.exceptions.ChatNotFoundException;
import com.samczsun.skype4j.exceptions.ConnectionException;
import com.samczsun.skype4j.user.User;
import org.apache.logging.log4j.LogManager;

public class Skype4jPermissionChecker extends PermissionChecker {

    private Skype skype;

    @Inject
    public Skype4jPermissionChecker(@Named("admins") String admins, Skype skype) {
        super(admins);
        this.skype = skype;
    }

    @Override
    public boolean checkIsChatModerator(String username, String chatId) {

        try {
            Chat chat = skype.getOrLoadChat(chatId);
            User.Role userRole = chat.getUser(username).getRole();
            return userRole == User.Role.ADMIN;
        } catch (ConnectionException | ChatNotFoundException e) {
            LogManager.getLogger("core-logger").catching(e);
        }

        return false;
    }
}
