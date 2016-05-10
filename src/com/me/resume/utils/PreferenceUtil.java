package com.me.resume.utils;

import com.me.resume.comm.Constants;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * SharedPreferences util类
 * @author Administrator
 *
 */
public class PreferenceUtil {

	private SharedPreferences sp;
	private SharedPreferences.Editor editor;

	public PreferenceUtil() {
	}
	
	public PreferenceUtil(Context context) {
		sp = context.getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE);
		editor = sp.edit();
	}

	public void setPreferenceData(String key, String value) {
		editor.putString(key, value).commit();
	}

	public String getPreferenceData(String str, String def) {
		return sp.getString(str, def);
	}

	public void setPreferenceData(String key, int value) {
		editor.putInt(key, value).commit();
	}

	public int getPreferenceData(String str, int def) {
		return sp.getInt(str, def);
	}

	public void setPreferenceData(String key, boolean value) {
		editor.putBoolean(key, value).commit();
	}

	public boolean getPreferenceData(String str) {
		return sp.getBoolean(str, false);
	}
	
	public void clearPreferenceData() {
		editor.clear().commit();
	}
}
