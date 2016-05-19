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
import com.me.resume.comm.CommForMapBaseAdapter;
import com.me.resume.comm.CommonBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.comm.ViewHolder.ClickEvent;
import com.me.resume.model.UUIDGenerator;
import com.me.resume.tools.L;
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
        				MyApplication.USERAVATORPATH = FileUtils.BASE_PATH + File.separator + MyApplication.USERNAME + File.separator + Constants.FILENAME;
        				Bitmap bitmap = ImageUtils.getLoacalBitmap(MyApplication.USERAVATORPATH);
        				if (bitmap != null) {
        					setLeftIcon(ImageUtils.toRoundBitmap(bitmap));
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		}
            	break;
			case 11:
				startChildActivity("BaseInfoActivity",false);
				break;
			case 12:
				preferenceUtil.setPreferenceData("noticeshow",0);
				break;
			case 100:
				if (CommUtil.isNetworkAvailable(self)) {
					getNoticeInfo();
					getReCoverData();
				}else{
					setShareView(false);
					msgText.setText(CommUtil.getStrValue(self, R.string.item_text5));
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
		
		if (preferenceUtil.getPreferenceData("startVerytime")) {
			startActivity(Constants.MAINACTIVITY, true);
			return;
		}
		
		findViews();
		
		setCoverView(true);
		
		setTopicData();
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				
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
		msgText.setText(CommUtil.getStrValue(self, R.string.item_text43));
		
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
						view.setRefreshText("刷新时间: " + TimeUtils.getCurrentTimeInString());
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

	
	private void initData(){
		String uid = preferenceUtil.getPreferenceData("uid", "0");
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
			 cValues.put("createtime", TimeUtils.getCurrentTimeInString());
			 cValues.put("lastlogintime", TimeUtils.getCurrentTimeInString());
			 
			 queryResult = dbUtil.insertData(self, 
						CommonText.USERINFO, cValues);
			 if (queryResult) {
				 preferenceUtil.setPreferenceData("uid", uuid);
				 initData();
			 }
		 }
	}
	
	/**
	 * 显示底部button
	 */
	private void initBottomButton(){
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = '" + uTokenId +"'";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null) {
			String realname = commMapArray.get("realname")[0];
			if (RegexUtil.checkNotNull(realname)) {
				makeResume.setText(CommUtil.getStrValue(self, R.string.edit_resume));
				reviewResume.setVisibility(View.VISIBLE);
			}else{
				makeResume.setText(CommUtil.getStrValue(self, R.string.make_resume));
				reviewResume.setVisibility(View.GONE);
			}
		}else{
			makeResume.setText(CommUtil.getStrValue(self, R.string.make_resume));
			reviewResume.setVisibility(View.GONE);
		}
	}
	
	
	/**
	 * @Description: app通知
	 */
	private void getNoticeInfo(){
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		requestData("pro_get_noticeinfo", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				setMsgVisibility(View.GONE);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					set2Msg(map.get("notice").get(0));
				} catch (Exception e) {
					setMsgVisibility(View.GONE);
					e.printStackTrace();
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
		
		setCoverData(map,islocal);
	}
	
	private String[] id = {"111","222","333"};
	private String[] note = {"简历预览封面1","简历预览封面2","简历预览封面3"};
	private String[] url = {R.drawable.resume_cover+"",R.drawable.resume_cover+"",R.drawable.resume_cover+""};
	
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
					holder.setTextColor(R.id.itemName,
							CommUtil.getIntValue(self, R.color.red));
				}

				holder.setOnClickEvent(R.id.itemName, new ClickEvent() {

					@Override
					public void onClick(View view) {
						ActivityUtils.startActivityPro(self, Constants.PACKAGENAMECHILD + "TopicActivity", "title",
								title[0]+";"+title[1]);

					}
				});
			}
		};

		resumeLinkgridview.setAdapter(commStrAdapter);
	}
	
	
	/**
	 * @Description: 面试分享心得
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
					mHandler.sendEmptyMessage(101);
					setCoverData(map,false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * 简历预览封面
	 * @param map
	 */
	private void setCoverData(final Map<String, List<String>> map,final boolean isLocal){
		commapBaseAdapter = new CommForMapBaseAdapter(self,map,R.layout.home_cover_gridview_item,"id") {
			
			@Override
			public void convert(ViewHolder holder, List<String> item, int position) {
				if (isLocal) {
					holder.setImageResource(R.id.item1,CommUtil.parseInt(map.get("url").get(position)));
				}else{
					holder.showImage(R.id.item1,
							CommUtil.getHttpLink(map.get("url").get(position)),false);
				}
				holder.setText(R.id.item2, map.get("note").get(position));
				
				holder.setOnClickEvent(R.id.item3, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		};
		
		reviewCovergridview.setAdapter(commapBaseAdapter);
	}
	/**
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
					mHandler.sendEmptyMessageDelayed(-1, 1000);
					setShareView(true);
					setShareData(map);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * @Description: 面试分享心得
	 */
	private void setShareData(final Map<String, List<String>> map){
		commapBaseAdapter = new CommForMapBaseAdapter(self,map,R.layout.home_share_item,"id") {
			
			@Override
			public void convert(final ViewHolder holder, List<String> item, final int position) {
				
				if(map.get("userId").get(position).equals(MyApplication.USERID)){
					if (FileUtils.existsFile(MyApplication.USERAVATORPATH)) {
						Bitmap bitmap = ImageUtils.getLoacalBitmap(MyApplication.USERAVATORPATH);
	    				if (bitmap != null) {
	    					holder.setImageBitmap(R.id.share_usernameavator,ImageUtils.toRoundBitmap(bitmap));
	    				}
					}
				}else{
					holder.showImage(R.id.share_usernameavator,
							CommUtil.getHttpLink(map.get("avator").get(position)),true);
				}
				
				String realname = map.get("realname").get(position);
				if (!realname.equals("") && realname != null) {
					holder.setText(R.id.share_username, map.get("realname").get(position));
				}else{
					holder.setText(R.id.share_username, map.get("username").get(position));
				}
				
				String jobtitleStr = map.get("expworkindustry").get(position);
				if (RegexUtil.checkNotNull(jobtitleStr)) {
					holder.setTextVisibe(R.id.share_jobtitle, View.VISIBLE);
					holder.setText(R.id.share_jobtitle, jobtitleStr);
				}else{
					holder.setTextVisibe(R.id.share_jobtitle, View.GONE);
				}
				
				String workyear = map.get("joinworktime").get(position);
				if (RegexUtil.checkNotNull(workyear)) {
					int year = CommUtil.parseInt(workyear.substring(0, 4));
					int theYear = CommUtil.parseInt(TimeUtils.theYear());
					holder.setTextVisibe(R.id.share_workyear, View.VISIBLE);
					holder.setText(R.id.share_workyear,(theYear - year) + "年工作经验");
				}else{
					holder.setTextVisibe(R.id.share_workyear, View.GONE);
				}
				
				final String content = map.get("content").get(position);
				holder.setText(R.id.share_content, content);
				holder.setText(R.id.share_city, map.get("city").get(position));
				holder.setText(R.id.share_datime, map.get("createtime").get(position));
				
				queryWhere = "select * from " + CommonText.MYCOLLECTION + " where content = '" + content +"'";
				commMapArray = dbUtil.queryData(self, queryWhere);
				if (commMapArray == null) {
					holder.setImageResource(R.id.share_collection, R.drawable.icon_collection_nor);
				}else{
					holder.setImageResource(R.id.share_collection, R.drawable.icon_collection_sel);
				}
				
				holder.setOnClickEvent(R.id.share_collection, new ClickEvent() {
					
					@Override
					public void onClick(View view) {
						queryWhere = "select * from " + CommonText.MYCOLLECTION + " where content = '" + content +"'";
						commMapArray = dbUtil.queryData(self, queryWhere);
						if (commMapArray == null) {
							if (!MyApplication.USERID.equals("0")) {
								addCollection(holder,map,position);
							}else{
								toastMsg(R.string.action_login_head);
							}
						}else{
							queryWhere = "delete from " + CommonText.MYCOLLECTION + " where content = '" + content +"'";
							dbUtil.deleteData(self, queryWhere);
							commapBaseAdapter.notifyDataSetChanged();
							toastMsg(R.string.item_text91);
						}
						
					}
				});
			}
		};
		
		reviewsharingListView.setAdapter(commapBaseAdapter);
	}
	
	/**
	 * 添加到我的收藏
	 * @param holder
	 * @param map
	 * @param position
	 */
	private void addCollection(ViewHolder holder,Map<String, List<String>> map,int position){
		String content = map.get("content").get(position);
		String sharename = map.get("realname").get(position);
		if (RegexUtil.checkNotNull(sharename)) {
			sharename = map.get("username").get(position);
		}
		
		queryWhere = "select * from " + CommonText.MYCOLLECTION + " where content = '" + content +"'";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray == null) {
			 ContentValues cValues = new ContentValues();
			 cValues.put("userId", map.get("userId").get(position));
			 cValues.put("title", "");
			 cValues.put("content", content);
			 cValues.put("sharename", sharename);
			 cValues.put("sharenamecity", map.get("city").get(position));
			 cValues.put("sharedatime", TimeUtils.getCurrentTimeInString());
			 cValues.put("type", "0");// 0:面试分享心得;  !0:话题
			 
			 queryResult = dbUtil.insertData(self, 
						CommonText.MYCOLLECTION, cValues);
			 if (queryResult) {
				 toastMsg(R.string.item_text9);
				 commapBaseAdapter.notifyDataSetChanged();
			 }
		}
	}
	
	
	private void setShareView(boolean hasdata){
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
	protected void onResume() {
		super.onResume();
		MyApplication.USERID = preferenceUtil.getPreferenceData("useId", "0");
		MyApplication.USERNAME = preferenceUtil.getPreferenceData("username", "");
		L.d("======onResume======userId:"+MyApplication.USERID + "## uuid:" + uTokenId);
		
		initData();
		MyApplication.USERAVATORPATH = FileUtils.BASE_PATH + File.separator + MyApplication.USERNAME + File.separator + Constants.FILENAME;
		Bitmap bitmap = ImageUtils.getLoacalBitmap(MyApplication.USERAVATORPATH);
		String avatorStr= preferenceUtil.getPreferenceData("avator", "");
		if (bitmap != null && RegexUtil.checkNotNull(avatorStr)) {
			setLeftIcon(ImageUtils.toRoundBitmap(bitmap));
		}else{
			setLeftIcon(R.drawable.icon_person_avtar);
			if (CommUtil.isNetworkAvailable(self)) {
				if(RegexUtil.checkNotNull(avatorStr)){
					mHandler.sendMessage(mHandler.obtainMessage(1, avatorStr));
				}
			}
		}
		
		commscrollview.scrollTo(0, 0);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.make_btn:
			if (MyApplication.USERID.equals("0")) {
				if(preferenceUtil.getPreferenceData("noticeshow",1) == 1){
					DialogUtils.showAlertDialog(self, CommUtil.getStrValue(self,
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
				startChildActivity("UserLoginActivity", false);
			}else{
				startChildActivity("UserCenterActivity", false);
			}
			break;
		case R.id.right_icon:
			startChildActivity("SettingActivity", false);
			break;
		case R.id.sharemore:
			startChildActivity("ResumeShareMoreActivity", false);
			break;
		case R.id.covermore:
			startChildActivity("ResumeCoverMoreActivity", false);
			break;
		case R.id.linkmore:
			startChildActivity("TopicListDetailActivity", false);
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
