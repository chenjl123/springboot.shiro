package com.cn.zm.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Permission {
    private Integer id;

    private String name;

    private Integer pid;

    private Integer zindex;

    private Integer istype;

    private String descpt;

    private String code;

    private String icon;

    private String page;

    private Date create_time;
    
	private boolean checked;
}