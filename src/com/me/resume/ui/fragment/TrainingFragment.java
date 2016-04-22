package com.me.resume.ui.fragment;

import java.util.Map;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
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
	
	private TextView info_startime,info_endtime;
	
	private EditText info_trainingorganization,info_trainingclass,info_certificate,info_description;
	
	protected DbLocalUtil dbUtil = new DbUtilImplement();;// 本地数据库对象
	protected BaseCommonUtil baseCommon = new CommonUtil();;// 通用工具对象实例
	protected Info info = new Info();
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
			case 11:
				if (msg.obj != null) {
					info_startime.setText((String)msg.obj);
				}
				break;
			case 12:
				if (msg.obj != null) {
					info_endtime.setText((String)msg.obj);
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
		info_startime = (TextView)view.findViewById(R.id.info_startworktime);
		info_endtime = (TextView)view.findViewById(R.id.info_endworktime);
		info_trainingorganization = (EditText)view.findViewById(R.id.info_trainingorganization);
		info_trainingclass = (EditText)view.findViewById(R.id.info_trainingclass);
		info_certificate = (EditText)view.findViewById(R.id.info_certificate);
		info_description = (EditText)view.findViewById(R.id.info_description);
		
		info_startime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogUtils.showTimeChooseDialog(getActivity(), info_startime,
						R.string.we_info_start_worktime, 11,mHandler);
			}
		});
		info_endtime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				DialogUtils.showTimeChooseDialog(getActivity(), info_endtime,
						R.string.we_info_end_worktime, 12,mHandler);
			}
		});
	}
	
	private void initData() {
		String queryWhere = "select * from " + CommonText.EDUCATION_TRAIN + " where userId = 1 order by _id limit 1";
		Map<String, String[]> map = dbUtil.queryData(getActivity(), queryWhere);
		if (map!= null && map.get("userId").length > 0) {
//			setInfoStartTime(map.get("trainingtimestart")[0]);
//			setInfoEndTime(map.get("trainingtimeend")[0]);
			setInfotrainingorganization(map.get("trainingorganization")[0]);
			setInfotrainingclass(map.get("trainingclass")[0]);
			setInfocertificate(map.get("certificate")[0]);
			setInfodescription(map.get("description")[0]);
		}
	}
	
	public void setInfoStartTime(String value){
		info_startime.setText(value);
	}
	
	public void setInfoEndTime(String value){
		info_endtime.setText(value);
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
	
	public String getInfoStartTime(){
		return CommUtil.getTextValue(info_startime);
	}
	
	public String getInfoEndTime(){
		return CommUtil.getTextValue(info_endtime);
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
