package com.cn.zm.controller;

import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cn.zm.domain.Permission;
import com.cn.zm.domain.RolePermission;
import com.cn.zm.domain.User;
import com.cn.zm.service.AuthService;
import com.cn.zm.utils.ResponseResult;

/**
 * 
 * @author chenjl
 *
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
	@Autowired private AuthService authService;

	/**
	 * 权限树
	 * @return
	 */
	@GetMapping("/permList")
	public String permList(){
		return "/auth/permission";
	}
	
	/**
	 * 查询权限树数据
	 * @return PermTreeDTO
	 */
	@RequestMapping(value = "/findPerms", method = RequestMethod.GET)
	@ResponseBody
	public List<Permission> findPerms() {
		return authService.findPerms();
	}
	
	/**
	 * 根据用户id查询权限树数据
	 * @return PermTreeDTO
	 */
	@RequestMapping(value = "/getUserPerms", method = RequestMethod.GET)
	@ResponseBody
	public List<Permission> getUserPerms() {
		List<Permission> pvo = null;
		User existUser= (User) SecurityUtils.getSubject().getPrincipal();
		if(null==existUser){
			return pvo;
		}
		try {
			pvo = authService.getUserPerms(existUser.getId());
			//生成页面需要的json格式
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pvo;
	}
	
	/**
	 * 获取用户角色
	 * @param uid
	 * @return
	 */
	@GetMapping("/getRoleByUser")
	@ResponseBody
	public ResponseResult getRoleByUser(@RequestParam("uid") Integer uid){
		return new ResponseResult("200", "success", authService.getRoleByUser(uid));
	}

	/**
	 * 添加用户角色
	 * @param roles
	 * @param uid
	 * @return
	 */
	@PostMapping("/addUserRoles")
	@ResponseBody
	public ResponseResult addUserRoles(@RequestParam("roles[]") Integer[] roles, Integer uid){
		authService.addUserRoles(roles, uid);
		return new ResponseResult("200", "设置成功");
	}
	
	/**
	 * 获取角色权限
	 * @param uid
	 * @return
	 */
	@GetMapping("/getpermList")
	@ResponseBody
	public ResponseResult getpermList(@RequestParam("rid") Integer rid){
		List<RolePermission> rper = authService.listByRole(rid);
		List<Permission> plist = authService.findPerms();
		for (RolePermission rpk : rper) {
			//设置角色下的权限checked状态为：true
			for (Permission pvo : plist) {
				if(String.valueOf(rpk.getPermit_id()).equals(String.valueOf(pvo.getId()))){
					pvo.setChecked(true);
				}
			}
		}
		return new ResponseResult("200", "获取成功", plist);
	}
	
	/**
	 * 添加角色权限
	 * @param perms
	 * @param rid
	 * @return
	 */
	@PostMapping("/addRolePerms")
	@ResponseBody
	public ResponseResult addRolePerms(@RequestParam("perms[]") Integer[] perms, Integer rid){
		authService.addRolePerms(perms, rid);
		return new ResponseResult("200", "添加成功");
	}
}
