package com.me.resume.comm;

/**
 * 响应码
 * 
 * @author Administrator
 * 
 */
public class ResponseCode {

	/**
	 * 正常返回结果
	 */
	public static final String RESULT_OK = "200";

	/**
	 * 用户名或密码不正确
	 */
	public static final String INVALID_INFO = "404";

	/**
	 * 用户名重复
	 */
	public static final String USERNAME_REPEAT = "405";
	
	/**
	 * 用户名/手机号不存在
	 */
	public static final String USERNAME_NOEXIST = "406";
	
	// 请求超时
	public static final int EXECUTE_TIMEOUT = -0X2000;

	// 请求网络异常
	public static final int EXECUTE_NETERROR = -0X1000;

	// 加载数据成功
	public static final int LOAD_DATA_SUCCESS = 0X1000;

	// 加载数据失败
	public static final int LOAD_DATA_ERROR = 0X8000;

	// 加载暂无数据
	public static final int LOAD_NO_DATA = -0X8000;
}
