package com.cn.zm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.zm.domain.Permission;

@Mapper
public interface PermissionMapper {

    /**
     * 查找所有权限数据
     * @return
     */
	List<Permission> findAll();

	/**
	 * 根据角色id获取权限数据
	 * @param roleId
	 * @return
	 */
	List<Permission> findPermsByRole(Integer roleId);

	List<Permission> getUserPerms(Integer userId);
}