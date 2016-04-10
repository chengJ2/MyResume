package com.me.resume.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResumeModel implements Serializable{

	/** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private ArrayList<String> picUrl;
	
	private String title;
	
	private String desc;
	
	private String datetime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ArrayList<String> getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(ArrayList<String> picUrl) {
		this.picUrl = picUrl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	@Override
	public String toString() {
		return "ResumeModel [id=" + id + ", picUrl=" + picUrl + ", title="
				+ title + ", desc=" + desc + ", datetime=" + datetime + "]";
	}
	
}
