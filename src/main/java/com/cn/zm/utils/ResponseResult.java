package com.cn.zm.utils;

import java.io.Serializable;

import com.cn.zm.utils.IStatusMessage;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResponseResult implements Serializable {
	
	private static final long serialVersionUID = 7285065610386199394L;

	private String code;
	private String msg;
	private Object data;
	
	public ResponseResult() {
		this.code = IStatusMessage.SystemStatus.SUCCESS.getCode();
		this.msg = IStatusMessage.SystemStatus.SUCCESS.getMessage();
	}
	
	public ResponseResult(IStatusMessage statusMessage){
		this.code = statusMessage.getCode();
		this.msg = statusMessage.getMessage();
	}
	
	public ResponseResult(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public ResponseResult(String code, String msg, Object data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
}
