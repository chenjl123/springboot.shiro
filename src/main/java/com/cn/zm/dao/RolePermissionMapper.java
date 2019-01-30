package com.cn.zm.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.cn.zm.domain.RolePermission;

@Mapper
public interface RolePermissionMapper {
    int delete(RolePermission key);

    int insert(List<RolePermission> record);

	List<RolePermission> listByRole(int role_id);
}