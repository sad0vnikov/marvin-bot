package net.sadovnikov.marvinbot.core.service.permissions;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class BotFrameworkPermissionChecker extends PermissionChecker {

    @Inject
    public BotFrameworkPermissionChecker(@Named("admins") String admins) {
        super(admins);
    }

    @Override
    public boolean checkIsChatModerator(String username, String chatId) {
        // there is no way to know if user is a chat owner for now
        //using BotFramework REST Api
        return false;
    }
}
