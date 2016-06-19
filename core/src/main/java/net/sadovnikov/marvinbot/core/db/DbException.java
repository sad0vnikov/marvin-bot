package net.sadovnikov.marvinbot.core.db;

public class DbException extends Exception {

    public DbException(Throwable e) {
        super(e);
    }

    public DbException(String message) {
        super(message);
    }

}
