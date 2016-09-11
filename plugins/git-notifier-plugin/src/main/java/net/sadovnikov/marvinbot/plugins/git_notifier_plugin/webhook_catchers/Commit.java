package net.sadovnikov.marvinbot.plugins.git_notifier_plugin.webhook_catchers;


public class Commit {

    private String hash;
    private String user;
    private String message;


    public Commit(String hash, String user, String message) {
        this.hash = hash;
        this.user = user;
        this.message = message;
    }

    public String getHash() {
        return hash;
    }

    public String getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
