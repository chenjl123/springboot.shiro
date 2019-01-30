package com.cn.zm.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户查询
 * @author Administrator
 *
 */
@Setter
@Getter
@ToString
public class UserSearchVo {

	private Integer page;

	private Integer limit;

	private String user_code;

	private String mobile;
}
