package com.tal.note_board.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 存储（操作字符串）
     * 无过期时间 慎用
     *
     * @param key
     * @param value
     */
    public void setObject(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {

        }
    }

    /**
     * 获取（操作字符串）
     *
     * @param key
     * @return
     */
    public Object getObject(String key) {
        Object res;
        try {
            res = redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            res = null;
        }
        return res;
    }

    /**
     * 存储hash
     *
     * @param key
     * @param hk
     * @param value
     */
    public void setHash(String key, String hk, Object value) {
        redisTemplate.opsForHash().put(key, hk, value);
    }

    /**
     * 获取hash
     *
     * @param key
     * @param hk
     * @return
     */
    public Object getHash(String key, String hk) {
        Object value = redisTemplate.opsForHash().get(key, hk);
        return value;
    }

    /**
     * 存储list
     *
     * @param key
     * @param value
     */
    public void setList(String key, Object value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 获取list
     *
     * @param key
     * @return
     */
    public Object getList(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    /**
     * 存储set
     *
     * @param key
     * @param value
     */
    public void setSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }

    /**
     * 获取set
     *
     * @param key
     * @return
     */
    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 存储zset
     *
     * @param key
     * @param value
     * @param score
     */
    public void setZset(String key, Object value, double score) {
        redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 获取zset
     *
     * @param key
     * @return
     */
    public Object getZset(String key) {
        return redisTemplate.opsForZSet().rangeWithScores(key, -1, -1);
    }

    /**
     * 设置缓存过期时间
     *
     * @param key   key
     * @param hours 小时数
     */
    public void setExpire(final String key, final int hours) {
        try {
            redisTemplate.expire(key, hours, TimeUnit.HOURS);
        } catch (Exception e) {

        }
    }

    /**
     * 原子操作setEx(字符串)
     * 存储+设置过期时间
     *
     * @param key
     * @param value
     * @param seconds
     */
    public void setEx(String key, Object value, int seconds) {
        try {
            redisTemplate.opsForValue().set(key, value, seconds, TimeUnit.SECONDS);
        } catch (Exception e) {

        }
    }


    /**
     * 获取字符串
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        String res;
        try {
            res = (String) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            res = null;
        }
        return res;
    }

    /**
     * 批量get
     *
     * @param keys
     * @return
     */
    public List multiGet(Collection<String> keys) {
        List res = new ArrayList();
        try {
            res = redisTemplate.opsForValue().multiGet(keys);
        } catch (Exception e) {
            for (int i = 0; i < keys.size(); ++i) {
                res.add(null);
            }
        }
        return res;
    }

    /**
     * 流水线读
     *
     * @param reqs
     * @return
     */
    public Map<String, Object> pipLineGet(List<String> reqs) {
        Map<String, Object> result = new HashMap<>();
        try {
            redisTemplate.executePipelined((RedisConnection connection) -> {
                for (String req : reqs) {
                    Object object = null;
                    object = redisTemplate.opsForValue().get(req);
                    result.put(req, object);
                }
                return null;
            });
        } catch (Exception e) {
            for (String req : reqs) {
                result.put(req, null);
            }
        }
        return result;
    }

    /**
     * 流水线写
     *
     * @param req
     * @param seconds
     */
    public void pipLineSet(Map<String, ?> req, Integer seconds) {
        try {
            redisTemplate.executePipelined((RedisConnection connection) -> {
                req.forEach((k, v) -> {
                    setEx(k, v, seconds);
                });
                return null;
            });
        } catch (Exception e) {

        }
    }

    /**
     * 流水线删
     *
     * @param keyList
     */
    public void pipLineDelete(List<String> keyList) {
        try {
            redisTemplate.executePipelined((RedisConnection connection) -> {
                keyList.forEach(this::delete);
                return null;
            });
        } catch (Exception e) {

        }
    }

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    public boolean delete(String key) {
        try {
            return redisTemplate.delete(key);
        } catch (Exception e) {

        }
        return false;
    }
}
