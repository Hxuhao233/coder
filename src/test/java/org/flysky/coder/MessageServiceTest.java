package org.flysky.coder;


import org.flysky.coder.entity.Message;
import org.flysky.coder.mapper.MessageMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MessageServiceTest {
    @Autowired
    private MessageMapper messageMapper;
    @Test
    public void nimabi() throws Exception{
        for(int i=0;i<10;i++) {
            Message message = new Message();
            Message message2 = new Message();
            message.setContent("cnmgb");
            message.setCreatedAt(LocalDateTime.now());
            message.setFromUid(1);
            message.setToUid(2);
            Thread.sleep(1000);
            message2.setContent("cnmgbbbb");
            message2.setCreatedAt(LocalDateTime.now());
            message2.setFromUid(2);
            message2.setToUid(1);
            messageMapper.insert(message);
            messageMapper.insert(message2);
            Thread.sleep(1000);
        }
    }
}
