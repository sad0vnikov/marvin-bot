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

    public Void set(String key, String value) {
        conn.set(key, value);
        return (Void) null;
    }

    public String get(String key) {
        return conn.get(key);
    }

    public Set<String> keys(String pattern) {
        return conn.keys(pattern);
    }

    public Void del(String key) {
        conn.del(key);
        return (Void) null;
    }

    public long incr(String key) {
        return conn.incr(key);
    }

    public Void hdel(String hash, String key) {
        conn.hdel(hash, key);
        return (Void) null;
    }

    public long hincrBy(String hash, String key, long inc) {
        return conn.hincrBy(hash, key, inc);
    }

    public Void hset(String hash, String key, String value) {
        conn.hset(hash, key, value);
        return (Void) null;
    }

    public Void hmset(String hash, Map<String,String> values) {
        conn.hmset(hash, values);
        return (Void) null;
    }


    public String hget(String hash, String key) {
        return conn.hget(hash, key);
    }

    public Map<String,String> hgetAll(String hash) {
        return conn.hgetAll(hash);
    }


}
