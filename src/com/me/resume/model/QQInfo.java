package com.me.resume.model;

import java.io.Serializable;

public class QQInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String OpenId;//qq授权唯一标识
	private String UserName;//用户名
	private String sex;//性别
	private String photoPath;//头像地址
	private String address;//地址
	
	public String getOpenId() {
		return OpenId;
	}
	public void setOpenId(String openId) {
		OpenId = openId;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getPhotoPath() {
		return photoPath;
	}
	public void setPhotoPath(String photoPath) {
		this.photoPath = photoPath;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "QQInfo [OpenId=" + OpenId + ", UserName=" + UserName + ", sex=" + sex + ", photoPath=" + photoPath
				+ ", address=" + address + "]";
	}
	
	
}
