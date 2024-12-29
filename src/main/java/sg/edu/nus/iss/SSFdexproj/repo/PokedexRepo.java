package sg.edu.nus.iss.SSFdexproj.repo;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.SSFdexproj.utils.Constants;

@Repository
public class PokedexRepo {

    @Autowired
    @Qualifier(Constants.template01)
    private RedisTemplate<String, String> template;

    
    
    public void setHash(String redisKey, String mapKey, String value) {
        template.opsForHash().put(redisKey,mapKey, value);
    }

    public void updateValue(String redisKey,String mapKey,String value) {
        template.opsForHash().put(redisKey, mapKey,value); 
    }

    
    public List<Object> getAllValuesFromHash(String redisKey) {
    
        return (List<Object>) template.opsForHash().values(redisKey); 
        
    }
    
    public String getValueFromHash(String redisKey, String mapKey) {
        return (String) template.opsForHash().get(redisKey, mapKey); 
    }

    
    public Boolean deleteKeyFromHash(String redisKey, String mapKey) {

        Boolean isDeleted = false;
        Long iFound = template.opsForHash().delete(redisKey, mapKey);
        if (iFound>0) {
            isDeleted = true;
        }
        return isDeleted;
        
    }

    
    public Boolean hasKey(String redisKey, String mapKey) { 
        return template.opsForHash().hasKey(redisKey, mapKey);
    }



    
    public Boolean hashExists(String redisKey) {
        return template.hasKey(redisKey);
    }

    //expire a key
    // HSET myHashKey field1 value1
    // EXPIRE myHashKey 10
    //remember to casr int to long
    public void expireKey(String redisKey, Long seconds) {
        Duration expireDuration = Duration.ofSeconds(seconds);
        template.expire(redisKey, expireDuration);
        
    }
    public void addToHashWithTTL(String redisKey, String hashKey, String hashValue, long seconds) {
        // Add value to the hash
        template.opsForHash().put(redisKey, hashKey, hashValue);

        // Set TTL for the key
        template.expire(redisKey, Duration.ofSeconds(seconds));
    }



    // Add multiple key-value pairs to a hash
    public void setMapAll(String redisKey, HashMap<String, String> map) {
        template.opsForHash().putAll(redisKey, map);
    }

    // Retrieve all key-value pairs in a hash
    public Map<Object, Object> getAllFromHash(String redisKey) {
        return template.opsForHash().entries(redisKey);
    }

    
    // Retrieve all keys in a hash
    public Set<Object> getAllKeysFromHash(String redisKey) {
        return template.opsForHash().keys(redisKey); //hkeys c01
    }

    // Retrieve all values in a hash -> already have in a list
    public Set<Object> getAllValues(String redisKey) { //hvals c01
        return (Set<Object>) template.opsForHash().values(redisKey);
    }

    // Increment a numeric value in a hash
    public void incrementHashValue(String redisKey, String mapKey, long delta) {
        template.opsForHash().increment(redisKey, mapKey, delta); //hincrby c01 count 1
    }

    // Increment a floating-point value in a hash
    public void incrementHashValue(String redisKey, String mapKey, double delta) {
        template.opsForHash().increment(redisKey, mapKey, delta);
    }

    public String getValue(String redisKey) { 
        return template.opsForValue().get(redisKey);  
        

    }
    
    
}
