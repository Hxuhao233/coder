package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.Message;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MessageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Message record);

    int insertSelective(Message record);

    Message selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Message record);

    int updateByPrimaryKey(Message record);

    List<Message> viewConversations(@Param("fromUid")Integer fromUid, @Param("toUid") Integer toUid);

    List<Message> getMessageByContentAndTime(@Param("content")String content, @Param("time1") LocalDateTime time1, @Param("time2")LocalDateTime time2);

    List<Message> getMessageByContentAndUsername(@Param("content") String content,@Param("username1") String username1,@Param("username2") String username2);
}