package org.flysky.coder.service;


import com.github.pagehelper.PageInfo;
import org.flysky.coder.entity.wrapper.RecordWrapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * Created by hxuhao233 on 2018/3/29.
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
public class ChatServiceTest {

    @Autowired
    private IChatService chatService;

    //@Test
    public void getRecordTest(){
        PageInfo<RecordWrapper> record = chatService.getRecord(1,1,10);
        System.out.println(record);
    }



}
