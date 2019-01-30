package com.cn.zm.utils;

/**
 * @项目名称： wyait-manage
 * @类名称： IStatusMessage
 * @类描述：响应状态信息
 * @创建时间： 2018年1月4日11:04:17
 * @version:
 */
public interface IStatusMessage {
	
	String getCode();

	String getMessage();
	
	public enum SystemStatus implements IStatusMessage{

		SUCCESS("200","SUCCESS"), //请求成功
		ERROR("2001","ERROR"),	   //请求失败
		PARAM_ERROR("2002","PARAM_ERROR"), //请求参数有误
		SUCCESS_MATCH("2003","SUCCESS_MATCH"), //表示成功匹配
		NO_LOGIN("2100","NO_LOGIN"), //未登录
		MANY_LOGINS("2101","MANY_LOGINS"), //多用户在线（踢出用户）
		UPDATE("2102","UPDATE"), //用户信息或权限已更新（退出重新登录）
		LOCK("2111","LOCK"); //用户已锁定
		private String code;
		private String message;

		private SystemStatus(String code,String message){
			this.code = code;
			this.message = message;
		}

		public String getCode(){
			return this.code;
		}

		public String getMessage(){
			return this.message;
		}
	}
}