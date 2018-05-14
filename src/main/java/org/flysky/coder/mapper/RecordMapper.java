package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.Record;
import org.flysky.coder.entity.wrapper.RecordWrapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface RecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Record record);

    int insertSelective(Record record);

    Record selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Record record);

    int updateByPrimaryKey(Record record);

    List<RecordWrapper> getRecordWrapperByRoomId(int roomId);

    List<RecordWrapper> getRecordWrapperByRoomIdAndLastTime(@Param("roomId") int roomId, @Param("time")LocalDateTime time);
}