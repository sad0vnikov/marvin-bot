package net.sadovnikov.marvinbot.core.db;

import com.google.inject.Inject;
import redis.clients.jedis.Jedis;

import java.util.Map;
import java.util.Set;

public class Executor {

    private Jedis conn;

    public Executor(Jedis conn) {
        this.conn = conn;
    }

    public void set(String key, String value) {
        conn.set(key, value);
    }

    public String get(String key) {
        return conn.get(key);
    }

    public Set<String> keys(String pattern) {
        return conn.keys(pattern);
    }

    public void del(String key) {
        conn.del(key);
    }

    public long incr(String key) {
        return conn.incr(key);
    }

    public void hdel(String hash, String key) {
        conn.hdel(hash, key);
    }

    public long hincrBy(String hash, String key, long inc) {
        return conn.hincrBy(hash, key, inc);
    }

    public void hset(String hash, String key, String value) {
        conn.hset(hash, key, value);
    }

    public void hmset(String hash, Map<String,String> values) {
        conn.hmset(hash, values);
    }


    public String hget(String hash, String key) {
        return conn.hget(hash, key);
    }

    public Map<String,String> hgetAll(String hash) {
        return conn.hgetAll(hash);
    }


}
