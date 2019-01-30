package com.cn.zm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.zm.domain.UserRole;

@Mapper
public interface UserRoleMapper {
    int delete(UserRole key);

    int insert(List<UserRole> record);

	/**
	 * 根据用户获取用户角色中间表数据
	 * @param userId
	 * @return
	 */
	List<UserRole> findByUserId(int userId);
}