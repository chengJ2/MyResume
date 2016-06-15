package com.me.resume.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.tools.L;
import com.me.resume.tools.UUIDGenerator;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.FileUtils;
import com.me.resume.utils.ImageUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.me.resume.views.CommScrollView;
import com.me.resume.views.CustomListView;
import com.me.resume.views.RefreshableView;
import com.me.resume.views.RefreshableView.RefreshListener;
import com.whjz.android.text.CommonText;

/**
 * 
 * @Description: 首页
 * @date 2016/3/29 下午4:56:41
 * 
 */
public class HomeActivity extends BaseActivity implements OnClickListener {
	
	private RefreshableView refreshview;
	private CommScrollView commscrollview;
	
	private GridView reviewCovergridview;
	
	private CustomListView reviewsharingListView;
	private ImageView sharemore,linkmore,covermore;
	private TextView msgText;

	private Button makeResume,reviewResume;

	private GridView resumeLinkgridview;
	
	// 构建cover本地数据
	private String[] id = {"1","2","3"};
	private String[] note = {"简历预览封面1","简历预览封面2","简历预览封面3"};
	private String[] url = {R.drawable.default_cover1+"",R.drawable.default_cover2+"",R.drawable.default_cover3+""};
	
	private boolean isExit = false;
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				if(msg.obj != null){
					ImageUtils.getURLImage(mHandler,CommUtil.getHttpLink((String)msg.obj),2);
				}
                break;
            case 2:
            	if(msg.obj!= null){
        			try {
        				ImageUtils.saveImage(self,(Bitmap)msg.obj,Constants.FILENAME);
        				MyApplication.USERAVATORPATH = FileUtils.BASE_PATH + File.separator + MyApplication.USERNAME 
        						+ File.separator + Constants.FILENAME; // 创建用户名文件夹
        				Bitmap bitmap = ImageUtils.getLoacalBitmap(MyApplication.USERAVATORPATH);
        				if (bitmap != null) {
        					setLeftIcon(ImageUtils.toRoundBitmap(bitmap));
        				}
        			} catch (Exception e) {
        				L.e(e.getMessage());
        			}
        		}
            	break;
			case 11:
				startChildActivity(Constants.BASEINFO,false);
				break;
			case 12:
				preferenceUtil.setPreferenceData(Constants.NOTICESHOW,true);
				break;
			case 100:
				if (CommUtil.isNetworkAvailable(self)) {
					getNoticeInfo();
					getReCoverData();
					mHandler.sendEmptyMessage(101); // 请求 分享心得数据
				}else{
					setShareView(false);
					msgText.setText(getStrValue(R.string.item_text5));
				}
				break;
			case 101:
				getShareData();
				break;
			case -1:
				refreshview.finishRefresh();
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
		MyApplication.getApplication().initDisplay(self);
		
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_home, null);
		boayLayout.addView(v);
		
		if (preferenceUtil.getPreferenceFData(Constants.FIRSTINSTALL)) {
			startChildActivity(Constants.GUIDE, true);
			return;
		}else{
			if (preferenceUtil.getPreferenceData(Constants.SET_STARTVERYTIME)) {
				startActivity(Constants.MAINACTIVITY, true);
				return;
			}
		}
		
		findViews();
		setCoverView(true);
		setTopicData();
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				// 请求网络数据
				mHandler.sendEmptyMessage(100);
			}
		},100);
		
	}
	
	/**
	 * 初始化界面
	 */
	private void findViews(){
		setTopTitle(R.string.resume_center);
		setMsgVisibility(View.GONE);
		setRightIconVisible(View.VISIBLE);
		setRight2IconVisible(View.GONE);
		setLeftIcon(R.drawable.icon_person_avtar);
		setRightIcon(R.drawable.icon_setting);
		setfabLayoutVisible(View.GONE);
		
		refreshview = findView(R.id.refreshview);
		commscrollview = findView(R.id.commscrollview);
		
		reviewCovergridview = findView(R.id.covergridview);
		resumeLinkgridview = findView(R.id.linkgridview);

		reviewsharingListView = findView(R.id.reviewshareListView);
		covermore = findView(R.id.covermore);
		linkmore = findView(R.id.linkmore);
		sharemore = findView(R.id.sharemore);
		msgText = findView(R.id.msgText);
		msgText.setVisibility(View.VISIBLE);
		msgText.setText(getStrValue(R.string.item_text43));
		
		makeResume = findView(R.id.make_btn);
		reviewResume = findView(R.id.review_btn);
		
		covermore.setOnClickListener(this);
		linkmore.setOnClickListener(this);
		sharemore.setOnClickListener(this);
		
		makeResume.setOnClickListener(this);
		reviewResume.setOnClickListener(this);
		
		refreshview.setRefreshListener(new RefreshListener() {
			@Override
			public void onRefresh(RefreshableView view) {
				if(CommUtil.isNetworkAvailable(self)){
					if (!view.isRefreshing()) {
//						view.setRefreshText("刷新时间: " + TimeUtils.getCurrentTimeInString());
						mHandler.sendEmptyMessage(101);
					} else {
						L.d("刷新中。。。");
					}
				} else {
					mHandler.sendEmptyMessageDelayed(-1, 1000);
				}
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initData();
		
		MyApplication.USERID = preferenceUtil.getPreferenceData(UserInfoCode.USEID, "0");
		MyApplication.USERNAME = preferenceUtil.getPreferenceData(UserInfoCode.USERNAME,"");
		L.d("======onResume======userId:" + MyApplication.USERID + "## uuid:"+ uTokenId);

		if (!MyApplication.USERID.equals(0)) { // 登录用户显示头像
			MyApplication.USERAVATORPATH = FileUtils.BASE_PATH + File.separator
					+ MyApplication.USERNAME + File.separator
					+ Constants.FILENAME; // 创建用户名文件夹
			Bitmap bitmap = ImageUtils
					.getLoacalBitmap(MyApplication.USERAVATORPATH);
			String avatorStr = preferenceUtil.getPreferenceData(UserInfoCode.AVATOR, "");
			if (bitmap != null && RegexUtil.checkNotNull(avatorStr)) {
				setLeftIcon(ImageUtils.toRoundBitmap(bitmap));
			} else {
				setLeftIcon(R.drawable.icon_person_avtar);
				if (CommUtil.isNetworkAvailable(self)) {
					if (RegexUtil.checkNotNull(avatorStr)) {
						mHandler.sendMessage(mHandler.obtainMessage(1,avatorStr));
					}
				}
			}
		}
		
		if (preferenceUtil.getPreferenceData(UserInfoCode.USERSTATUS)) {
			preferenceUtil.setPreferenceData(UserInfoCode.USERSTATUS, false);
			if(CommUtil.isNetworkAvailable(self)){
				mHandler.sendEmptyMessage(101);
			}
		}
		commscrollview.scrollTo(0, 0);
	}
	
	/**
	 * 初始化用户信息
	 */
	private void initData(){
		String uid = preferenceUtil.getPreferenceData(UserInfoCode.UTOKENID, "0");
		queryWhere = "select * from " + CommonText.USERINFO +" where uid = '"+ uid +"' order by id desc limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		 if (commMapArray!= null && commMapArray.get("id").length > 0) {
			 uTokenId = commMapArray.get("uid")[0];
			 L.d("======初始化用户ID(uTokenId)======="+uTokenId);
			 initBottomButton();
		 }else{
			 String uuid = UUIDGenerator.getUUID();
			 ContentValues cValues = new ContentValues();
			 cValues.put("uid", uuid);
			 cValues.put("deviceid", deviceID);
			 cValues.put("patform", "app");// 默认 app qq,sina...
			 cValues.put("createtime", TimeUtils.getCurrentTimeInString());
			 
			 queryResult = dbUtil.insertData(self, CommonText.USERINFO, cValues);
			 if (queryResult) {
				 preferenceUtil.setPreferenceData(UserInfoCode.UTOKENID, uuid);
				 initData();
				 
				 // 同时将userId插入BASEINFO表
				 cValues = new ContentValues();
				 cValues.put("userId", uTokenId);
				 cValues.put("bgcolor", getCheckColor(checkColor));
				 cValues.put("createtime", TimeUtils.getCurrentTimeInString());
				 queryResult = dbUtil.insertData(self,CommonText.BASEINFO, cValues);
			 }
		 }
	}
	
	/**
	 * 显示底部button
	 */
	private void initBottomButton(){
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = '" + uTokenId +"' limit 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null) {
			String realname = commMapArray.get("realname")[0];
			if (RegexUtil.checkNotNull(realname)) {
				makeResume.setText(getStrValue(R.string.edit_resume));
				reviewResume.setVisibility(View.VISIBLE);
			}else{
				makeResume.setText(getStrValue(R.string.make_resume));
				reviewResume.setVisibility(View.GONE);
			}
		}else{
			makeResume.setText(getStrValue(R.string.make_resume));
			reviewResume.setVisibility(View.GONE);
		}
	}
	
	
	/**
	 * app通知
	 */
	private void getNoticeInfo(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		requestData("pro_get_noticeinfo", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				setMsgVisibility(View.GONE);
//				set2Msg(getStrValue(R.string.app_server_error));
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					set2Msg(map.get("notice").get(0));
				} catch (Exception e) {
					setMsgVisibility(View.GONE);
					L.e(e.getMessage());
				}
			}
		});
	}
	
	
	/**
	 * 初始化封面视图(default)
	 * @param islocal
	 */
	private void setCoverView(boolean islocal){
		Map<String, List<String>> map = new HashMap<String,List<String>>();
		
		List<String> idList = new ArrayList<String>();
		idList = Arrays.asList(id);
		map.put("id",idList);
		
		List<String> noteList = new ArrayList<String>();
		noteList = Arrays.asList(note);
		map.put("note",noteList);
		
		List<String> urlList = new ArrayList<String>();
		urlList = Arrays.asList(url);
		map.put("url",urlList);
		
		setCoverData(reviewCovergridview,map,islocal);
	}
	
	/**
	 * 设置简历面试相关话题数据
	 */
	private void setTopicData(){
		String[] item_text = CommUtil.getArrayValue(self,R.array.review_link); 
		mList = Arrays.asList(item_text);
		commStrAdapter = new CommonBaseAdapter<String>(self, mList,
				R.layout.home_xgln_grilview) {

			@Override
			public void convert(ViewHolder holder, String item,
					final int position) {
				final String[] title = mList.get(position).toString().split(";");
				holder.setText(R.id.itemName, title[0]);
				if (position == 5) {
					holder.setTextColor(R.id.itemName,getColorValue(R.color.red));
				}

				holder.setOnClickEvent(R.id.itemName, new ClickEvent() {

					@Override
					public void onClick(View view) {
						ActivityUtils.startActivityPro(self, 
								Constants.PACKAGENAMECHILD + Constants.TOPICLISTDETAIL, "title",
								title[0]+";"+title[1]);
					}
				});
			}
		};
		resumeLinkgridview.setAdapter(commStrAdapter);
	}
	
	
	/**
	 * 获取简历封面
	 */
	private void getReCoverData(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		requestData("pro_getcover_info", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					setCoverData(reviewCovergridview,map,false);
				} catch (Exception e) {
					L.e(e.getMessage());
				}
			}
		});
	}
	
	/**
	 * 获取 面试分享心得
	 */
	private void getShareData(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		requestData("pro_getshareinfo", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				setShareView(false);
				msgText.setText(getStrValue(R.string.item_text42));
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					setShareView(true);
					setShareData(reviewsharingListView,map);
				} catch (Exception e) {
					L.e(e.getMessage());
				}
			}
		});
	}
	
	/**
	 * 面试分享是否有数据
	 * @param hasdata
	 */
	private void setShareView(boolean hasdata){
		mHandler.sendEmptyMessageDelayed(-1, 100);
		if(hasdata){
			reviewsharingListView.setVisibility(View.VISIBLE);
			sharemore.setVisibility(View.VISIBLE);
			msgText.setVisibility(View.GONE);
		}else{
			reviewsharingListView.setVisibility(View.GONE);
			sharemore.setVisibility(View.GONE);
			msgText.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.make_btn:
			if (MyApplication.USERID.equals("0")) {
				if(!preferenceUtil.getPreferenceData(Constants.NOTICESHOW)){
					DialogUtils.showAlertDialog(self, getStrValue(
							R.string.dialog_action_alert),View.VISIBLE, mHandler);
				}else{
					mHandler.sendEmptyMessage(11);
				}
			} else {
				mHandler.sendEmptyMessage(11);
			}
			break;
		case R.id.review_btn:
			startActivity(Constants.MAINACTIVITY, false);
			break;
		case R.id.left_lable:
			if (MyApplication.USERID.equals("0")) {
				startChildActivity(Constants.USERLOGIN, false);
			}else{
				startChildActivity(Constants.USERCENTER, false);
			}
			break;
		case R.id.right_icon:
			startChildActivity(Constants.SETTING, false);
			break;
		case R.id.sharemore:
			startChildActivity(Constants.RESUMESHAREMORE, false);
			break;
		case R.id.covermore:
			startChildActivity(Constants.RESUMECOVERMORE, false);
			break;
		case R.id.linkmore:
			startChildActivity(Constants.TOPICLISTDETAIL, false);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
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
	
	/**
	 * 两次退出
	 */
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
