package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.UserCollectRecruitment;

import java.util.List;

@Mapper
public interface UserCollectRecruitmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserCollectRecruitment record);

    int insertSelective(UserCollectRecruitment record);

    UserCollectRecruitment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserCollectRecruitment record);

    int updateByPrimaryKey(UserCollectRecruitment record);

    UserCollectRecruitment getUserCollectRecruitmentByUserIdAndRecruitmentId(@Param("userId") int userId, @Param("recruitmentId") int recruitmentId);
}