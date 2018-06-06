package org.flysky.coder.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.flysky.coder.constant.ResponseCode;
import org.flysky.coder.entity.User;
import org.flysky.coder.security.SecurityUtil;
import org.flysky.coder.service.IUserService;
import org.flysky.coder.token.RedisTokenService;
import org.flysky.coder.vo.Result;
import org.flysky.coder.vo.ResultWrapper;
import org.flysky.coder.vo.mail.Mail;
import org.flysky.coder.vo.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    public UserData checkIfLogger(HttpSession session){

        User u = (User) session.getAttribute("user");
        UserData loginData = new UserData();

        if(u!=null){
            loginData.setCode(Code.SUCCEED);
            loginData.setUsername(u.getUsername());
            loginData.setType(u.getType());
            loginData.setIcon(u.getIcon());
        }else{
            loginData.setCode(Code.NOT_LOGGED);
        }
        return loginData;

    }

    /**
     * 获取当前用户
     * @param session
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value="/currentUser",method= RequestMethod.GET)
    public ResultWrapper getCurrentUser(HttpSession session){
        ResultWrapper resultWrapper = new ResultWrapper();
        User u = (User) session.getAttribute("user");
        UserData userData = new UserData();

        if(u!=null){
            resultWrapper.setCode(ResponseCode.SUCCEED);
            userData.setUsername(u.getUsername());
            userData.setType(u.getType());
            userData.setIcon(u.getIcon());
            userData.setSessionId(session.getId());
            userData.setUserId(u.getId());
            resultWrapper.setPayload(userData);
        }else{
            resultWrapper.setCode(ResponseCode.NOT_FOUND);
        }
        return resultWrapper;

    }

    /**
     * 查看用户
     * @param userId
     * @return
     */
    @RequestMapping(value="/user/{userId}", method = RequestMethod.GET)
    public ResultWrapper getUserById(@PathVariable(value="userId")int userId) {
        User u = userService.getUserById(userId);
        ResultWrapper result = new ResultWrapper();
        UserData userData = new UserData();
        if(u != null){
            userData.setUsername(u.getUsername());
            userData.setType(u.getType());
            userData.setIcon(u.getIcon());
            userData.setInfo(u.getInfo());
            userData.setUserId(u.getId());
            result.setCode(ResponseCode.SUCCEED);
            result.setPayload(userData);
        } else {
            result.setCode(ResponseCode.NOT_FOUND);
        }
        return result;
    }



    @RequestMapping(value = "/user/xlogin")
    public Result login(@RequestBody User user,HttpSession session) {
        //user.setPassword(SecurityUtil.encrypt(user.getPassword()));
        boolean isLogin = userService.login(user);
        Result result=new Result();
            if (isLogin) {// 如果用户名和密码正确
                User u = userService.getUserByEmailAndPassword(user);
                Integer type = u.getType();
                result.setCode(type);
                session.setAttribute("user",u);
            }else{
                result.setCode(0);
            }

        return result;
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

    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    public ResultWrapper register(@RequestBody User user, HttpSession session,HttpServletRequest request) throws Exception{
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
            return resultWrapper;
        } else {
            if (SecurityUtil.encrypt(pwdUtil.getOldPassword()).equals(u.getPassword())) {
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

    /**
     * 修改用户信息 username inco info
     * @param user
     * @param session
     * @return
     */
    @RequiresRoles(value = "user")
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public ResultWrapper changeUserInfo(@RequestBody User user, HttpSession session) {
        User u = (User) session.getAttribute("user");
        ResultWrapper resultWrapper = new ResultWrapper();

        boolean needCheckName = user.getUsername().equals(u.getUsername())? false :true;
        u.setUsername(user.getUsername());
        u.setInfo(user.getInfo());
        u.setIcon(user.getIcon());
        int code = userService.updateUser(u, needCheckName);

        session.setAttribute("user", u);
        resultWrapper.setCode(code);
        return resultWrapper;
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
        if (!SecurityUtil.encrypt(uid + String.valueOf(timestamp)).equals(encodedContent)) {
            resultWrapper.setCode(Code.ILLEGAL_ACTIVATE_LINK);
            return resultWrapper;
        }
        if (u.getActivated()==1) {
            resultWrapper.setCode(Code.HAS_BEEN_ACTIVATED);
            return resultWrapper;
        }

        if (currentTime - tstmp > 60 * 60 * 1000 * 24) {
            try {
                Mail.sendMail("13710685836@163.com", "hxh211517", "smtp.163.com", u.getEmail(), u.getId(),request.getServerName()+":"+request.getServerPort()+request.getContextPath());
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

    @RequestMapping("/user/searchUserByUsername/{username}")
    public ResultWrapper searchUserByUsername(@PathVariable String username){
        ResultWrapper resultWrapper=new ResultWrapper();
        resultWrapper.setPayload(userService.searchUserByUsername(username));
        return resultWrapper;
    }

}
