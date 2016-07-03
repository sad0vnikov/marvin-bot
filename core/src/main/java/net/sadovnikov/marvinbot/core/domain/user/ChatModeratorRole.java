package net.sadovnikov.marvinbot.core.domain.user;

public class ChatModeratorRole extends Role {

    private final int value = 1;
    private final String name = "moderator";

    public int value() {
        return value;
    }

    public String name() {
        return name;
    }

}
