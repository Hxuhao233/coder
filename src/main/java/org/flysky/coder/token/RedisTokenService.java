package org.flysky.coder.token;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenService {
    @Autowired
    private  StringRedisTemplate redisTemplate;

    public  Integer getIdByToken(String token){
        Set<String> keys=redisTemplate.keys(token);
        List<String> keyList= new ArrayList<>();
        keyList.addAll(keys);
        if(keyList.isEmpty()){
            return null;
        }else{
            redisTemplate.boundValueOps("token").expire(3600,TimeUnit.SECONDS);
            return Integer.parseInt(redisTemplate.opsForValue().get(keyList.get(0)));
        }

    }

    public  String addToken(Integer uid){
        String token=UUID.randomUUID().toString();
        redisTemplate.opsForValue().set(token,String.valueOf(uid),3600, TimeUnit.SECONDS);
        return token;
    }
}
