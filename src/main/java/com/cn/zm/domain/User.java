package com.cn.zm.domain;

import java.io.Serializable;
import java.util.Date;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class User implements Serializable {
	private static final long serialVersionUID = -3096736268081409238L;
	private Integer id;

	private String user_name;

	private String mobile;

	private String email;

	private String password;

	private Date create_time;

	private String is_del;

	@NotNull(message="用户编码不能为空")
	private String user_code;

	private String is_lock;
}