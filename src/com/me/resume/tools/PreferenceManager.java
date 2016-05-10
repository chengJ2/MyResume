package com.me.resume.tools;

import com.me.resume.comm.Constants;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences管理类
 * @author Administrator
 *
 */
public class PreferenceManager {

	protected SharedPreferences sp;
	
	public PreferenceManager() {
		// TODO Auto-generated constructor stub
	}
	
	public PreferenceManager(Context context) {
		sp = context.getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE);
	}
	
	public void setPreferenceData(String key, String value) {
		sp.edit().putString(key, value).commit();
	}

	public String getPreferenceData(String str, String def) {
		return sp.getString(str, def);
	}

	public void setPreferenceData(String key, int value) {
		sp.edit().putInt(key, value).commit();
	}

	public int getPreferenceData(String str, int def) {
		return sp.getInt(str, def);
	}

	public void setPreferenceData(String key, boolean value) {
		sp.edit().putBoolean(key, value).commit();
	}

	public boolean getPreferenceData(String str) {
		return sp.getBoolean(str, false);
	}
	
}
