package org.flysky.coder.service;

import org.flysky.coder.entity.User;

import java.util.List;

public interface IUserService {
    String getUserNameById(Integer uid);
    int register(User user,String context);
    boolean login(User user);
    int updatePassword(User user,String newPassword);
    int updateNickName(User user,String newNickname);
    boolean activate(User user);
    User getUserById(Integer id);
    User getUserByEmailAndPassword(User user);
    List<User> searchUserByUsername(String username);

    int updateUser(User u, boolean needCheckName);
}
