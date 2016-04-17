package com.me.resume.ui;

import java.util.Arrays;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.DialogUtils;
import com.me.resume.utils.TimeUtils;
import com.whjz.android.text.CommonText;

/**
 * 
 * @ClassName: OtherInfoActivity
 * @Description: 其他信息
 * @author Comsys-WH1510032
 * @date 2016/4/6 下午1:32:58
 * 
 */
public class OtherInfoActivity extends SwipeBackActivity implements
		OnClickListener {

	private TextView toptext, leftLable, rightLable;

	private TextView info_language, info_literacyskills,
			info_listeningspeaking;

	private TextView ot_languages_edit, ot_languages_add;
	
	private TextView info_certificate,info_certificatetime;

	private TextView ot_certificate_edit, ot_certificate_add;

	private TextView info_title;
	private EditText info_description;
	
	private TextView ot_otherinfo_edit, ot_otherinfo_add;

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				break;
			case 2:
				int position = (int) msg.obj;
				if (whichTab == 1) {
					info_language.setText(mList.get(position));
					info_literacyskills.setEnabled(true);
					info_listeningspeaking.setEnabled(true);
				} else if (whichTab == 2) {
					info_literacyskills.setText(mList.get(position));
				} else if (whichTab == 3) {
					info_listeningspeaking.setText(mList.get(position));
				}
				break;
			case 13:
				if (msg.obj != null) {
					info_certificatetime.setText(((String)msg.obj)/*.substring(0, 4)*/);
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
		setContentView(R.layout.activity_otherinfo_layout);

		findViews();

		initViews();

	}

	private void findViews() {
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);

		info_language = findView(R.id.info_language);
		info_literacyskills = findView(R.id.info_literacyskills);
		info_listeningspeaking = findView(R.id.info_listeningspeaking);

		ot_languages_edit = findView(R.id.ot_languages_edit);
		ot_languages_add = findView(R.id.ot_languages_add);
		

		info_certificate = findView(R.id.info_certificate);;
		info_certificatetime = findView(R.id.info_certificatetime);;
		
		ot_certificate_edit = findView(R.id.ot_certificate_edit);
		ot_certificate_add = findView(R.id.ot_certificate_add);

		info_title = findView(R.id.info_title);
		info_description = findView(R.id.info_description);
		
		ot_otherinfo_edit = findView(R.id.ot_otherinfo_edit);
		ot_otherinfo_add = findView(R.id.ot_otherinfo_add);

		info_language.setOnClickListener(this);
		info_literacyskills.setOnClickListener(this);
		info_listeningspeaking.setOnClickListener(this);

		ot_languages_add.setOnClickListener(this);
		ot_languages_edit.setOnClickListener(this);

		ot_certificate_edit.setOnClickListener(this);
		ot_certificate_add.setOnClickListener(this);
		
		info_certificatetime.setOnClickListener(this);

		ot_otherinfo_edit.setOnClickListener(this);
		ot_otherinfo_add.setOnClickListener(this);
	}

	private void initViews() {
		toptext.setText(CommUtil.getStrValue(self, R.string.resume_otherinfo));
		rightLable.setText(CommUtil.getStrValue(self, R.string.review_resume));

		if (CommUtil.textIsNull(info_language)) {
			info_literacyskills.setEnabled(false);
			info_listeningspeaking.setEnabled(false);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			startActivity(".MainActivity", false);
			break;
		case R.id.info_language:
			whichTab = 1;
			getValues(R.array.oi_wysp_values, info_language,
					R.string.ot_info_language);
			break;
		case R.id.info_literacyskills:
			whichTab = 2;
			getValues(R.array.oi_tsdx_values, info_literacyskills,
					R.string.ot_info_literacyskills);
			break;
		case R.id.info_listeningspeaking:
			whichTab = 3;
			getValues(R.array.oi_tsdx_values, info_listeningspeaking,
					R.string.ot_info_listeningspeaking);
			break;
		case R.id.ot_languages_add:
			String info_languageStr = CommUtil.getTextValue(info_language);
			String info_literacyskillsStr = CommUtil
					.getTextValue(info_literacyskills);
			String info_listeningspeakingStr = CommUtil
					.getTextValue(info_listeningspeaking);

			ContentValues cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("language", info_languageStr);
			cValues.put("literacyskills", info_literacyskillsStr);
			cValues.put("listeningspeaking", info_listeningspeakingStr);
			cValues.put("createtime", TimeUtils.getCurrentTimeInString());

			queryResult = dbUtil
					.insertData(self, CommonText.OTHERINFO, cValues);
			if (queryResult) {
				ot_languages_edit.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.ot_languages_edit:

			break;
		case R.id.ot_certificate_add:
			String info_certificateStr = CommUtil.getTextValue(info_certificate);
			String info_certificatetimesStr = CommUtil.getTextValue(info_certificatetime);

			cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("certificate", info_certificateStr);
			cValues.put("certificatetime", info_certificatetimesStr);
			cValues.put("createtime", TimeUtils.getCurrentTimeInString());

			queryResult = dbUtil
					.insertData(self, CommonText.OTHERINFO1, cValues);
			if (queryResult) {
				ot_certificate_edit.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.ot_certificate_edit:

			break;
		case R.id.ot_otherinfo_add:
			String info_titleStr = CommUtil.getTextValue(info_title);
			String info_descriptionStr = CommUtil
					.getTextValue(info_description);
			cValues = new ContentValues();
			cValues.put("userId", "1");
			cValues.put("title", info_titleStr);
			cValues.put("description", info_descriptionStr);

			queryResult = dbUtil
					.insertData(self, CommonText.OTHERINFO2, cValues);
			if (queryResult) {
				ot_otherinfo_edit.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.ot_otherinfo_edit:

			break;
		case R.id.info_certificatetime:
			DialogUtils.showTimeChooseDialog(self, info_certificatetime,R.string.ot_info_getcertificatetime,13,mHandler);
			break;
		default:
			break;
		}
	}

	private void getValues(int array, View parent, int resId) {
		String[] item_text = CommUtil.getArrayValue(self, array);
		mList = Arrays.asList(item_text);
		DialogUtils.showPopWindow(self, parent, resId, mList, mHandler);
	}
}
