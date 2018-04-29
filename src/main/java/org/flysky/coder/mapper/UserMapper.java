package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.Post;
import org.flysky.coder.entity.User;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User selectByUsername(@Param("username")String username);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    User selectByEmailAndPassword(User user);

    Integer isExistEmail(String email);

    Integer isExistNickname(String nickname);

    List<User> selectAll();

    List<User> selectAllNormalUser();

}