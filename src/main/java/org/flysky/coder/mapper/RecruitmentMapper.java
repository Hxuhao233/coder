package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.Recruitment;
import org.flysky.coder.entity.wrapper.RecruitmentWrapper;

import java.util.List;

@Mapper
public interface RecruitmentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Recruitment record);

    int insertSelective(Recruitment record);

    Recruitment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Recruitment record);

    int updateByPrimaryKey(Recruitment record);

    boolean hasRecruitmentName(String name);

    RecruitmentWrapper getRecruitmentWrapperById(Integer id);

    List<RecruitmentWrapper> getRecruitmentWrappersByInfo(String info);
}