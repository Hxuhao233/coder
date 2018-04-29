package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.UserVoteRecruitment;

import java.util.List;

@Mapper
public interface UserVoteRecruitmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserVoteRecruitment record);

    int insertSelective(UserVoteRecruitment record);

    UserVoteRecruitment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserVoteRecruitment record);

    int updateByPrimaryKey(UserVoteRecruitment record);

    UserVoteRecruitment getUserVoteRecruitmentByUserIdAndRecruitmentId(@Param("userId") int userId, @Param("recruitmentId") int recruitmentId);
}