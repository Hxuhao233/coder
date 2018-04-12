package org.flysky.coder.controller;

import org.flysky.coder.service.IUserService;
import org.flysky.coder.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @RequestMapping("/user/getUsernameByUid/{uid}")
    public String getUsernameByUid(@PathVariable Integer uid){
        return userService.getUserNameById(uid);
    }
}
