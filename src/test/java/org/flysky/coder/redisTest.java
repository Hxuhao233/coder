package org.flysky.coder;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by hxuhao233 on 2018/3/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class redisTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Test
    public void test001() {
        redisTemplate.opsForValue().set("key1","value1");
        //redisTemplate.opsForHash().put();
    }

    @Test
    public void test002() {
        String value = redisTemplate.opsForValue().get("key1");
        System.out.println(value);
    }

}
