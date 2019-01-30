package com.cn.zm.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserRoleVo {
	private Integer id;

	private String user_code;
	
	private String user_name;

	private String mobile;

	private String email;

	private String create_time;

	private boolean is_del;

	private String role_names;

}