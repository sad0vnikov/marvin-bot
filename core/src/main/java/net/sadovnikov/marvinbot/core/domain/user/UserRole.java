package net.sadovnikov.marvinbot.core.domain.user;

public class UserRole extends Role {

    private final int value = 0;
    private final String name = "user";

    public int value() {
        return value;
    }

    public String name() {
        return name;
    }

}
