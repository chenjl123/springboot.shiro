package com.cn.zm.service;

import java.util.List;

import com.cn.zm.domain.Permission;
import com.cn.zm.domain.Role;
import com.cn.zm.domain.RolePermission;
import com.cn.zm.utils.PageUtil;

/**
 *权限管理类
 */
public interface AuthService {
	/**
	 * 查询所有角色 --
	 * @return
	 */
	PageUtil roleList(int page, int limit);

	/**
	 * 关联查询权限树列表
	 * @return
	 */
	List<Permission> findPerms();

	/**
	 * 添加角色
	 * @param role--
	 * @return
	 */
	String addRole(Role role);

	/**
	 * 更新角色--
	 * @param role
	 * @return
	 */
	String updateRole(Role role);

	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	String delRole(int id);

	/**
	 * 根据用户获取角色列表
	 * @param userId
	 * @return
	 */
	List<Role> getRoleByUser(Integer userId);

	/**
	 * 根据角色id获取权限数据
	 * @param id
	 * @return
	 */
	List<Permission> findPermsByRoleId(Integer id);
	
	/**
	 * 根据用户id获取权限数据
	 * @param id
	 * @return
	 */
	List<Permission> getUserPerms(Integer id);
	
	/**
	 * 查询所有角色 --
	 * @return
	 */
	List<Role> allRoles();
	
	/**
	 * 添加用户角色
	 * @param roles
	 * @param uid
	 */
	void addUserRoles(Integer[] roles, Integer uid);
	
	/**
	 * 通过角色回去权限
	 * @param role_id
	 * @return
	 */
	List<RolePermission> listByRole(Integer role_id);
	
	/**
	 * 添加角色权限
	 * @param roles
	 * @param uid
	 */
	void addRolePerms(Integer[] perms, Integer rid);

}
