package org.flysky.coder.controller;

import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {
	
	
	@Autowired
	private UserMapper userMapper;
	
    @RequestMapping("/home")
    @ResponseBody
    String home() {
    	
    	User u = userMapper.getUser(1);
    	
        return "Hello " + u.getName();
    }

}
