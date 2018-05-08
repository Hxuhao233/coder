package org.flysky.coder.service.impl;

import org.flysky.coder.entity.User;
import org.flysky.coder.mapper.UserMapper;
import org.flysky.coder.service.IUserService;
import org.flysky.coder.vo.mail.Mail;
import org.flysky.coder.vo.user.Code;
import org.flysky.coder.vo.user.EncodeUtil;
import org.flysky.coder.vo.user.PMConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public String getUserNameById(Integer uid) {
        User u=userMapper.selectByPrimaryKey(uid);
        if(u!=null){
            return u.getUsername();
        }
        return null;
    }

    @Override
    public int register(User user, String context) {
        Integer isExistEmail=userMapper.isExistEmail(user.getEmail());
        Integer isExistNickname=userMapper.isExistNickname(user.getUsername());
        if(isExistEmail!=null){
            return Code.OCCUPIED_EMAIL;
        }else if(isExistNickname!=null){
            return Code.OCCUPIED_NICKNAME;
        } else {
            // user.setActivated(false);
            String pwd = user.getPassword();
            user.setPassword(EncodeUtil.string2MD5(pwd + PMConfig.SALT));
            int status = userMapper.insertSelective(user);
            if (status != 1) {
                return Code.SYSTEM_ERROR;
            } else {
                try {
                    System.out.println("nmb");
                    Mail.sendMail("18813299326@163.com", "ll86417738", "smtp.163.com", user.getEmail(), user.getId(),
                            context);
                    System.out.println("nmb");
                } catch (Exception e) {
                    return Code.SYSTEM_ERROR;
                }
                return Code.SUCCEED;
            }
        }
    }

    @Override
    public boolean login(User user) {
        String pwd = user.getPassword();
        user.setPassword(EncodeUtil.string2MD5(pwd + PMConfig.SALT));
        User u = userMapper.selectByEmailAndPassword(user);
        // 如果返回的u不为null，u没有被封，u被激活
        if (u == null) {

            return false;
        }
        return u.getActivated()==1 ? true : false;
    }

    @Override
    public int updatePassword(User user, String newPassword) {
        User u = new User();
        u.setId(user.getId());
        u.setPassword(EncodeUtil.string2MD5(newPassword + PMConfig.SALT));
        // user.setPassword(EncodeUtil.string2MD5(newPassword+PMConfig.SALT));
        return userMapper.updateByPrimaryKeySelective(u) == 1 ? Code.SUCCEED : Code.SYSTEM_ERROR;
    }

    @Override
    public int updateNickName(User user,String newUsername) {
        if(userMapper.isExistNickname(newUsername)!=null){
            return Code.OCCUPIED_NICKNAME;
        }
        User u = new User();
        u.setId(user.getId());
        u.setUsername(newUsername);
        return userMapper.updateByPrimaryKeySelective(u) == 1 ? Code.SUCCEED : Code.SYSTEM_ERROR;
    }

    @Override
    public boolean activate(User user) {
        user.setActivated(1);
        return userMapper.updateByPrimaryKey(user) == 1 ? true : false;
    }

    public User getUserById(Integer id) {
        return userMapper.selectByPrimaryKey(id);
    }

    @Override
    public User getUserByEmailAndPassword(User user) {
        return userMapper.selectByEmailAndPassword(user);
    }

    public void subscribeUser(int uid,int subscribedUid){
       String subscriptionListName="subscriptionlist-"+String.valueOf(uid);
       Long result1=redisTemplate.opsForList().leftPushIfPresent(subscriptionListName,String.valueOf(subscribedUid));
       if(result1==0){
           redisTemplate.opsForList().leftPush(subscriptionListName,String.valueOf(subscribedUid));
       }else{
           redisTemplate.opsForList().leftPop(subscriptionListName);
       }
    }

    public void unsubscribeUser(int uid,int subscribedUid){
        String subscriptionListName="subscriptionlist-"+String.valueOf(uid);
        redisTemplate.opsForList().remove(subscriptionListName,0,String.valueOf(subscribedUid));
    }
}
