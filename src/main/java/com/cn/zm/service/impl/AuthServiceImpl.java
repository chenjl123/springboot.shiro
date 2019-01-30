package com.cn.zm.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cn.zm.dao.PermissionMapper;
import com.cn.zm.dao.RoleMapper;
import com.cn.zm.dao.RolePermissionMapper;
import com.cn.zm.dao.UserRoleMapper;
import com.cn.zm.domain.Permission;
import com.cn.zm.domain.Role;
import com.cn.zm.domain.RolePermission;
import com.cn.zm.domain.UserRole;
import com.cn.zm.service.AuthService;
import com.cn.zm.utils.PageUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class AuthServiceImpl implements AuthService {
	@Autowired
	private PermissionMapper permissionMapper;
	@Autowired
	private RoleMapper roleMapper;
	@Autowired
	private UserRoleMapper userRoleMapper;
	@Autowired
	private RolePermissionMapper rolePermissionMapper;
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
	public String addRole(Role role) {
		role.setCreate_time(new Date());
		this.roleMapper.insert(role);
		return "添加成功";
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
	public String updateRole(Role role) {
		this.roleMapper.update(role);
		return "修改成功";
	}



	@Override
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=30000,rollbackFor={RuntimeException.class, Exception.class})
	public String delRole(int id) {
		this.roleMapper.delete(id);
		return "删除成功";
	}

	@Override
	public List<Role> getRoleByUser(Integer userId) {
		return this.roleMapper.getRoleByUserId(userId);
	}

	@Override
	public List<Permission> findPermsByRoleId(Integer id) {
		return this.permissionMapper.findPermsByRole(id);
	}

	@Override
	public PageUtil roleList(int page, int limit) {
		PageUtil pdr = new PageUtil();
		PageHelper.startPage(page, limit);
		List<Role> urList = roleMapper.findList();
		// 获取分页查询后的数据
		PageInfo<Role> pageInfo = new PageInfo<>(urList);
		// 设置获取到的总记录数total：
		pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
		pdr.setList(urList);
		return pdr;
	}

	@Override
	public List<Permission> getUserPerms(Integer id) {
		return this.permissionMapper.getUserPerms(id);
	}

	@Override
	public List<Role> allRoles() {
		return roleMapper.findList();
	}

	@Override
	public void addUserRoles(Integer[] roles, Integer uid) {
		// TODO Auto-generated method stub
		UserRole drole = new UserRole();
		drole.setUser_id(uid);
		userRoleMapper.delete(drole);
		List<UserRole> list = new ArrayList<>();
		if(roles != null){
			for(Integer role : roles){
				UserRole urole = new UserRole();
				urole.setRole_id(role);
				urole.setUser_id(uid);
				list.add(urole);
			}
			userRoleMapper.insert(list);
		}
	}

	@Override
	public List<Permission> findPerms() {
		return permissionMapper.findAll();
	}

	@Override
	public List<RolePermission> listByRole(Integer role_id) {
		return rolePermissionMapper.listByRole(role_id);
	}

	@Override
	public void addRolePerms(Integer[] perms, Integer rid) {
		// TODO Auto-generated method stub
		RolePermission rp = new RolePermission();
		rp.setRole_id(rid);
		rolePermissionMapper.delete(rp);
		if(perms != null){
			List<RolePermission> list = new ArrayList<>();
			for(Integer id : perms){
				RolePermission temp = new RolePermission();
				temp.setPermit_id(id);
				temp.setRole_id(rid);
				list.add(temp);
			}
			rolePermissionMapper.insert(list);
		}
	}

}
