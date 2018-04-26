package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.Source;
import org.flysky.coder.entity.wrapper.SourceWrapper;

import java.util.List;

@Mapper
public interface SourceMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Source record);

    int insertSelective(Source record);

    Source selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Source record);

    int updateByPrimaryKey(Source record);

    boolean hasSourceName(String name);

    SourceWrapper getSourceWrapperById(int id);

    List<SourceWrapper> getSourceWrappersByInfo(String info);

    List<SourceWrapper> getSourceWrappersByUserId(int userId);
}