package net.sadovnikov.marvinbot.core.domain.user;

public class User {

    protected String username;
    protected Role role;

    public User(String username, Role role) {
        this.username = username;
        this.role = role;
    }

    public String username() {
        return this.username;
    }

    public Role role() {
        return role;
    }

    public boolean equals(User user) {
        return user.username().equals(this.username) && user.role().equals(this.role);
    }

}
