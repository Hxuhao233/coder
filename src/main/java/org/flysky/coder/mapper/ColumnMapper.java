package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.Column;
import org.flysky.coder.entity.wrapper.ColumnWrapper;

import java.util.List;

@Mapper
public interface ColumnMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Column record);

    int insertSelective(Column record);

    Column selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Column record);

    int updateByPrimaryKey(Column record);

    boolean hasColumnName(String name);

    ColumnWrapper getColumnWrapperById(int id);

    List<Column> getColumnByUserId(int userId);
}