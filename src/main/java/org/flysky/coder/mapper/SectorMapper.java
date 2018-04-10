package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.flysky.coder.entity.Sector;

import java.util.List;

@Mapper
public interface SectorMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Sector record);

    int insertSelective(Sector record);

    Sector selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Sector record);

    int updateByPrimaryKey(Sector record);

    int getSectorByName_COUNT(@Param("name") String name);

    List<Sector> selectAll();
}