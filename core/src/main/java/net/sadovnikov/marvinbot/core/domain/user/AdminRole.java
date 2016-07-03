package net.sadovnikov.marvinbot.core.domain.user;


public class AdminRole extends Role {

    private final int value = 2;
    private final String name = "admin";

    public int value() {
        return value;
    }

    public String name() {
        return name;
    }

}
