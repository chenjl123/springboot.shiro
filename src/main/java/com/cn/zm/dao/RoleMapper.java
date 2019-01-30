package com.cn.zm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.zm.domain.Role;

@Mapper
public interface RoleMapper {
    int delete(Integer id);

    int insert(Role record);

    int update(Role record);

    /**
     * 查询所有的角色列表
     * @return
     */
	List<Role> findList();

	/**
	 * 根据用户id获取角色数据
	 * @param userId
	 * @return
	 */
	List<Role> getRoleByUserId(Integer userId);

}