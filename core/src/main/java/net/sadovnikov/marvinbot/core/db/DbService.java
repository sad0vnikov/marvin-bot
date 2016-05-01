package net.sadovnikov.marvinbot.core.db;

import com.google.inject.name.Named;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class DbService {

    JedisPool pool;

    public DbService(@Named("redisHost") String redisHost) {
        pool = new JedisPool(new JedisPoolConfig(), redisHost);
    }

    public Jedis getConnection() {
        return pool.getResource();
    }

    public <T> T exec(DbCommand<T> cmd) throws DbException {
        try (Jedis conn = getConnection()) {
            Executor executor = new Executor(conn);
            T value = cmd.execute(executor);

            return value;
        }

    }
}
