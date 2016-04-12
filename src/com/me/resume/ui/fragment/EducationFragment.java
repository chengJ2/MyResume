package com.me.resume.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
	
	// 学校名称
	private EditText info_school;
	
	// 专业名称;学历
	private TextView info_majorname,info_degreee;
	
	// 是否统招
	private RadioGroup rg_examination;
	private RadioButton rb_examination1,rb_examination2;
	
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
		initdata();
//		fillData();
	}

	private void initview() {
		// TODO Auto-generated method stub
		info_school = (EditText)view.findViewById(R.id.info_school);
		info_majorname = (TextView)view.findViewById(R.id.info_majorname);
		info_degreee = (TextView)view.findViewById(R.id.info_degreee);
		rg_examination = (RadioGroup)view.findViewById(R.id.rg_examination);
		rb_examination1 = (RadioButton)view.findViewById(R.id.rb_examination1);
		rb_examination2 = (RadioButton)view.findViewById(R.id.rb_examination2);
	}
	
	private void initdata() {
		// TODO Auto-generated method stub
		
	}
}
