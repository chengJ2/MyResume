package com.me.resume.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.me.resume.MyApplication;
import com.me.resume.R;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.ui.fragment.AllFragmentFactory;
import com.me.resume.ui.fragment.EducationFragment;
import com.me.resume.ui.fragment.TrainingFragment;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.views.CustomFAB;
import com.me.resume.views.SegmentButton;

/**
 * 
* @ClassName: EducationActivity 
* @Description: 教育培训经历
* @author Comsys-WH1510032 
* @date 2016/4/6 下午1:32:23 
*
 */
public class EducationActivity extends SwipeBackActivity implements OnClickListener{

	private TextView toptext,leftLable,rightLable;
	
	private SegmentButton segment_button;
	
	private CustomFAB saveGo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_education_layout);
		
		toptext = findView(R.id.top_text);
		leftLable = findView(R.id.left_lable);
		rightLable = findView(R.id.right_lable);
		leftLable.setOnClickListener(this);
		rightLable.setOnClickListener(this);
		toptext.setText(CommUtil.getStrValue(EducationActivity.this, R.string.resume_education));
		rightLable.setText(CommUtil.getStrValue(EducationActivity.this, R.string.review_resume));
		
		segment_button = findView(R.id.segment_button);
		
		saveGo = findView(R.id.saveGo);
		saveGo.setOnClickListener(this);
		
		initData();
	}
	
	private void initData() {
		String tab = segment_button.getSegmentButton(0).getText().toString();
		switchContent(MyApplication.cposition,tab+"_zj");
		segment_button.setOnCheckedChangeListener(new SegmentButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(int position, Button button) {
				MyApplication.cposition = position;
				switchContent(MyApplication.cposition,button.getText().toString()+"_zj");
			}
		});
	}
	
	private void switchContent(int position,String tab){
		FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
		Fragment fragment=AllFragmentFactory.getFragment(tab);
//		L.dedug("switchContent--->"+fragment +"--position:"+position+ "--mPosition->"+GlobalApplication.cposition);
		if(fragment==null){
			switch (position) {
			case 0:
				fragment=new EducationFragment();
				break;
			case 1:
				fragment=new TrainingFragment();
				break;
			}
			AllFragmentFactory.putFragment(fragment, tab);
		}
		if(fragment.isAdded()){
			fragment.onResume();
		}else{
			transaction.replace(R.id.layout_content, fragment);
			transaction.commit();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.saveGo:
			ActivityUtils.startActivity(EducationActivity.this, MyApplication.PACKAGENAME
					+ ".ui.JobIntensionActivity");
			break;
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_lable:
			ActivityUtils.startActivity(EducationActivity.this, MyApplication.PACKAGENAME 
					+ ".MainActivity",false);
			break;
		default:
			break;
		}
	}
}
