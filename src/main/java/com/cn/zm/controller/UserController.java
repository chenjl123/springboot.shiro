package com.cn.zm.controller;

import java.util.concurrent.atomic.AtomicInteger;

import javax.validation.Valid;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.zm.domain.User;
import com.cn.zm.entity.UserSearchVo;
import com.cn.zm.entity.UserVo;
import com.cn.zm.service.UserService;
import com.cn.zm.utils.IStatusMessage;
import com.cn.zm.utils.PageUtil;
import com.cn.zm.utils.ResponseResult;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author chenjl
 *
 */
@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	@Autowired
	private EhCacheManager ecm;

	@RequestMapping("/userList")
	public String toUserList() {
		return "/auth/userList";
	}

	/**
	 * 分页查询用户列表
	 * @param page
	 * @param limit
	 * @param userSearch
	 * @return
	 */
	@RequestMapping(value = "/getUsers", method = RequestMethod.POST)
	@ResponseBody
	@RequiresPermissions(value="usermanage")
	@RequiresRoles(value={"bdmanage"})
	public PageUtil getUsers(@RequestParam("page") Integer page,
		@RequestParam("limit") Integer limit, UserSearchVo userSearch) {
		if(StringUtils.isEmpty(userSearch.getUser_code())){userSearch.setUser_code(null);}
		if(StringUtils.isEmpty(userSearch.getMobile())){userSearch.setMobile(null);}
		PageUtil paged = new PageUtil();
		if (null == page) {
			page = 1;
		}
		if (null == limit) {
			limit = 10;
		}
		// 获取用户和角色列表
		paged = userService.getUsers(userSearch, page, limit);
		return paged;
	}

	/**
	 * 新增或编辑用户
	 * @param user
	 * @return
	 */
	@RequestMapping(value = "/setUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult setUser(User user) {
		return new ResponseResult("200", userService.setUser(user));
	}

	/**
	 * 删除用户
	 * @return 
	 */
	@RequestMapping(value = "/delUser", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult delUser(@RequestParam("id") Integer id) {
		userService.delete(id);
		return new ResponseResult("200", "删除成功");
	}


	/**
	 * 登录
	 * @param user
	 * @param rememberMe
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult login(@RequestBody @Valid UserVo user,
			@RequestParam(value = "rememberMe", required = false) boolean rememberMe, BindingResult result) {
		if(result.hasErrors()){
			return new ResponseResult("2001", result.getFieldError().getDefaultMessage());
		}else{
			ResponseResult responseResult = new ResponseResult();
			responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
			
			//用户是否存在
			User existUser = this.userService.findUserByCode(user.getUser_code());
			if (existUser == null) {
				responseResult.setMsg("用户不存在");
				return responseResult;
			}
			
			// 用户登录
			try {
				// 1、 封装用户名、密码、是否记住我到token令牌对象 
				AuthenticationToken token = new UsernamePasswordToken(user.getUser_code(), DigestUtils.md5Hex(user.getPassword()),rememberMe);
				// 2、 Subject调用login
				Subject subject = SecurityUtils.getSubject();
				// 在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
				// 每个Realm都能在必要时对提交的AuthenticationTokens作出反应
				// 所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
				subject.login(token);
				responseResult.setCode(IStatusMessage.SystemStatus.SUCCESS.getCode());
			} catch (UnknownAccountException uae) {
				log.error("用户登录，用户验证未通过：未知用户！user=" + user.getUser_code(), uae);
				responseResult.setMsg("该用户不存在，请您联系管理员");
			} catch (IncorrectCredentialsException ice) {
				log.error("用户登录，用户验证未通过：错误的凭证，密码输入错误！user=" + user.getUser_code(),ice);
				responseResult.setMsg("用户名或密码不正确");
			} catch (LockedAccountException lae) {
				log.error("用户登录，用户验证未通过：账户已锁定！user=" + user.getUser_code(), lae);
				responseResult.setMsg("账户已锁定");
			} catch (ExcessiveAttemptsException eae) {
				log.error("用户登录，用户验证未通过：错误次数大于5次,账户已锁定！user=.getUser_code()" + user, eae);
				responseResult.setMsg("用户名或密码错误次数大于5次,账户已锁定!</br><span style='color:red;font-weight:bold; '>2分钟后可再次登录，或联系管理员解锁</span>");
			} catch (DisabledAccountException sae){
				log.error("用户登录，用户验证未通过：帐号已经禁止登录！user=" + user.getUser_code(),sae);
				responseResult.setCode(IStatusMessage.SystemStatus.ERROR.getCode());
				responseResult.setMsg("帐号已经禁止登录");
			}catch (AuthenticationException ae) {
				// 通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
				log.error("用户登录，用户验证未通过：认证异常，异常信息如下！user=" + user.getUser_code(),ae);
				responseResult.setMsg("用户名或密码不正确");
			} catch (Exception e) {
				log.error("用户登录，用户验证未通过：操作异常，异常信息如下！user=" + user.getUser_code(), e);
				responseResult.setMsg("用户登录失败，请您稍后再试");
			}
			Cache<String, AtomicInteger> passwordRetryCache = ecm.getCache("passwordRetryCache");
			if (null != passwordRetryCache) {
				int retryNum = (passwordRetryCache.get(existUser.getUser_code()) == null ? 0 : passwordRetryCache.get(existUser.getUser_code())).intValue();
				log.info("输错次数：" + retryNum);
				if (retryNum > 0 && retryNum < 6) {
					responseResult.setMsg("用户名或密码错误" + retryNum + "次,再输错" + (6 - retryNum) + "次账号将锁定");
				}
			}
			log.info("用户登录，user=" + user.getUser_code() + ",登录结果=responseResult:" + responseResult);
			return responseResult;
		}
	}
}
