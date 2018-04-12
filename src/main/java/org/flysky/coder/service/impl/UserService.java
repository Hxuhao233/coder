package org.flysky.coder.service.impl;

import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.UserMapper;
import org.flysky.coder.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{
    @Autowired
    private UserMapper userMapper;

    @Override
    public String getUserNameById(Integer uid) {
        User u=userMapper.selectByPrimaryKey(uid);
        if(u!=null){
            return u.getUsername();
        }
        return null;
    }
}
