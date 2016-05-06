package com.me.resume.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.model.ResumeModel;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.tools.L;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.FileUtils;
import com.me.resume.utils.ImageUtils;
import com.me.resume.utils.TimeUtils;
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
	
	private ListView reviewsharingListView;
	private TextView sharemore,nodata;

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
				//String gotoStr = ".ui.BaseInfoActivity";
				// if (map!= null && map.get("userId").length > 0) {
				// gotoStr = ".ui.WorkExperienceActivity";
				// }else{
				// gotoStr = ".ui.BaseInfoActivity";
				// }
				startActivity(".ui.BaseInfoActivity",false);
				break;
			case 100:
				setData();
				setGridView();
				setTopicData();
//				getShareData();
				break;
			case 110:
				setPreferenceData("noticeshow",0);
				break;
			case 111:
				setShareData((Map<String, List<String>>)msg.obj);
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
		
		reviewsharingListView = findView(R.id.reviewsharingListView);
		sharemore = findView(R.id.sharemore);
		nodata = findView(R.id.nodata);
		makeResume.setOnClickListener(this);
		reviewResume.setOnClickListener(this);
		
		sharemore.setOnClickListener(this);
	}

	private void initData(){
		queryWhere = "select * from " + CommonText.USERINFO +" order by id desc limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		 if (commMapArray!= null && commMapArray.get("id").length > 0) {
			 kId = commMapArray.get("id")[0];
			 L.d("======初始化用户ID======="+kId);
			 initBottomButton();
		 }else{
			 ContentValues cValues = new ContentValues();
			 cValues.put("deviceid", deviceID);
			 cValues.put("createtime", TimeUtils.getCurrentTimeInString());
			 cValues.put("lastlogintime", TimeUtils.getCurrentTimeInString());
			 
			 queryResult = dbUtil.insertData(self, 
						CommonText.USERINFO, cValues);
			 if (queryResult) {
				 initData();
			 }
		 }
	}
	
	/**
	 * 显示底部button
	 */
	private void initBottomButton(){
		queryWhere = "select * from " + CommonText.BASEINFO
				+ " where userId = " + kId;
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			makeResume
					.setText(CommUtil.getStrValue(self, R.string.edit_resume));
			reviewResume.setVisibility(View.VISIBLE);
		} else {
			makeResume
					.setText(CommUtil.getStrValue(self, R.string.make_resume));
			reviewResume.setVisibility(View.GONE);
		}
	}
	
	
	
	/**
	 * 设置简历模板数据
	 */
	private void setData() {
		resumeModelList = new ArrayList<ResumeModel>();
		for (int i = 0; i < 3; i++) {
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
	
	/**
	 * 
	 * @Title:HomeActivity
	 * @Description: 简历模板view
	 */
	private void setGridView() {
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
						.get(position).getDesc());
				/*holder.setText(R.id.item_datimeview, resumeModelList
						.get(position).getDatetime());*/
			}
		};
		resumeModelgridView.setAdapter(commAdapter);

		resumeModelgridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
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
		mList.add("面试问题");
		mList.add("自我鉴定");
		mList.add("面试技巧");
		mList.add("求职简历");
		mList.add("面试问题");
		mList.add("自我鉴定");
		mList.add("面试技巧");
		mList.add("注意事项");
		mList.add("笔试经验");
		
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
	
	/**
	 * 
	 * @Title:HomeActivity
	 * @Description: 面试分享心得
	 */
	private void getShareData(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		requestData("pro_getshareinfo", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				setShareView(false);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					setShareView(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 
	 * @Title:HomeActivity
	 * @Description: 面试分享心得
	 */
	private void setShareData(Map<String, List<String>> map){
		CommForMapBaseAdapter mapBaseAdapter = new CommForMapBaseAdapter(self,map,R.layout.home_share_item,"id") {
			
			@Override
			public void convert(ViewHolder holder, List<String> item, int position) {
				// TODO Auto-generated method stub
				
			}
		};
		
		reviewsharingListView.setAdapter(mapBaseAdapter);
	}
	
	
	private void setShareView(boolean hasdata){
		if(hasdata){
			reviewsharingListView.setVisibility(View.GONE);
			sharemore.setVisibility(View.VISIBLE);
			nodata.setVisibility(View.VISIBLE);
		}else{
			reviewsharingListView.setVisibility(View.GONE);
			sharemore.setVisibility(View.VISIBLE);
			nodata.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MyApplication.userId = getPreferenceData("useId", 0);
		L.d("===userId==="+MyApplication.userId + "==KID==" + kId);
		
		initData();
		
		Bitmap bitmap = ImageUtils.getLoacalBitmap(Constants.userhead.toString());
		if (bitmap != null) {
			setLeftIcon(ImageUtils.toRoundBitmap(bitmap));
		}
		
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
			}else{
				startActivity(".ui.UserCenterActivity", false);
			}
			break;
		case R.id.right_icon:
			startActivity(".ui.SettingActivity", false);
			break;
		case R.id.sharemore:
			startActivity(".ui.ResumeShareActivity", false);
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
