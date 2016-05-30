package com.me.resume.tools;

import java.util.UUID;

/**
 * 
* @ClassName: UUIDGenerator 
* @Description: 获得一个UUID码
* @author Comsys-WH1510032 
* @date 2016/5/9 下午3:37:41 
*
 */
public class UUIDGenerator {

	public UUIDGenerator() { 
    } 
   
	 /** 
     * 去掉UUID里的'-'
     * @return String UUID 32位
     */ 
    public static String getUUID(){ 
        String s = UUID.randomUUID().toString();
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18)+s.substring(19,23)+s.substring(24); 
    } 
    
    /** 
     * 去掉UUID里的'-'
     * @return String UUID 16位
     */ 
    public static String getKUUID(){ 
        String s = UUID.randomUUID().toString();
        return s.substring(0,8)+s.substring(9,13)+s.substring(14,18); 
    } 
}
