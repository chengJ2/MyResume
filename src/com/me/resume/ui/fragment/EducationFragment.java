package com.me.resume.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.me.resume.R;

/**
 * 
* @ClassName: EducationFragment 
* @Description: 教育背景 
* @author Comsys-WH1510032 
* @date 2016/4/6 下午1:55:32 
*
 */
public class EducationFragment extends Fragment {

	private View view;
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}
	
	 @Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
	 
	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_education_layout, container, false);
        return view;
    }
	 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initview();
//		initdata();
//		fillData();
	}

	private void initview() {
		// TODO Auto-generated method stub
		
	}
}
