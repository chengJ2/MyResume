package com.me.resume.ui.fragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.utils.DialogUtils;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: EducationFragment 
* @Description: 培训经历 
* @date 2016/4/6 下午1:55:32 
*
 */
public class TrainingFragment extends BaseFragment {

	private View view;
	
	private TextView info_startime,info_endtime;
	
	private EditText info_trainingorganization,info_trainingclass,info_certificate,info_description;
	
	private String trId;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
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
		super.onAttach(activity);
	}
	
	 @Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	}
	 
	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_training_layout, container, false);
		initview();
		initData();
		registerReceiver();
        return view;
    }
	 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
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
				DialogUtils.showTimeChooseDialog(getActivity(), info_startime,
						R.string.we_info_start_worktime, 11,mHandler);
			}
		});
		info_endtime.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				DialogUtils.showTimeChooseDialog(getActivity(), info_endtime,
						R.string.we_info_end_worktime, 12,mHandler);
			}
		});
	}
	
	private void registerReceiver(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constants.EDUCATION_RECEIVE_TR);
		filter.addAction(Constants.MANAGER_EDUCATION_RECEIVE_TR);
		getActivity().registerReceiver(trainReceiver, filter);
	}
	
	 private BroadcastReceiver trainReceiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(Constants.EDUCATION_RECEIVE_TR)){
				initData();
				
				Intent i=new Intent();
				i.setAction(Constants.TRAIN_GET);
	       		getActivity().sendBroadcast(i);
				
			}else if (intent.getAction().equals(Constants.MANAGER_EDUCATION_RECEIVE_TR)) {
				String tokenId = intent.getStringExtra(Constants.TOKENID);
				refreshData(tokenId);
			}
	}};
	
	
	private void initData() {
		queryWhere = "select * from " + CommonText.EDUCATION_TRAIN + " where userId = '"+ BaseActivity.uTokenId +"' order by id desc limit 1";
		commap = dbUtil.queryData(getActivity(), queryWhere);
		if (commap!= null && commap.get("userId").length > 0) {
			trId = commap.get("tokenId")[0];
			setInfoStartTime(commap.get("trainingtimestart")[0]);
			setInfoEndTime(commap.get("trainingtimeend")[0]);
			setInfotrainingorganization(commap.get("trainingorganization")[0]);
			setInfotrainingclass(commap.get("trainingclass")[0]);
			setInfocertificate(commap.get("certificate")[0]);
			setInfodescription(commap.get("description")[0]);
		}
	}
	
	private void refreshData(String tokenId) {
		queryWhere = "select * from " + CommonText.EDUCATION_TRAIN + " where userId = '"+ BaseActivity.uTokenId +"' and tokenId ='" + tokenId + "' limit 1";
		commap = dbUtil.queryData(getActivity(), queryWhere);
		if (commap!= null && commap.get("userId").length > 0) {
			trId = commap.get("tokenId")[0];
			setInfoStartTime(commap.get("trainingtimestart")[0]);
			setInfoEndTime(commap.get("trainingtimeend")[0]);
			setInfotrainingorganization(commap.get("trainingorganization")[0]);
			setInfotrainingclass(commap.get("trainingclass")[0]);
			setInfocertificate(commap.get("certificate")[0]);
			setInfodescription(commap.get("description")[0]);
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
	
	
	public String getTrId(){
		return trId;
	}
	
	public String getInfoStartTime(){
		return getTextValue(info_startime);
	}
	
	public String getInfoEndTime(){
		return getTextValue(info_endtime);
	}
	
	public String getInfotrainingorganization(){
		return getTextValue(info_trainingorganization);
	}
	
	public String getInfotrainingclass(){
		return getTextValue(info_trainingclass);
	}
	
	public String getInfocertificate(){
		return getTextValue(info_certificate);
	}
	public String getInfodescription(){
		return getTextValue(info_description);
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		if (trainReceiver != null) {
			getActivity().unregisterReceiver(trainReceiver);
		}
	}
}
