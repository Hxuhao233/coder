package org.flysky.coder.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.flysky.coder.entity.User;

@Mapper
public interface UserMapper {
	
	User getUser(Integer id);
	
}
