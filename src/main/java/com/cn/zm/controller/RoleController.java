package com.cn.zm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cn.zm.domain.Role;
import com.cn.zm.service.AuthService;
import com.cn.zm.utils.PageUtil;
import com.cn.zm.utils.ResponseResult;

/**
 * 
 * @author chenjl
 *
 */
@Controller
@RequestMapping("/auth")
public class RoleController {
	@Autowired 
	private AuthService authService;
	
	/**
	 * 跳转到角色列表
	 * @return
	 */
	@RequestMapping("/roleList")
	public ModelAndView toPage() {
		return new ModelAndView("/auth/roleList");
	}
	
	/**
	 * 角色列表
	 */
	@RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
	@ResponseBody
	public PageUtil getRoleList(@RequestParam("page") Integer page,
			@RequestParam("limit") Integer limit) {
		if (null == page) {
			page = 1;
		}
		if (null == limit) {
			limit = 10;
		}
		// 获取用户和角色列表
		return authService.roleList(page, limit);
	}
	
	/**
	 * 添加角色
	 * @return PermTreeDTO
	 */
	@RequestMapping(value = "/addRole", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult addRole(Role role) {
		if(StringUtils.isEmpty(role.getId())){
			return new ResponseResult("200", authService.addRole(role));
		}else{
			return new ResponseResult("200", authService.updateRole(role));
		}
	}
	
	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delRole", method = RequestMethod.POST)
	@ResponseBody
	public ResponseResult delRole(@RequestParam("id") int id) {
		return new ResponseResult("200", authService.delRole(id));
	}
	
	/**
	 * 获取所有角色
	 * @return
	 */
	@GetMapping("/allRoles")
	@ResponseBody
	public ResponseResult allRoles(){
		return new ResponseResult("200", "获取角色", authService.allRoles());
	}
}
