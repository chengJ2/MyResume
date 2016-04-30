package com.me.resume.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.comm.Constants;
import com.me.resume.comm.UploadPhotoTask;
import com.me.resume.swipeback.SwipeBackActivity.HandlerData;
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.FileUtils;
import com.me.resume.utils.ImageUtils;

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
	private String[] photoPath = new String[] { "通过本地图库", "通过照相机" };
	private static final int IMAGE_SELECT = 1; // 调用系统图库
	
	protected ImageView user_info_avatar;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			L.d("==what=="+msg.what + " --->>>"+msg.obj);
			switch (msg.what) {
			case 1:
				if(msg.obj != null){
					ImageUtils.getURLImage(mHandler,CommUtil.getHttpLink((String)msg.obj),2);
				}
                break;
            case 2:
            	if(msg.obj!= null){
        			try {
        				ImageUtils.saveImage(self,(Bitmap)msg.obj,Constants.fileName);
        				Bitmap bitmap = ImageUtils.getLoacalBitmap(Constants.userhead.toString());
        				if (bitmap != null) {
        					user_info_avatar.setImageBitmap(ImageUtils.toRoundBitmap(bitmap));
        				}
        			} catch (Exception e) {
        				e.printStackTrace();
        			}
        		}
            	break;
			default:
				break;
			}
		};
	};
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		boayLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_user_center_layout, null);
		boayLayout.addView(v);
		findViews();
		
		setTopTitle(R.string.action_user_regist);
		setMsgHide();
		setRightIconVisible(View.VISIBLE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
	}
	
	private void findViews() {
		user_info_avatar = findView(R.id.user_info_avatar);
		user_info_avatar.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.left_lable:
			self.scrollToFinishActivity();
			break;
		case R.id.right_icon:
			logoutSend();
			break;
		case R.id.user_info_avatar:
			chooseUserHead();
			break;
		default:
			break;
		}
	}
	

	/**
	 * 
	 * @Description: 同步数据
	 * @author Comsys-WH1510032
	 */
	private void logoutSend(){ 
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		
		params.add("p_userId");
		values.add(MyApplication.userId);
		
		requestData("pro_login_out", 1, params, values, new HandlerData() {
			@Override
			public void error() {
				runOnUiThread(R.string.action_logout_fail);
			}
			
			public void success(Map<String, List<String>> map) {
				try {
					if (map.get("msg").get(0).equals("200")) {
						setPreferenceData("useId","");
						startActivity(".ui.HomeActivity",true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	protected void chooseUserHead(){
		new AlertDialog.Builder(self).setTitle(
    			"选择照片路径").setItems(photoPath,
    			new DialogInterface.OnClickListener() {

    				@Override
    				public void onClick(DialogInterface dialog,
    						int which) {
    					switch (which) {
    					case 0:
    						Intent intent = new Intent(
    								Intent.ACTION_PICK,
    								android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    						startActivityForResult(intent, IMAGE_SELECT);
    						break;

    					case 1:
    						if (FileUtils.isSDCardExist()) {
    							Intent intentCamera = new Intent(
    									MediaStore.ACTION_IMAGE_CAPTURE);
    							startActivityForResult(intentCamera,
    									ImageUtils.CEMERA_WITH_DATA);
    						} 
    						break;
    					}
    				}
    			}).show();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		L.d("data----12--:"+data+"--requestCode----2--:"+requestCode);
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
			case IMAGE_SELECT:
				ImageUtils.doCropPhoto(this, data, 128, 128);
				break;
			}
		}
	};
	
	
	
	
	
}
