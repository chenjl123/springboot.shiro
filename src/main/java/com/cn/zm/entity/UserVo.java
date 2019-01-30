package com.cn.zm.entity;



import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class UserVo {
	private Integer id;
	private String user_name;
	private String mobile;
	private String email;
	@NotNull(message = "密码不能为空")
	private String password;
	@NotNull(message = "用户名不能为空")
	private String user_code;

}