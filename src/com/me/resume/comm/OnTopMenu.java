package com.me.resume.comm;

/**
 * 
* @ClassName: OnTopMenu 
* @Description: 更多菜单监听事件 
* @date 2016/4/26 下午3:19:28 
*
 */
public interface OnTopMenu {

	public static final int MSG_MENU1 = 1001;
	public static final int MSG_MENU2 = 1002;
	/**
	 * 同步
	 */
	public static final int MSG_MENU3 = 1003;
	
	/**
	 * 未登录
	 */
	public static final int MSG_MENU31 = 1004;
	
	public static final int MSG_MENU32 = 1014;
	/**
	 * 未联网
	 */
	public static final int MSG_MENU33 = 1015; 
	
	public static final int MSG_MENU41 = 1005;
	public static final int MSG_MENU42 = 1006;
}
