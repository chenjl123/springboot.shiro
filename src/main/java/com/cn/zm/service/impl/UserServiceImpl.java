package com.cn.zm.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.cn.zm.dao.RoleMapper;
import com.cn.zm.dao.UserMapper;
import com.cn.zm.domain.Role;
import com.cn.zm.domain.User;
import com.cn.zm.entity.UserRoleVo;
import com.cn.zm.entity.UserSearchVo;
import com.cn.zm.service.UserService;
import com.cn.zm.utils.PageUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private RoleMapper roleMapper;

	@Override
	public PageUtil getUsers(UserSearchVo userSearch, int page, int limit) {
		PageUtil pdr = new PageUtil();
		PageHelper.startPage(page, limit);
		List<UserRoleVo> urList = userMapper.getUsers(userSearch);
		// 获取分页查询后的数据
		PageInfo<UserRoleVo> pageInfo = new PageInfo<>(urList);
		// 设置获取到的总记录数total：
		pdr.setTotals(Long.valueOf(pageInfo.getTotal()).intValue());
		// 将角色名称提取到对应的字段中
		if (null != urList && urList.size() > 0) {
			for (UserRoleVo ur : urList) {
				List<Role> roles = roleMapper.getRoleByUserId(ur.getId());
				if (null != roles && roles.size() > 0) {
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < roles.size(); i++) {
						Role r = roles.get(i);
						sb.append(r.getRole_name());
						if (i != (roles.size() - 1)) {
							sb.append("，");
						}
					}
					ur.setRole_names(sb.toString());
				}
			}
		}
		pdr.setList(urList);
		return pdr;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = 30000, rollbackFor = {
			RuntimeException.class, Exception.class })
	public String setUser(User user) {
		if (user.getId() != null) {
			this.userMapper.update(user);
		} else {
			// 判断用户是否已经存在
			User existUser = this.userMapper.findUserByCode(user.getUser_code());
			if (null != existUser) {
				return "用户名已经存在";
			}
			// 新增用户
			user.setCreate_time(new Date());
			user.setIs_del("0");
			user.setIs_lock("0");
			// 设置加密密码
			if (!StringUtils.isEmpty(user.getPassword())) {
				user.setPassword(DigestUtils.md5Hex(user.getPassword()));
			} else {
				user.setPassword(DigestUtils.md5Hex("123456"));
			}
			this.userMapper.insert(user);
		}
		return "操作成功";
	}

	@Override
	public User findUserByCode(String user_code) {
		return this.userMapper.findUserByCode(user_code);
	}

	@Override
	public void delete(Integer id) {
		this.userMapper.delete(id);
	}
}
