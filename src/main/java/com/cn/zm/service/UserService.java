package com.cn.zm.service;

import com.cn.zm.domain.User;
import com.cn.zm.entity.UserSearchVo;
import com.cn.zm.utils.PageUtil;

/**
 * 用户管理
 * @author chenjl
 *
 */
public interface UserService {
	/**
	 * 分页查询用户列表
	 * @param page
	 * @param limit
	 * @return
	 */
	PageUtil getUsers(UserSearchVo userSearch, int page, int limit);

	/**
	 *	设置用户【新增或更新】
	 * @param user
	 * @param roleIds
	 * @return
	 */
	String setUser(User user);


	/**
	 * 根据用户编码查询用户数据
	 * @param user_code
	 * @return
	 */
	User findUserByCode(String user_code);

	/**
	 * 删除用户
	 * @param id
	 */
	void delete(Integer id);
}
