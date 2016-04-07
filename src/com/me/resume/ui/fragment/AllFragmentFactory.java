package com.me.resume.ui.fragment;

import java.util.HashMap;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class AllFragmentFactory {

	private static Map<String, Fragment> fragments = new HashMap<String, Fragment>();

	public static Fragment putFragment(Fragment mfragment, String tab) {
		Fragment fragment = fragments.get(tab);
		if (fragment == null) {
			Bundle b = new Bundle();
			b.putString("all", tab);
			mfragment.setArguments(b);
			fragments.put(tab, mfragment);
			return mfragment;
		}
		return fragment;
	}

	public static Map<String, Fragment> getFragmentMap() {

		return fragments;
	}
	
	public static void removeFragment(String tab){
		
		fragments.remove(tab);
	}
	public static Fragment getFragment(String tab) {
		
		return fragments.get(tab);
	}
}
