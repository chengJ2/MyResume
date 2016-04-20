package com.me.resume.ui.fragment;

import java.util.Arrays;
import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: EducationFragment 
* @Description: 教育背景 
* @author Comsys-WH1510032 
* @date 2016/4/6 下午1:55:32 
*
 */
public class EducationFragment extends BaseFragment implements OnClickListener{

	private View view;
	
	// 学校名称
	private EditText info_school;
	
	// 专业名称;学历
	private TextView info_startworktime,info_endworktime,info_majorname,info_degree;
	
	// 是否统招
	private RadioGroup rg_examination;
	private RadioButton rb_examination1,rb_examination2;
	
	private String rg_examinationStr = "0";
	
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 2:
				int position = (int) msg.obj;
				info_degree.setText(mList.get(position));
				break;
			case 11:
				if (msg.obj != null) {
					info_startworktime.setText((String)msg.obj);
				}
				break;
			case 12:
				if (msg.obj != null) {
					info_endworktime.setText((String)msg.obj);
				}
				break;
			default:
				break;
			}
		};
	};
	
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
		initview(view);
		initData();
        return view;
    }
	 
	private void initData() {
		String queryWhere = "select * from " + CommonText.EDUCATION + " where userId = 1 order by _id limit 1";
		Map<String, String[]> map = dbUtil.queryData(getActivity(), queryWhere);
		if (map!= null && map.get("userId").length > 0) {
			setInfoStartTime(map.get("worktimestart")[0]);
			setInfoEndTime(map.get("worktimeend")[0]);
			setInfoSchool(map.get("school")[0]);
			setInfomajorname(map.get("majorname")[0]);
			setInfodegree(map.get("degree")[0]);
			setInfoexamination(map.get("examination")[0]);
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	private void initview(View view) {
		// TODO Auto-generated method stub
		info_startworktime = (TextView)view.findViewById(R.id.info_startworktime);
		info_endworktime = (TextView)view.findViewById(R.id.info_endworktime);
		info_school = (EditText)view.findViewById(R.id.info_school);
		info_majorname = (TextView)view.findViewById(R.id.info_majorname);
		info_degree = (TextView)view.findViewById(R.id.info_degree);
		rg_examination = (RadioGroup)view.findViewById(R.id.rg_examination);
		rb_examination1 = (RadioButton)view.findViewById(R.id.rb_examination1);
		rb_examination2 = (RadioButton)view.findViewById(R.id.rb_examination2);
		
		info_degree.setOnClickListener(this);
		
		rg_examination.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId == rb_examination1.getId()){
					rg_examinationStr = "0";
				}else if(checkedId == rb_examination2.getId()){
					rg_examinationStr = "1";
				}
			}
		});
		
		info_startworktime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogUtils.showTimeChooseDialog(getActivity(), info_startworktime,
						R.string.we_info_start_worktime, 11,mHandler);
			}
		});
		info_endworktime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogUtils.showTimeChooseDialog(getActivity(), info_endworktime,
						R.string.we_info_end_worktime, 12,mHandler);
			}
		});
	}
	
	public void setInfoStartTime(String value){
		info_startworktime.setText(value);
	}
	
	public void setInfoEndTime(String value){
		info_endworktime.setText(value);
	}
	
	public void setInfoSchool(String value){
		info_school.setText(value);
	}
	
	public void setInfomajorname(String value){
		info_majorname.setText(value);
	}
	
	public void setInfodegree(String value){
		info_degree.setText(value);
	}
	
	public void setInfoexamination(String value){
		if(value.equals("0")){
			rb_examination1.setChecked(true);
			rb_examination2.setChecked(false);
		}else{
			rb_examination2.setChecked(true);
			rb_examination1.setChecked(false);
		}
	}
	
	
	public String getInfoStartTime(){
		return CommUtil.getTextValue(info_startworktime);
	}
	
	public String getInfoEndTime(){
		return CommUtil.getTextValue(info_endworktime);
	}
	
	public String getInfoSchool(){
		return CommUtil.getTextValue(info_school);
	}
	
	public String getInfomajorname(){
		return CommUtil.getTextValue(info_majorname);
	}
	
	public String getInfodegree(){
		return CommUtil.getTextValue(info_degree);
	}
	
	public String getInfoexamination(){
		return rg_examinationStr;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.info_degree:
			getValues(R.array.ed_degress_values,info_degree,R.string.ed_info_degree);
			break;
		default:
			break;
		}
	}
	
	private void getValues(int array,View parent,int resId) {
		String[] item_text = CommUtil.getArrayValue(getActivity(),array); 
		mList = Arrays.asList(item_text);
		DialogUtils.showPopWindow(getActivity(), parent, resId, mList, mHandler);
	}
	
}
