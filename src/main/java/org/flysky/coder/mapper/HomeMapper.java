package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.Home;
import org.flysky.coder.entity.wrapper.HomeWrapper;

import java.util.List;

@Mapper
public interface HomeMapper {
    boolean hasHomeName(String name);

    int deleteByPrimaryKey(Integer id);

    int insert(Home record);

    int insertSelective(Home record);

    Home selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Home record);

    int updateByPrimaryKey(Home record);

    List<Home> getHomesByUserId(int userId);

    HomeWrapper getHomeWrapperById(int homeId);
}