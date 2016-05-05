package net.sadovnikov.marvinbot.core.db;

public interface DbCommand<T> {

    public T execute(Executor executor) throws DbException;

}
