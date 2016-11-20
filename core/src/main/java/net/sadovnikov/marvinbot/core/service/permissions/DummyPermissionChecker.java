package net.sadovnikov.marvinbot.core.service.permissions;

import com.google.inject.name.Named;

public class DummyPermissionChecker extends PermissionChecker {

    public DummyPermissionChecker(@Named("admins") String admins) {
        super(admins);
    }

    @Override
    public boolean checkIsChatModerator(String username, String chatId) {
        return false;
    }
}
