package org.flysky.coder.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.UserMapper;
import org.flysky.coder.vo.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;

/**
 * 测试用控制器
 */
@Controller
public class TestController {


    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/hello")
    @ResponseBody
    public String home() {
        return "Hello sb";
    }



    @RequestMapping(value = "/test/login", method = RequestMethod.GET)
    public String login(){
        return "/login.html";
    }

    //@RequestMapping(value = "/test/login", method = RequestMethod.POST)
    //@ResponseBody
    public ResultWrapper login(@RequestParam("username")String username, @RequestParam("password")String password){
        ResultWrapper result = new ResultWrapper(1);
        result.setInfo("登录成功");
        //result.setPayload(u);
        return result;
    }


    //@RequiresRoles(value = "user")
    @RequestMapping("/test/show")
    @ResponseBody
    public User show(Principal principal, HttpSession session){
        User u = (User) session.getAttribute("user");
        return u;
    }


    //@RequiresRoles(value = "manager")
    @RequestMapping("/test/show2")
    @ResponseBody
    public User show2(HttpSession session){
        User u = (User) session.getAttribute("user");
        return u;
    }

    @RequestMapping("/test/logout")
    @ResponseBody
    public String exit(HttpSession session){
        return "bye";
    }

}