package com.cn.zm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.zm.domain.User;
import com.cn.zm.entity.UserRoleVo;
import com.cn.zm.entity.UserSearchVo;

@Mapper
public interface UserMapper {
	int delete(Integer id);

	int insert(User record);

	int update(User record);

	/**
	 * 分页查询用户数据
	 * @return
	 */
	List<UserRoleVo> getUsers(UserSearchVo userSearch);

	/**
	 *	根据用户编码获取用户数据
	 * @param user_code
	 * @return
	 */
	User findUserByCode(String user_code);
}