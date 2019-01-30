package com.cn.zm.utils;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


/**
 * 分页工具类
 * @author Administrator
 *
 */

@Setter
@Getter
@ToString
public class PageUtil {

	//总记录数量
	private Integer totals;
	//当前页数据列表
	private List<?> list;

	private Integer code=200;

	public PageUtil() {
	}

	public PageUtil( Integer totals,
			List<?> list) {
		this.totals = totals;
		this.list = list;
	}
}
