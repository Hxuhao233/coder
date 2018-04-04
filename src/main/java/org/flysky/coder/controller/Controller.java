package org.flysky.coder.controller;

import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class Controller {


    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/home")
    @ResponseBody
    public String home() {
        return "Hello sb";
    }

    @RequestMapping("/test/login/{uid}")
    @ResponseBody
    public String login(HttpSession session,@PathVariable("uid") int uid){
        User u = userMapper.selectByPrimaryKey(uid);
        session.setAttribute("user",u);
        return "login "  + u.getUsername();
    }


    @RequestMapping("/test/show")
    @ResponseBody
    public User show(HttpSession session){
        User u = (User) session.getAttribute("user");
        return u;
    }


    @RequestMapping("/test/exit")
    @ResponseBody
    public String exit(HttpSession session){
        session.invalidate();
        return "bye";
    }

}
