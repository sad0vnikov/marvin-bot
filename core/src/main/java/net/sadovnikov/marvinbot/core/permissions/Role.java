package net.sadovnikov.marvinbot.core.permissions;


import com.samczsun.skype4j.user.User;

public class Role {

    /**
     * These are roles ordered by priority
     * The higher roles include the lower ones (e.g. ADMIN can do everything CHAT_MODERATOR can)
     */
    public static final int ADMIN = 3;
    public static final int CHAT_MODERATOR = 2;
    public static final int USER = 1;


}
