package com.me.resume.ui.fragment;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.utils.CommUtil;
import com.whjz.android.text.CommonText;
import com.whjz.android.text.Info;
import com.whjz.android.util.common.CommonUtil;
import com.whjz.android.util.common.DbUtilImplement;
import com.whjz.android.util.interfa.BaseCommonUtil;
import com.whjz.android.util.interfa.DbLocalUtil;

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
	
	protected DbLocalUtil dbUtil = new DbUtilImplement();;// 本地数据库对象
	protected BaseCommonUtil baseCommon = new CommonUtil();;// 通用工具对象实例
	protected Info info = new Info();
	
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
		initData();
	}

	private void initview() {
		info_time = (TextView)view.findViewById(R.id.info_time);
		info_trainingorganization = (EditText)view.findViewById(R.id.info_trainingorganization);
		info_trainingclass = (EditText)view.findViewById(R.id.info_trainingclass);
		info_certificate = (EditText)view.findViewById(R.id.info_certificate);
		info_description = (EditText)view.findViewById(R.id.info_description);
	}
	
	private void initData() {
		String queryWhere = "select * from " + CommonText.EDUCATION_TRAIN + " where userId = 1";
		Map<String, String[]> map = dbUtil.queryData(getActivity(), queryWhere);
		if (map!= null && map.get("userId").length > 0) {
			setInfoTime(map.get("time")[0]);
			setInfotrainingorganization(map.get("trainingorganization")[0]);
			setInfotrainingclass(map.get("trainingclass")[0]);
			setInfocertificate(map.get("certificate")[0]);
			setInfodescription(map.get("description")[0]);
		}
	}
	
	public void setInfoTime(String value){
		info_time.setText(value);
	}
	
	public void setInfotrainingorganization(String value){
		info_trainingorganization.setText(value);
	}
	
	public void setInfotrainingclass(String value){
		info_trainingclass.setText(value);
	}
	
	public void setInfocertificate(String value){
		info_certificate.setText(value);
	}
	
	public void setInfodescription(String value){
		info_description.setText(value);
	}
	
	public String getInfoTime(){
		return CommUtil.getTextValue(info_time);
	}
	
	public String getInfotrainingorganization(){
		return CommUtil.getTextValue(info_trainingorganization);
	}
	
	public String getInfotrainingclass(){
		return CommUtil.getTextValue(info_trainingclass);
	}
	
	public String getInfocertificate(){
		return CommUtil.getTextValue(info_certificate);
	}
	public String getInfodescription(){
		return CommUtil.getTextValue(info_description);
	}
	
}
