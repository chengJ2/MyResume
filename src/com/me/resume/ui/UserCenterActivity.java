package com.me.resume.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.OnTopMenu;
import com.me.resume.comm.ResponseCode;
import com.me.resume.comm.UploadPhotoTask;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.FileUtils;
import com.me.resume.utils.ImageUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: UserCenterActivity 
* @Description: 用户中心 
* @date 2016/4/27 下午12:06:31 
*
 */
public class UserCenterActivity extends BaseActivity implements OnClickListener{

	protected File pictureFile = null;
	
	private RelativeLayout center_topbar;
	private TextView center_top_text;
	private ImageView left_back;
	private ImageView right_lable;
	
	private LinearLayout llout01,llout02,llout03;
	
	protected ImageView user_info_avatar;
	private TextView center_username,center_workyear,center_city;
	
	private RelativeLayout viewcalendar;
	private TextView mycollection,review_resume,view_day;
	
	private LinearLayout info_layout1,info_layout2,info_layout3,info_layout4;
	private TextView info_item1,info_item2,info_item3,info_item4;
	
	private TextView resume_complete,resume_updatime;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				ImageUtils.getURLImage(mHandler, CommUtil.getHttpLink((String)msg.obj), 2);
				break;
            case 2:
            	if(msg.obj!= null){
        			try {
        				MyApplication.USERNAME = preferenceUtil.getPreferenceData(UserInfoCode.USERNAME, "");
        				MyApplication.USERAVATORPATH = FileUtils.BASE_PATH + File.separator + MyApplication.USERNAME + File.separator + Constants.FILENAME;
        				ImageUtils.saveImage(self,(Bitmap)msg.obj,Constants.FILENAME);
        				Bitmap bitmap = ImageUtils.getLoacalBitmap(MyApplication.USERAVATORPATH.toString());
        				if (bitmap != null) {
        					blur(bitmap);
        					user_info_avatar.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
        					preferenceUtil.setPreferenceData(UserInfoCode.CHANGEAVATOR, true);
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		}
            	break;
            case OnTopMenu.MSG_MENU41:
            	Intent intent = new Intent(
						Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, ImageUtils.IMAGE_SELECT);
            	break;
            case OnTopMenu.MSG_MENU42:
            	if (FileUtils.isSDCardExist()) {
					Intent intentCamera = new Intent(
							MediaStore.ACTION_IMAGE_CAPTURE);
					startActivityForResult(intentCamera,
							ImageUtils.CEMERA_WITH_DATA);
				} 
            	break;
            case 100:
            	initViews();
            	break;
            case 101:
            	setAnimView(center_topbar,0);
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
		View v = View.inflate(self,R.layout.activity_user_center_layout, null);
		boayLayout.addView(v);
		findViews();
		
		setMsgHide();
		setTopBarVisibility(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		mHandler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				mHandler.sendEmptyMessage(100);
			}
		},100);

	}
	
	private void findViews() {
		center_topbar = findView(R.id.center_topbar);
		setAnimView(center_topbar,1);
		center_top_text = findView(R.id.center_top_text);
		left_back = findView(R.id.left_back);
		right_lable = findView(R.id.right_lable);
		center_top_text.setText(getStrValue(R.string.personal_center));
		
		mHandler.sendEmptyMessageDelayed(101,Constants.DEFAULTIME);
		
		llout01 = findView(R.id.llout01);
		llout02 = findView(R.id.llout02);
		llout03 = findView(R.id.llout03);
		user_info_avatar = findView(R.id.user_info_avatar);
		user_info_avatar.setOnClickListener(this);
		
		center_username = findView(R.id.center_username);
		center_workyear = findView(R.id.center_workyear);
		center_city = findView(R.id.center_city);
		
		mycollection = findView(R.id.mycollection);
		viewcalendar = findView(R.id.viewcalendar);
		view_day = findView(R.id.day);
		view_day.setText(TimeUtils.theToday());
		review_resume = findView(R.id.review_resume);
		
		center_topbar.setOnClickListener(this);
		left_back.setOnClickListener(this);
		right_lable.setOnClickListener(this);
		llout01.setOnClickListener(this);
		llout02.setOnClickListener(this);
		llout03.setOnClickListener(this);
		
		resume_complete = findView(R.id.resume_complete);
		resume_updatime = findView(R.id.resume_updatime);
		
		info_layout1 = findView(R.id.info_layout1);
		info_layout2 = findView(R.id.info_layout2);
		info_layout3 = findView(R.id.info_layout3);
		info_layout4 = findView(R.id.info_layout4);
		
		info_item1 = findView(R.id.info_item1);
		info_item2 = findView(R.id.info_item2);
		info_item3 = findView(R.id.info_item3);
		info_item4 = findView(R.id.info_item4);
		
		mycollection.setOnClickListener(this);
		viewcalendar.setOnClickListener(this);
		review_resume.setOnClickListener(this);
		
		info_layout1.setOnClickListener(this);
		info_layout2.setOnClickListener(this);
		info_layout3.setOnClickListener(this);
		info_layout4.setOnClickListener(this);
	}
	
	/**
	 * 显示用户头像
	 */
	private void initViews(){	
		MyApplication.USERNAME = preferenceUtil.getPreferenceData(UserInfoCode.USERNAME, "");
		MyApplication.USERAVATORPATH = FileUtils.BASE_PATH + File.separator 
				+ MyApplication.USERNAME + File.separator + Constants.FILENAME;
		
		Bitmap bitmap = ImageUtils.getLoacalBitmap(MyApplication.USERAVATORPATH);
		String avatorStr= preferenceUtil.getPreferenceData("avator", "");
		if (bitmap != null && RegexUtil.checkNotNull(avatorStr)) {
			blur(bitmap);
			user_info_avatar.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
		}else{
			if (CommUtil.isNetworkAvailable(self)) {
				if(RegexUtil.checkNotNull(avatorStr)){
					mHandler.sendMessage(mHandler.obtainMessage(1, avatorStr));
				}
			}
		}
		
	}
	
	/**
	 * 获取基本资料
	 */
	private void getBaseInfo(){
		queryWhere = "select a.username,b.* from " + CommonText.USERINFO + " a,"
				+ CommonText.BASEINFO +" b where a.uid = b.userId and a.uid = '"+ uTokenId +"'";
		Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			String realname = commMapArray.get("realname")[0];
			if (RegexUtil.checkNotNull(realname)) {
				center_username.setText(realname);
			}else{
				center_username.setText(commMapArray.get("username")[0]);
			}
			
			String workyear = commMapArray.get("joinworktime")[0];
			if (RegexUtil.checkNotNull(workyear)) {
				int year = CommUtil.parseInt(workyear.substring(0, 4));
				int theYear = CommUtil.parseInt(TimeUtils.theYear());
				int work = (theYear - year);
				if (work <= 0) {
					center_workyear.setText("|"+ getStrValue(R.string.personal_c_item17));
				}else{
					center_workyear.setText("|"+ String.format(getStrValue(R.string.personal_c_item18), work));
				}
			}else{
				center_workyear.setText("");
			}
			
			String city = commMapArray.get("city")[0];
			if (RegexUtil.checkNotNull(city)) {
				center_city.setText("|"+city); 
			}else{
				center_city.setText("");
			}
		}
	}
	
	/**
	 * 
	 * @Description:基本信息完整度
	 */
	private void getBaseInfoComplete(){
		resume_updatime.setText(
				String.format(getStrValue(R.string.personal_c_item16),
						preferenceUtil.getPreferenceData(UserInfoCode.RESUMEUPDTIME,
								TimeUtils.getCurrentTimeString())));
		queryWhere = "select (a1+a2+a3+a4+a5+a6+a7+a8+a9+a10+a11) num"
					+ " from ("
					+ " select case when realname='' then 1 else 0 end a1,"
					+ " case when gender='' then 1 else 0 end a2,"
					+ " case when brithday='' then 1 else 0 end a3,"
					+ " case when joinworktime='' then 1 else 0 end a4,"
					+ " case when city='' then 1 else 0 end a5,"
					+ " case when email='' then 1 else 0 end a6,"
					+ " case when ismarry='' then 1 else 0 end a7,"
					+ " case when nationality='' then 1 else 0 end a8,"
					+ " case when license='' then 1 else 0 end a9,"
					+ " case when workingabroad='' then 1 else 0 end a10,"
					+ " case when politicalstatus='' then 1 else 0 end a11"
					+ " from "+ CommonText.BASEINFO +" where userId = '"+ uTokenId +"') a";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!=null && commMapArray.get("num").length>0) {
			int num = CommUtil.parseInt(commMapArray.get("num")[0]);
			int nPercent = 0;
			nPercent = (int) (((11 - num) * 100 / 11));
			if (nPercent <= 0)
				nPercent = 0;
			resume_complete.setText(String.format(getStrValue(R.string.personal_c_item14), nPercent));
			if (nPercent >= 100){
				resume_complete.setText(getStrValue(R.string.personal_c_item11));
			}
		}else{
			resume_complete.setText(getStrValue(R.string.personal_c_item13));
		}
		
	}
	
	/**
	 * 
	 * @Description:求职意向完整度
	 */
	private void getJobIntensionComplete(){
		queryWhere = "select (a1+a2+a3+a4+a5+a6) num"
					+ " from ("
					+ " select case when expworkingproperty='' then 1 else 0 end a1,"
					+ " case when expdworkplace='' then 1 else 0 end a2,"
					+ " case when expworkindustry='' then 1 else 0 end a3,"
					+ " case when expworkcareer='' then 1 else 0 end a4,"
					+ " case when expmonthlysalary='' then 1 else 0 end a5,"
					+ " case when workingstate='' then 1 else 0 end a6"
					+ " from "+ CommonText.JOBINTENSION +" where userId = '"+ uTokenId +"') a";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!=null && commMapArray.get("num").length>0) {
			int num = CommUtil.parseInt(commMapArray.get("num")[0]);
			int nPercent = 0;
			nPercent = (int) (((6 - num) * 100 / 6));
			if (nPercent <= 0)
				nPercent = 0;
			info_item1.setText(String.format(getStrValue(R.string.personal_c_item14), nPercent));
			if (nPercent >= 100){
				info_item1.setText(getStrValue(R.string.personal_c_item11));
			}
		}else{
			info_item1.setText(getStrValue(R.string.personal_c_item13));
		}
		
	}
	
	/**
	 * 
	 * @Description:教育背景完整度
	 */
	private void getEducationComplete(){
		queryWhere = "select (a1+a2+a3+a4+a5+a6) num"
					+ " from ("
					+ " select case when educationtimestart='' then 1 else 0 end a1,"
					+ " case when educationtimeend='' then 1 else 0 end a2,"
					+ " case when school='' then 1 else 0 end a3,"
					+ " case when examination='' then 1 else 0 end a4,"
					+ " case when majorname='' then 1 else 0 end a5,"
					+ " case when degree='' then 1 else 0 end a6"
					+ " from "+ CommonText.EDUCATION +" where userId = '"+ uTokenId +"') a";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!=null && commMapArray.get("num").length>0) {
			int num = CommUtil.parseInt(commMapArray.get("num")[0]);
			int nPercent = 0;
			nPercent = (int) (((6 - num) * 100 / 6));
			if (nPercent <= 0)
				nPercent = 0;
			info_item2.setText(String.format(getStrValue(R.string.personal_c_item14), nPercent));
			if (nPercent >= 100){
				info_item2.setText(getStrValue(R.string.personal_c_item11));
			}
		}else{
			info_item2.setText(getStrValue(R.string.personal_c_item13));
		}
	}
	
	
	/**
	 * 
	 * @Description:工作实习完整度
	 */
	private void getWorkExperienceComplete(){
		queryWhere = "select (a1+a2+a3+a4+a5+a6+a7+a8+a9) num"
					+ " from ("
					+ " select case when companyname='' then 1 else 0 end a1,"
					+ " case when companynature='' then 1 else 0 end a2,"
					+ " case when companyscale='' then 1 else 0 end a3,"
					+ " case when industryclassification='' then 1 else 0 end a4,"
					+ " case when jobtitle='' then 1 else 0 end a5,"
					+ " case when worktimestart='' then 1 else 0 end a6,"
					+ " case when worktimeend='' then 1 else 0 end a7,"
					+ " case when expectedsalary='' then 1 else 0 end a8,"
					+ " case when workdesc='' then 1 else 0 end a9"
					+ " from "+ CommonText.WORKEXPERIENCE +" where userId = '"+ uTokenId +"') a";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!=null && commMapArray.get("num").length>0) {
			int num = CommUtil.parseInt(commMapArray.get("num")[0]);
			int nPercent = 0;
			nPercent = (int) (((9 - num) * 100 / 9));
			if (nPercent <= 0)
				nPercent = 0;
			info_item3.setText(String.format(getStrValue(R.string.personal_c_item14), nPercent));
			if (nPercent >= 100){
				info_item3.setText(getStrValue(R.string.personal_c_item11));
			}
		}else{
			info_item3.setText(getStrValue(R.string.personal_c_item13));
		}
	}
	
	
	/**
	 * 
	 * @Description:项目经验
	 */
	private void getProjectExperienceComplete(){
		queryWhere = "select count(*) as num from "+ CommonText.PROJECT_EXPERIENCE +" where userId = '"+ uTokenId +"'";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray!=null && commMapArray.get("num").length>0) {
			int num = CommUtil.parseInt(commMapArray.get("num")[0]);
			if (num > 0) {
				info_item4.setText(String.format(getStrValue(R.string.personal_c_item15), num));
			}else{
				info_item4.setText(getStrValue(R.string.personal_c_item13));
			}
		}else{
			info_item4.setText(getStrValue(R.string.personal_c_item13));
		}
	}
	
	/**
	 * 背景虚化
	 * @param bkg
	 */
	private void blur(Bitmap bkg) {   
 	    long startMs = System.currentTimeMillis();   
 	    float radius = 20;   
 	  
 	    bkg = ImageUtils.small(bkg);  
 	    Bitmap bitmap = bkg.copy(bkg.getConfig(), true);  
 	  
 	    final RenderScript rs = RenderScript.create(self);  
 	    final Allocation input = Allocation.createFromBitmap(rs, bkg, Allocation.MipmapControl.MIPMAP_NONE,  
 	            Allocation.USAGE_SCRIPT);  
 	    final Allocation output = Allocation.createTyped(rs, input.getType());  
 	    final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));  
 	    script.setRadius(radius);  
 	    script.setInput(input);  
 	    script.forEach(output);  
 	    output.copyTo(bitmap);  
 	  
 	    bitmap = ImageUtils.big(bitmap);  
 	    
 	    llout01.setBackgroundDrawable(new BitmapDrawable(getResources(), bitmap));
 	    
 	    rs.destroy();   
 	    L.d("blur take away:" + (System.currentTimeMillis() - startMs )+ "ms");   
 	}   
	
	@Override
	protected void onResume() {
		super.onResume();
		getBaseInfo();
		getBaseInfoComplete();
		getJobIntensionComplete();
		getEducationComplete();
		getWorkExperienceComplete();
		getProjectExperienceComplete();
	}
	
	private boolean topbarView = false;
	
	/**
	 * View切换动画
	 * @param v
	 * @param visible
	 */
	private void setAnimView(View v,int visible){
		AlphaAnimation dismiss = new AlphaAnimation(0, 1);
		if (visible == 0) {
			dismiss = new AlphaAnimation(1, 0);
		}
		dismiss.setDuration(1000);
		dismiss.setFillAfter(true);
		v.startAnimation(dismiss);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.llout01:
		case R.id.center_topbar:
			topbarView = !topbarView;
			if(topbarView){
				setAnimView(center_topbar,1);
			}else{
				setAnimView(center_topbar,0);
			}
			break;
		case R.id.left_back:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			actionLogout();
			break;
		case R.id.user_info_avatar:
			DialogUtils.showPhotoPathDialog(self, user_info_avatar, mHandler);
			break;
		case R.id.llout02:
		case R.id.llout03:
			startChildActivity(Constants.BASEINFO,false);
			break;
		case R.id.mycollection:
			startChildActivity(Constants.MYCOLLECTION,false);
			break;
		case R.id.viewcalendar:
			// TODO
			break;
		case R.id.review_resume:
			startActivity(Constants.MAINACTIVITY,false);
			break;
		case R.id.info_layout1:
			startChildActivity(Constants.JOBINTENSION,false);
			break;
		case R.id.info_layout2: // 教育经历
			startChildActivity(Constants.EDUCATION,false);
			break;
		case R.id.info_layout3:// 工作经历
			startChildActivity(Constants.WORKEXPERIENCE,false);
			break;
		case R.id.info_layout4: // 项目经验
			startChildActivity(Constants.PROJECTEXPERIENCE,false);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	 * @Description: 注销用户
	 * @author Comsys-WH1510032
	 */
	private void actionLogout(){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_userId");
		values.add(uTokenId);
		
		requestData("pro_user_loginout", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				toastMsg(R.string.action_logout_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("msg").get(0).equals(ResponseCode.RESULT_OK)) {
						preferenceUtil.setPreferenceData(UserInfoCode.AVATOR, "");
						preferenceUtil.setPreferenceData(UserInfoCode.USEID,"0");
						preferenceUtil.setPreferenceData(UserInfoCode.ISREGISTER, false);
						MyApplication.USERID = "0";
//						if(FileUtils.existsFile(MyApplication.USERAVATORPATH)){
//							new File(MyApplication.USERAVATORPATH).delete();
//			        	}
						toastMsg(R.string.action_logout_success);
						scrollToFinishActivity();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (data == null) {
			return;
		} else {
			switch (requestCode) {
			case ImageUtils.CROP_WITH_DATA:
				pictureFile = ImageUtils.getPhotoFile(data);
				if (pictureFile.length() <= 1048576){
					new UploadPhotoTask(self,mHandler).execute(pictureFile.toString());
				}else {
					pictureFile = null;
					toastMsg(R.string.max_photo_size);
			    }
				break;
			case ImageUtils.CEMERA_WITH_DATA:
				ImageUtils.doCropPhoto(this, data, 128, 128);
				break;
			case ImageUtils.IMAGE_SELECT:
				ImageUtils.doCropPhoto(this, data, 128, 128);
				break;
			}
		}
	};
	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
	}
}
