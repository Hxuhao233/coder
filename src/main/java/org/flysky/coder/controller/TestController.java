package org.flysky.coder.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.flysky.coder.constant.ResponseCode;
import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.UserMapper;
import org.flysky.coder.service.impl.UserService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.xml.ws.Response;
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
    public String home(HttpSession session) {
        String ret = "hello, " +  (session == null || session.getAttribute("user") == null ? "游客" : ((User)session.getAttribute("user")).getUsername());
        return ret;
    }

    @ResponseBody
    @RequestMapping(value = "/test/loginUser/{id}")
    public User login1(HttpSession session, @PathVariable(value = "id")int id){
        User user = userMapper.selectByPrimaryKey(id);
        session.setAttribute("user", user);
        return user;
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

    @RequestMapping("/test/guest")
    @ResponseBody
    public String guest(Principal principal, HttpSession session){
        return "游客可访问";
    }

    @RequiresRoles(value = "user")
    @RequestMapping("/test/user")
    @ResponseBody
    public User user(Principal principal, HttpSession session){
        User u = (User) session.getAttribute("user");
        return u;
    }


    @RequiresRoles(value = "manager")
    @RequestMapping("/test/manager")
    @ResponseBody
    public User manager(HttpSession session){
        User u = (User) session.getAttribute("user");
        return u;
    }

    @RequestMapping("/test/logout")
    @ResponseBody
    public ResultWrapper logout(){
        Subject subject = SecurityUtils.getSubject();
        if (subject != null)
            subject.logout();
        return new ResultWrapper(ResponseCode.SUCCEED);
    }

}