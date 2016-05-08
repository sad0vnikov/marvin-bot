package net.sadovnikov.marvinbot.core.permissions;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Checks user permission level
 */
public class PermissionChecker {

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
}
