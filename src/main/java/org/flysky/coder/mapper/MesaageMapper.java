package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.Mesaage;

@Mapper
public interface MesaageMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Mesaage record);

    int insertSelective(Mesaage record);

    Mesaage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Mesaage record);

    int updateByPrimaryKey(Mesaage record);
}