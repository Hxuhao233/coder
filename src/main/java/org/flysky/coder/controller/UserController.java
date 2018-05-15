package org.flysky.coder.controller;

import org.flysky.coder.entity.User;
import org.flysky.coder.service.IUserService;
import org.flysky.coder.service.impl.UserService;
import org.flysky.coder.token.RedisTokenService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.SessionWrapper;
import org.flysky.coder.vo.mail.Mail;
import org.flysky.coder.vo.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
public class UserController {
    @Autowired
    private IUserService userService;

    @Autowired
    private RedisTokenService redisTokenService;



    @RequestMapping("/user/getUsernameByUid/{uid}")
    public String getUsernameByUid(@PathVariable Integer uid){
        return userService.getUserNameById(uid);
    }


    /**
     * 检测是否登陆
     * @param session
     * @return
     */

    @RequestMapping(value="/user/checkIfLogged",method= RequestMethod.GET)
    public LoginData checkIfLogger(HttpSession session){

        User u = (User) session.getAttribute("user");
        LoginData loginData = new LoginData();

        if(u!=null){
            loginData.setCode(Code.SUCCEED);
            loginData.setUsername(u.getUsername());
            loginData.setType(u.getType());
        }else{
            loginData.setCode(Code.NOT_LOGGED);
        }
        return loginData;

    }



    @RequestMapping(value = "/user/login")
    public LoginDataWithSessionID login(HttpSession session,@RequestBody User user) {
        boolean isLogin = userService.login(user);
        LoginDataWithSessionID loginData = new LoginDataWithSessionID();

            if (isLogin) {// 如果用户名和密码正确
                User u = userService.getUserByEmailAndPassword(user);
                loginData.setSessionId(session.getId());
                Integer type = u.getType();
                loginData.setType(type);
                if (u.getActivated()==0) {
                    loginData.setCode(Code.NOT_ACTIVATED);
                } else {
                    loginData.setCode(Code.SUCCEED);
                    loginData.setUsername(u.getUsername());
               }
            } else {
                loginData.setCode(Code.WRONG_EMAIL_OR_PASSWORD);
            }

        return loginData;
    }

    @RequestMapping(value = "/ff/login")
    public Result login(HttpSession session) {
//        boolean isLogin = userService.login(user);
//        LoginDataWithSessionID loginData = new LoginDataWithSessionID();
//
//        if (isLogin) {// 如果用户名和密码正确
//            User u = userService.getUserByEmailAndPassword(user);
//            loginData.setSessionId(session.getId());
//            Integer type = u.getType();
//            loginData.setType(type);
//            if (u.getActivated()==0) {
//                loginData.setCode(Code.NOT_ACTIVATED);
//            } else {
//                loginData.setCode(Code.SUCCEED);
//                loginData.setUsername(u.getUsername());
//            }
//        } else {
//            loginData.setCode(Code.WRONG_EMAIL_OR_PASSWORD);
//        }
         User u=new User();
         u.setId(1);
         String res=redisTokenService.addToken(1);
         Result result=new Result();
         result.setInfo(res);
         return result;
    }

    @RequestMapping("/user/register")
    public ResultWrapper register(@RequestBody User user, HttpSession session,HttpServletRequest request) {
        ResultWrapper resultWrapper=new ResultWrapper();
        int status = userService.register(user,request.getServerName()+":"+request.getServerPort()+request.getContextPath());
        resultWrapper.setCode(status);
        return resultWrapper;
    }


    @RequestMapping("/user/changePassword")
    public ResultWrapper changePassword(@RequestBody PasswordUtil pwdUtil, HttpSession session) {
        User u = (User) session.getAttribute("user");
        ResultWrapper resultWrapper=new ResultWrapper();
        if (u == null) {
            resultWrapper.setCode(Code.NOT_LOGGED);
            return  resultWrapper;
        } else {
            if (EncodeUtil.string2MD5(pwdUtil.getOldPassword() + PMConfig.SALT).equals(u.getPassword())) {
                int status = userService.updatePassword(u, pwdUtil.getNewPassword());
                resultWrapper.setCode(status);
                return resultWrapper;
            } else {
                resultWrapper.setCode(Code.NOT_PERMISSION);
                return resultWrapper;
            }
        }
    }


    @RequestMapping("/user/changeUsername")
    public ResultWrapper changeNickname(@RequestBody UsernameUtil usernameUtil, HttpSession session) {
        User u = (User) session.getAttribute("user");
        ResultWrapper resultWrapper=new ResultWrapper();
        if (u == null) {
            resultWrapper.setCode(Code.NOT_LOGGED);
            return resultWrapper;
        } else {
            int status = userService.updateNickName(u, usernameUtil.getUsername());
            u.setUsername(usernameUtil.getUsername());
            session.setAttribute("user", u);
            return resultWrapper;
        }
    }


    @RequestMapping("/user/logoff")
    public void logoff(HttpSession session) {
        session.invalidate();
    }



    @RequestMapping("/activate/{uid}/{timestamp}/{encodedContent}")
    public ResultWrapper activate(@PathVariable(value = "uid") String uid,@PathVariable(value = "timestamp")String timestamp,
                                  @PathVariable(value = "encodedContent") String encodedContent, HttpServletRequest request) {
        System.out.print(uid);
        // System.out.print(timestamp);
        // System.out.print(encodedContent);
        Long tstmp = Long.parseLong(timestamp);
        long currentTime = System.currentTimeMillis();
        User u = null;
        u = userService.getUserById(Integer.parseInt(uid));
        ResultWrapper resultWrapper=new ResultWrapper();
        if (u == null) {
            resultWrapper.setCode(Code.SYSTEM_ERROR);
            return resultWrapper;
        }
        if (!EncodeUtil.string2MD5(uid + String.valueOf(timestamp) + PMConfig.SALT).equals(encodedContent)) {
            resultWrapper.setCode(Code.ILLEGAL_ACTIVATE_LINK);
            return resultWrapper;
        }
        if (u.getActivated()==1) {
            resultWrapper.setCode(Code.HAS_BEEN_ACTIVATED);
            return resultWrapper;
        }

        if (currentTime - tstmp > 60 * 60 * 1000 * 24) {
            try {
                Mail.sendMail("reao123@163.com", "1a2s3d4f", "smtp.163.com", u.getEmail(), u.getId(),request.getServerName()+":"+request.getServerPort()+request.getContextPath());
            } catch (Exception e) {
                resultWrapper.setCode(Code.SYSTEM_ERROR);
                return resultWrapper;
            }
            resultWrapper.setCode(Code.ACTIVATE_TIME_OUT);
            return resultWrapper;
        } else {
            userService.activate(u);
            resultWrapper.setCode(Code.SUCCEED);
            return resultWrapper;
        }
    }
}
