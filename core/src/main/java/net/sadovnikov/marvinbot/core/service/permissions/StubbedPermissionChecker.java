package net.sadovnikov.marvinbot.core.service.permissions;

public class StubbedPermissionChecker extends PermissionChecker {

    public StubbedPermissionChecker(String admins) {
        super(admins);
    }

    @Override
    public boolean checkIsChatModerator(String username, String chatId) {
        if (username.equals("moderator")) {
            return true;
        }

        return false;
    }

    @Override
    public boolean checkIsUser(String username) {
        if (username.equals("notUser")) {
            return false;
        }
        return true;
    }


}
