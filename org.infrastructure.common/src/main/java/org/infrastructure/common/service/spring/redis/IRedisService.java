package org.infrastructure.common.service.spring.redis;

import java.util.List;

public interface IRedisService {

	public boolean set(String key, String value);
	
	public boolean set(String key, String value, long expireSecond);
	
	public boolean setObject(String key, Object value);
	
	public boolean setObject(String key, Object value, long expireSecond);
	
	public <T> T getObject(String key, Class<T> clz);
	
	public String get(String key);

	public boolean expire(String key, long expire);

	public <T> boolean setList(String key, List<T> list);

	public <T> List<T> getList(String key, Class<T> clz);

	public long lpush(String key, Object obj);

	public long rpush(String key, Object obj);

	public String lpop(String key);
	
	public Long incr(final String key);
	
	public boolean exists(final String key);
	
    public Boolean hSet(final String key, final String field, Object obj);
    
    public <T> T hGet(final String key, final String field, Class<T> clz);
    
    public void delKey(final String key);
}
