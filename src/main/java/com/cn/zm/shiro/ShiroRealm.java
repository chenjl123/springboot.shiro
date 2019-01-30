package com.cn.zm.shiro;

import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import com.cn.zm.dao.UserMapper;
import com.cn.zm.domain.Permission;
import com.cn.zm.domain.Role;
import com.cn.zm.domain.User;
import com.cn.zm.service.AuthService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShiroRealm extends AuthorizingRealm {

	@Autowired
	private UserMapper userMapper;
	@Autowired
	private AuthService authService;
	
	/**
	 * 权限认证
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		// TODO Auto-generated method stub
		//授权
		log.info("授予角色和权限");
		// 添加权限 和 角色信息
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 获取当前登陆用户
		Subject subject = SecurityUtils.getSubject();
		User user = (User) subject.getPrincipal();
		if (user.getMobile().equals("18516596566")) {
			// 超级管理员，添加所有角色、添加所有权限
			authorizationInfo.addRole("*");
			authorizationInfo.addStringPermission("*");
		} else {
			// 普通用户，查询用户的角色，根据角色查询权限
			Integer userId = user.getId();
			List<Role> roles = this.authService.getRoleByUser(userId);
			if (null != roles && roles.size() > 0) {
				for (Role role : roles) {
					authorizationInfo.addRole(role.getCode());
					// 角色对应的权限数据
					List<Permission> perms = this.authService.findPermsByRoleId(role
							.getId());
					if (null != perms && perms.size() > 0) {
						// 授权角色下所有权限
						for (Permission perm : perms) {
							authorizationInfo.addStringPermission(perm
									.getCode());
						}
					}
				}
			}
		}
		return authorizationInfo;
	}

	/**
	 * 登录认证
	 */
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
		//UsernamePasswordToken用于存放提交的登录信息
		UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken;
		String user_code = token.getUsername();
		// 调用数据层
		User user = userMapper.findUserByCode(user_code);
		if (user == null) {
			// 用户不存在
			return null;
		} else {
			// 密码存在
			// 第一个参数 ，登陆后，需要在session保存数据
			// 第二个参数，查询到密码(加密规则要和自定义的HashedCredentialsMatcher中的HashAlgorithmName散列算法一致)
			// 第三个参数 ，realm名字
			return new SimpleAuthenticationInfo(user, DigestUtils.md5Hex(user.getPassword()),getName());
		}
	}
	
	/**
	 * 清除所有缓存
	 */
	public void clearCachedAuth(){
		this.clearCachedAuthorizationInfo(SecurityUtils.getSubject().getPrincipals());
	}
}
