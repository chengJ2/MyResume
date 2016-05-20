package com.me.resume.ui;

import java.io.File;
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
import com.me.resume.comm.UploadPhotoTask;
import com.me.resume.tools.L;
import com.me.resume.utils.ActivityUtils;
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
* @author Comsys-WH1510032 
* @date 2016/4/27 下午12:06:31 
*
 */
public class UserCenterActivity extends BaseActivity implements OnClickListener{

	protected File pictureFile = null;
	
	private RelativeLayout center_topbar;
	private TextView top_text;
	private ImageView left_back;
	
	private LinearLayout llout01,llout02;
	
	protected ImageView user_info_avatar;
	private TextView center_username,center_workyear,center_city;
	
	private TextView mycollection,viewmode,review_resume;
	
	private LinearLayout info_layout1,info_layout2,info_layout3;
	private TextView info_item1,info_item2,info_item3;
	
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
            case 2:
            	if(msg.obj!= null){
        			try {
        				MyApplication.USERNAME = preferenceUtil.getPreferenceData("username", "");
        				MyApplication.USERAVATORPATH = FileUtils.BASE_PATH + File.separator + MyApplication.USERNAME + File.separator + Constants.FILENAME;
        				ImageUtils.saveImage(self,(Bitmap)msg.obj,Constants.FILENAME);
        				Bitmap bitmap = ImageUtils.getLoacalBitmap(MyApplication.USERAVATORPATH.toString());
        				if (bitmap != null) {
        					blur(bitmap);
        					user_info_avatar.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
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
		
		setTopBarVisibility(View.GONE);
		top_text.setText(CommUtil.getStrValue(self, R.string.personal_center));
		
		setMsgHide();
		setRightIconVisible(View.VISIBLE);
		setRightIcon(R.drawable.icon_setting);
		setRight2IconVisible(View.GONE);
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
		top_text = findView(R.id.top_text);
		left_back = findView(R.id.left_back);
		
		center_topbar.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				center_topbar.setVisibility(View.INVISIBLE);
				setAnimView(center_topbar,0);
			}
		}, 1500);
		
		llout01 = findView(R.id.llout01);
		llout02 = findView(R.id.llout02);
		user_info_avatar = findView(R.id.user_info_avatar);
		user_info_avatar.setOnClickListener(this);
		
		center_username = findView(R.id.center_username);
		center_workyear = findView(R.id.center_workyear);
		center_city = findView(R.id.center_city);
		
		mycollection = findView(R.id.mycollection);
		viewmode = findView(R.id.viewmode);
		review_resume = findView(R.id.review_resume);
		
		center_topbar.setOnClickListener(this);
		left_back.setOnClickListener(this);
		llout01.setOnClickListener(this);
		llout02.setOnClickListener(this);
		
		info_layout1 = findView(R.id.info_layout1);
		info_layout2 = findView(R.id.info_layout2);
		info_layout3 = findView(R.id.info_layout3);
		
		info_item1 = findView(R.id.info_item1);
		info_item2 = findView(R.id.info_item2);
		info_item3 = findView(R.id.info_item3);
		
		mycollection.setOnClickListener(this);
		viewmode.setOnClickListener(this);
		review_resume.setOnClickListener(this);
		
		info_layout1.setOnClickListener(this);
		info_layout2.setOnClickListener(this);
		info_layout3.setOnClickListener(this);
	}
	
	private void initViews(){	
		MyApplication.USERNAME = preferenceUtil.getPreferenceData("username", "");
		MyApplication.USERAVATORPATH = FileUtils.BASE_PATH + File.separator + MyApplication.USERNAME + File.separator + Constants.FILENAME;
		
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
	 * 
	 * @Title:UserCenterActivity
	 * @Description: 获取基本资料
	 * @author Comsys-WH1510032
	 * @return 返回类型
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
				center_workyear.setText("|"+ (theYear - year) + "年工作经验");
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
	 * @Title:UserCenterActivity
	 * @Description:求职意向完整度
	 * @author Comsys-WH1510032
	 * @return 返回类型
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
			if (nPercent >= 100){
				info_item1.setText("已完整");
			}
			if (nPercent <= 0)
				nPercent = 0;
			info_item1.setText("完整度("+nPercent +"%)");
		}else{
			info_item1.setText("未填写");
		}
	}
	
	/*public void stopAudioThread() {
		if (athread != null) {
			athread.stopThread();
			boolean retry = true;
			while (retry) {
				try {
					athread.join();
					retry = false;
				} catch (InterruptedException e) {
					L.e(e);
				}
			}
			athread = null;
		}
	}*/
	
	
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
		getJobIntensionComplete();
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
//		case R.id.right_icon:
//			startChildActivity(Constants.SETTING, false);
//			break;
		case R.id.user_info_avatar:
			DialogUtils.showPhotoPathDialog(self, user_info_avatar, mHandler);
			break;
		case R.id.llout02:
			startChildActivity(Constants.BASEINFO,false);
			break;
		case R.id.mycollection:
			startChildActivity(Constants.MYCOLLECTION,false);
//			ActivityUtils.startActivityPro(self,
//					Constants.PACKAGENAMECHILD +Constants.INFOMANAGER,"type",CommonText.MYCOLLECTION);
			break;
		case R.id.viewmode:
			// TODO
			break;
		case R.id.review_resume:
			startActivity(Constants.MAINACTIVITY,false);
			break;
		case R.id.info_layout1:
			startChildActivity(Constants.JOBINTENSION,false);
			break;
		default:
			break;
		}
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
