package com.me.resume.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
public class TrainingFragment extends Fragment {

	private View view;
	
	private TextView info_time;
	
	private EditText info_trainingorganization,info_trainingclass,info_certificate,info_description;
	
	
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
		view = inflater.inflate(R.layout.fragment_training_layout, container, false);
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
		info_time = (TextView)view.findViewById(R.id.info_time);
		info_trainingorganization = (EditText)view.findViewById(R.id.info_trainingorganization);
		info_trainingclass = (EditText)view.findViewById(R.id.info_trainingclass);
		info_certificate = (EditText)view.findViewById(R.id.info_certificate);
		info_description = (EditText)view.findViewById(R.id.info_description);
		
	}
}
