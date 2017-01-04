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
import android.text.Html;
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
import com.me.resume.tools.L;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.FileUtils;
import com.me.resume.utils.ImageUtils;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
import com.umeng.analytics.MobclickAgent;
import com.whjz.android.text.CommonText;

/**
 * 
* @ClassName: UserRegProtocal 
* @Description: 用户注册协议 
* @date 2016/4/27 下午12:06:31 
*
 */
public class UserRegProtocalActivity extends BaseActivity implements OnClickListener{

	private TextView protocol;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		bodyLayout.removeAllViews();
		View v = View.inflate(self,R.layout.activity_user_regprotocol_layout, null);
		bodyLayout.addView(v);
		
		setTopTitle(getString(R.string.protocol_title));
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);
		
		protocol = findView(R.id.protocol);
		protocol.setText(Html.fromHtml(getString(R.string.protocol_content)));
	}
	
	@Override
	public void onClick(View v) {
		super.onClick(v);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
}
