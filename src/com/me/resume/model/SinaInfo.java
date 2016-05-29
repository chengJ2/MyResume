package com.me.resume.model;

import java.io.Serializable;

public class SinaInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	  /** 用户UID（int64） */
    public String id;
    /** 字符串型的用户 UID */
    public String idstr;
    /** 用户昵称 */
    public String screen_name;
    /** 友好显示名称 */
    public String name;
    /** 用户所在省级ID */
    public int province;
    /** 用户所在城市ID */
    public int city;

    /** 性别，m：男、f：女、n：未知 */
    public String gender;
    /** 用户大头像地址 */
    public String avatar_large;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getIdstr() {
		return idstr;
	}
	public void setIdstr(String idstr) {
		this.idstr = idstr;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getProvince() {
		return province;
	}
	public void setProvince(int province) {
		this.province = province;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getAvatar_large() {
		return avatar_large;
	}
	public void setAvatar_large(String avatar_large) {
		this.avatar_large = avatar_large;
	}
	@Override
	public String toString() {
		return "SinaInfo [id=" + id + ", idstr=" + idstr + ", screen_name="
				+ screen_name + ", name=" + name + ", province=" + province
				+ ", city=" + city + ", gender="
				+ gender + ", avatar_large=" + avatar_large + "]";
	}
	
}
