package com.me.resume.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.model.ResumeModel;
import com.me.resume.tools.L;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.whjz.android.text.CommonText;

/**
 * 
 * @ClassName: HomeActivity
 * @Description: 首页
 * @author Comsys-WH1510032
 * @date 2016/3/29 下午4:56:41
 * 
 */
public class HomeActivity extends BaseActivity implements OnClickListener {
	
	private List<ResumeModel> resumeModelList;

	private CommonBaseAdapter<ResumeModel> commAdapter = null;
	private GridView resumeModelgridView;

	private Button makeResume,reviewResume;

	private GridView resumeQuegridview;
	
	private boolean isExit = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				// queryWhere = "select * from " + CommonText.BASEINFO +
				// " where userId = 1";
				// commMapArray = dbUtil.queryData(self, queryWhere);
				String gotoStr = ".ui.BaseInfoActivity";
				// if (map!= null && map.get("userId").length > 0) {
				// gotoStr = ".ui.WorkExperienceActivity";
				// }else{
				// gotoStr = ".ui.BaseInfoActivity";
				// }
				startActivity(gotoStr,false);
				break;
			case 100:
				setData();
				setGridView();
				setTopicData();
				break;
			case 110:
				setPreferenceData("noticeshow",0);
				break;
			case 0:
				isExit = false;
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_home, null);
		boayLayout.addView(v);
		
		if (getPreferenceData("startVerytime", 0) == 1) {
			startActivity(".MainActivity", true);
		}
		
		findViews();
		
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(100);
			}
		},50);
		
		TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
		Constants.DEVICEID = TelephonyMgr.getDeviceId();

	}
	
	private void findViews(){
		setTopTitle(R.string.resume_center);
		setMsgHide();
		setRightIconVisible(View.VISIBLE);
		setRight2IconVisible(View.GONE);
		setLeftIcon(R.drawable.icon_person_avtar);
		setRightIcon(R.drawable.icon_setting);
		setfabLayoutVisible(View.GONE);
		
		makeResume = findView(R.id.make_btn);
		reviewResume = findView(R.id.review_btn);
		resumeModelgridView = findView(R.id.grid);
		resumeQuegridview = findView(R.id.xgln_gridview);

		right_icon.setImageResource(R.drawable.icon_setting);
		
		makeResume.setOnClickListener(this);
		reviewResume.setOnClickListener(this);
		
		initData();
	}

	private void initData(){
		 queryWhere = "select * from " + CommonText.BASEINFO + " where userId = 1";
		 commMapArray = dbUtil.queryData(self, queryWhere);
		 if (commMapArray!= null && commMapArray.get("userId").length > 0) {
			 makeResume.setText(CommUtil.getStrValue(self, R.string.edit_resume));
			 reviewResume.setVisibility(View.VISIBLE);
		 }else{
			 makeResume.setText(CommUtil.getStrValue(self, R.string.make_resume));
			 reviewResume.setVisibility(View.GONE);
		 }
	}
	
	/**
	 * 设置简历模板数据
	 */
	private void setData() {
		resumeModelList = new ArrayList<ResumeModel>();
		for (int i = 0; i < 10; i++) {
			ResumeModel item = new ResumeModel();
			ArrayList<String> url = new ArrayList<String>();
			url.add(R.drawable.a001 + "");
			url.add(R.drawable.a002 + "");
			url.add(R.drawable.a003 + "");
			url.add(R.drawable.a004 + "");

			item.setTitle("简历模板" + i);
			item.setDesc("普通求职个人基本简历模板 ");
			item.setPicUrl(url);
			item.setDatetime("2015-06-" + i);
			resumeModelList.add(item);
		}
	}

	/** 设置GirdView参数，绑定数据 */
	private void setGridView() {
		int size = resumeModelList.size();
		int length = 100;
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int gridviewWidth = (int) (size * (length + 4) * density);
		int itemWidth = (int) (length * density);

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				gridviewWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		resumeModelgridView.setLayoutParams(params); // 设置GirdView布局参数,横向布局的关键
		resumeModelgridView.setColumnWidth(itemWidth); // 设置列表项宽
		resumeModelgridView.setHorizontalSpacing(5); // 设置列表项水平间距
		resumeModelgridView.setStretchMode(GridView.NO_STRETCH);
		resumeModelgridView.setNumColumns(size); // 设置列数量=列表集合数
		commAdapter = new CommonBaseAdapter<ResumeModel>(self, resumeModelList,
				R.layout.home_grilview_item) {

			@Override
			public void convert(ViewHolder holder, ResumeModel item,
					int position) {
				holder.setImageResource(
						R.id.item_imageview,
						Integer.parseInt(resumeModelList.get(position)
								.getPicUrl().get(0)));
				holder.setText(R.id.item_textview, resumeModelList
						.get(position).getTitle());
			}
		};
		resumeModelgridView.setAdapter(commAdapter);

		resumeModelgridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(self, ImagePagerActivity.class);
				// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
				intent.putStringArrayListExtra(
						ImagePagerActivity.EXTRA_IMAGE_URLS, resumeModelList
								.get(position).getPicUrl());
				intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 设置简历面试相关话题数据
	 */
	private void setTopicData(){
		mList = new ArrayList<String>();
		mList.add("求职简历");
		mList.add("面试问题");
		mList.add("自我鉴定");
		mList.add("面试技巧");
		mList.add("求职简历");
		mList.add("面试问题");
		mList.add("自我鉴定");
		mList.add("面试技巧");
		mList.add("注意事项");
		mList.add("笔试经验");
		mList.add("自我鉴定");
		mList.add("面试技巧");
		
		commStrAdapter = new CommonBaseAdapter<String>(self, mList,
				R.layout.home_xgln_grilview) {

			@Override
			public void convert(ViewHolder holder, String item,
					final int position) {
				holder.setText(R.id.itemName, mList.get(position));
				if (position == 1 || position == 5 || position == 6) {
					holder.setTextColor(R.id.itemName,
							CommUtil.getIntValue(self, R.color.red));
				}

				holder.setOnClickEvent(R.id.itemName, new ClickEvent() {

					@Override
					public void onClick(View view) {
						ActivityUtils.startActivityPro(self, Constants.PACKAGENAME + ".ui.TopicActivity", "position",
								String.valueOf(position));

					}
				});
			}
		};

		resumeQuegridview.setAdapter(commStrAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.make_btn:
			if (MyApplication.userId == 0) {
				if(getPreferenceData("noticeshow",1) == 1){
					DialogUtils.showAlertDialog(self, CommUtil.getStrValue(self,
							R.string.dialog_action_alert), mHandler);
				}else{
					mHandler.sendEmptyMessage(1);
				}
			} else {
				mHandler.sendEmptyMessage(1);
			}
			break;
		case R.id.review_btn:
			startActivity(".MainActivity", false);
			break;
		case R.id.left_lable:
			if (MyApplication.userId == 0) {
				startActivity(".ui.UserLoginActivity", false);
			}
			break;
		case R.id.right_icon:
			startActivity(".ui.SettingActivity", false);
			break;
		default:
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void exit(){  
        if (!isExit) {  
            isExit = true;  
            toastMsg(R.string.app_exit);
            mHandler.sendEmptyMessageDelayed(0, 2000);  
        } else {  
            Intent intent = new Intent(Intent.ACTION_MAIN);  
            intent.addCategory(Intent.CATEGORY_HOME);  
            startActivity(intent);  
            MyApplication.getApplication().exitAll();
        }  
    }
}
