package com.cn.zm.domain;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class Role {
    private Integer id;

    private String role_name;

    private String descpt;

    private String code;

    private Date create_time;

}