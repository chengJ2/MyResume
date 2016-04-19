package com.me.resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.comm.CommForMapArrayBaseAdapter;
import com.me.resume.comm.ViewHolder;
import com.me.resume.swipeback.SwipeBackActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.views.JazzyViewPager;
import com.me.resume.views.JazzyViewPager.TransitionEffect;
import com.me.resume.views.TagFlowLayout;
import com.whjz.android.text.CommonText;

/**
 * 
 * @ClassName: MainActivity
 * @Description: 简历模板预览界面
 * @author Comsys-WH1510032
 * @date 2016/3/29 下午2:13:45
 * 
 */
public class MainActivity extends SwipeBackActivity {

	private JazzyViewPager jazzyViewPager;

	private LayoutInflater mInflater;
	private View view1, view2, view3, view4, view5;// 页卡视图
	
	private List<View> mViewList = new ArrayList<>();// 页卡视图集合
	
	private static final int MSG_CHANGE_PHOTO = 1;
	/** view自动切换时间 */
	private static final int VIEW_CHANGE_TIME = 10000;
	
	private boolean showEffect = true;
	
	// View1
	private TextView index_1_realname, index_1_info,index_1_where,index_1_lisence,index_1_phone,index_1_email;
	
	// View2
	private ListView weListview;
	
	// View3
	private TextView self_evaluation;
	private TagFlowLayout tagFlowLayout;
	private String mTags[] = { "活泼好动", "易随波逐流", "多愁善感", "不善言谈",
			"务实", "适应能力差", "易怒而难以自制"};
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_CHANGE_PHOTO:
				int index = jazzyViewPager.getCurrentItem();
				if (index == mViewList.size() - 1) {
					index = -1;
				}
				jazzyViewPager.setCurrentItem(index + 1);
				mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,
						VIEW_CHANGE_TIME);
				break;
			case 2:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViews();
		initViews();
		showViews();
	}

	private void findViews() {
		jazzyViewPager = findView(R.id.index_product_container);
	}
	
	private Button goHomeBtn;

	private void initViews() {
		mInflater = LayoutInflater.from(this);
		
		view1 = mInflater.inflate(R.layout.index_resume_1, null);
		view2 = mInflater.inflate(R.layout.index_resume_2, null);
		view3 = mInflater.inflate(R.layout.index_resume_3, null);
		view4 = mInflater.inflate(R.layout.index_resume_4, null);
		view5 = mInflater.inflate(R.layout.index_resume_5, null);

		initView1(view1);
		
		initView2(view2);
		
		initView3(view3);
		
		initView4(view4);
		
		initView5(view5);

		mViewList.add(view4);
		mViewList.add(view5);

		goHomeBtn = (Button) view5.findViewById(R.id.go);
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 基本信息
	 * @author Comsys-WH1510032
	 * @return 返回类型  
	 * @param view
	 */
	private void initView1(View view){
		index_1_realname = ((TextView) view1.findViewById(R.id.index_1_realname));
		index_1_info = ((TextView) view1.findViewById(R.id.index_1_info));
		index_1_where = ((TextView) view1.findViewById(R.id.index_1_where));
		index_1_lisence = ((TextView) view1.findViewById(R.id.index_1_lisence));
		index_1_phone = ((TextView) view1.findViewById(R.id.index_1_phone));
		index_1_email = ((TextView) view1.findViewById(R.id.index_1_email));
		
		queryWhere = "select * from " + CommonText.BASEINFO
				+ " where userId = 1";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			mViewList.add(view1);
			
			index_1_realname.setText(commMapArray.get("realname")[0]);
			
			StringBuffer sbStr = new StringBuffer();
			String info = commMapArray.get("gender")[0];
			if(info.equals("0")){
				sbStr.append(CommUtil.getStrValue(self, R.string.info_sex_1));
			}else{
				sbStr.append(CommUtil.getStrValue(self, R.string.info_sex_2));
			}
			sbStr.append("|");
			
			info = commMapArray.get("ismarry")[0];
			if(info.equals("1")){
				sbStr.append(CommUtil.getStrValue(self, R.string.info_maritalstatus_2));
			}else if(info.equals("2")){
				sbStr.append(CommUtil.getStrValue(self, R.string.info_maritalstatus_3));
			}else{
				sbStr.append(CommUtil.getStrValue(self, R.string.info_maritalstatus_1));
			}
			sbStr.append(" | ");
			info = commMapArray.get("brithday")[0];
			if(RegexUtil.checkNotNull(info)){
				sbStr.append(info);
			}
			index_1_info.setText(sbStr);
			
			sbStr = new StringBuffer();
			info = commMapArray.get("hometown")[0];
			if(RegexUtil.checkNotNull(info)){
				sbStr.append("户口："+info);
				sbStr.append(" | ");
			}
			
			info = commMapArray.get("city")[0];
			if(RegexUtil.checkNotNull(info)){
				sbStr.append("现居地："+info);
			}
			
			index_1_where.setText(sbStr);
			
			index_1_lisence.setText("身份证："+commMapArray.get("license")[0]);
			index_1_phone.setText("手机号："+commMapArray.get("phone")[0]);
			index_1_email.setText("E-mail："+commMapArray.get("email")[0]);
		}
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 工作经历
	 * @author Comsys-WH1510032
	 * @return 返回类型  
	 * @param view
	 */
	private void initView2(View view){
		weListview = (ListView) view2.findViewById(R.id.weListview);

		queryWhere = "select * from " + CommonText.WORKEXPERIENCE + " where userId = 1 order by _id desc";
		final Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			mViewList.add(view2);
//			int we_show_nav = getPreferenceData("we_show_nav", 1);
			int LayoutID = R.layout.index_2_list_item;
//			if (we_show_nav == 1) {
//				LayoutID = R.layout.index_2_list_item;
//			} else if (we_show_nav == 2) {
//				LayoutID = R.layout.index_2_list2_item;
//			} else if (we_show_nav == 3) {
//
//			}
			CommForMapArrayBaseAdapter commMapAdapter = new CommForMapArrayBaseAdapter(
					self, commMapArray, LayoutID, "userId") {

				@Override
				public void convert(ViewHolder holder, String[] item,
						int position) {
					// TODO Auto-generated method stub
					holder.setText(R.id.item1, commMapArray.get("companyname")[position]);
					holder.setText(R.id.item2,
							commMapArray.get("worktimestart")[position] + " 至 "
									+ commMapArray.get("worktimeend")[position]);
				}
			};

			weListview.setAdapter(commMapAdapter);
		}
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 自我评价&职业目标
	 */
	private void initView3(View view){
		tagFlowLayout = (TagFlowLayout) view.findViewById(R.id.flowlayout);
		self_evaluation = (TextView)view.findViewById(R.id.self_evaluation);
		
		queryWhere = "select * from " + CommonText.EVALUATION + " where userId = 1 order by _id desc";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			mViewList.add(view3);
			self_evaluation.setText(commMapArray.get("selfevaluation")[0]);
		}
		
		MarginLayoutParams lp = new MarginLayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.leftMargin = 5;
		lp.rightMargin = 5;
		lp.topMargin = 5;
		lp.bottomMargin = 5;
		for (int i = 0; i < mTags.length; i++) {
			TextView tview = new TextView(this);
			tview.setText(mTags[i].toString().trim());
			tview.setTextSize(CommUtil.getFloatValue(self, R.dimen.microd_text_size));
			tview.setTextColor(CommUtil.getIntValue(self, R.color.red));
			tview.setTypeface(Typeface.SERIF);
			tview.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_tag_text_select));
			tagFlowLayout.addView(tview, lp);
		}
		
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 求职意向
	 */
	private void initView4(View view){
		
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 求职意向
	 */
	private void initView5(View view){
		
	}

	private void showViews() {
		if (showEffect) {
			jazzyViewPager.setTransitionEffect(TransitionEffect.Tablet);
		}

		jazzyViewPager.setCurrentItem(0);

		if (getPreferenceData("autoShow", 0) == 1) {
			mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, VIEW_CHANGE_TIME);
		}

		jazzyViewPager.setAdapter(new MyPagerAdapter(mViewList));

		

		goHomeBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setPreferenceData("firstInstall", 0);
				ActivityUtils.startActivity(self, MyApplication.PACKAGENAME
						+ ".ui.HomeActivity");
			}
		});

	}

	// ViewPager适配器
	class MyPagerAdapter extends PagerAdapter {
		private List<View> mViewList;

		public MyPagerAdapter(List<View> mViewList) {
			this.mViewList = mViewList;
		}

		@Override
		public int getCount() {
			return mViewList.size();// 页卡数
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;// 官方推荐写法
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mViewList.get(position));// 添加页卡
			return mViewList.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mViewList.get(position));// 删除页卡
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// return mTitleList.get(position);//页卡标题
			return String.valueOf(position);
		}

	}

}
