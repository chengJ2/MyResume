package com.me.resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.comm.CommForMapArrayBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.ViewHolder;
import com.me.resume.ui.BaseActivity;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.views.CustomListView;
import com.me.resume.views.JazzyViewPager;
import com.me.resume.views.JazzyViewPager.TransitionEffect;
import com.me.resume.views.TagFlowLayout;
import com.whjz.android.text.CommonText;
import com.whjz.android.util.common.DbUtilImplement;
import com.whjz.android.util.interfa.DbLocalUtil;

/**
 * 
 * @ClassName: MainActivity
 * @Description: 简历模板预览界面
 * @author Comsys-WH1510032
 * @date 2016/3/29 下午2:13:45
 * 
 */
public class MainActivity extends Activity {

	private MainActivity self;
	
	private JazzyViewPager jazzyViewPager;

	private LayoutInflater mInflater;
	private View cover,view1, view2, view3, view4, view5,view6,view7,view8;// 页卡视图
	
	private List<View> mViewList = new ArrayList<>();// 页卡视图集合
	
	private static final int MSG_CHANGE_PHOTO = 1;
	/** view自动切换时间 */
	private static final int VIEW_CHANGE_TIME = 10000;
	
	private boolean showEffect = true;
	
	private CommForMapArrayBaseAdapter commMapAdapter = null;
	
	protected DbLocalUtil dbUtil = new DbUtilImplement();;// 本地数据库对象
	
	private String queryWhere = "";
	
	private SharedPreferences sp;
	
	private TextView main_top_title;
	private ImageView main_top_edit;
	
	// cover
	private LinearLayout coverlayout;
	
	// View1
	private LinearLayout index1layout;
	private TextView index_1_realname, index_1_info,index_1_where,index_1_lisence,index_1_phone,index_1_email;
	
	// View2
	private LinearLayout index2layout;
	private ListView weListview;
	
	// View3
	private LinearLayout index3layout;
	private TextView self_evaluation;
	private TagFlowLayout tagFlowLayout;
	private String mTags[] = { "活泼好动", "易随波逐流", "多愁善感", "不善言谈",
			"务实", "适应能力差", "易怒而难以自制"};
	
	// View4
	private LinearLayout index4layout;
	private TextView index_4_info1,index_4_info2,index_4_info3,index_4_info4,index_4_info5,index_4_info6;
	
	// View5
	private LinearLayout index5layout;
	
	
	// View6
	private LinearLayout index6layout;
	private CustomListView edListview,trListview;
	
	private LinearLayout index6_trLayout;
	
	// View7
	private LinearLayout index7layout;
	private LinearLayout index7_layout1,index7_layout2,index7_layout3;
	private CustomListView listview1,listview2,listview3;
	
	// View8
	private ImageView goHome;
	
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
		sp = getSharedPreferences(Constants.CONFIG, Context.MODE_PRIVATE);
		jazzyViewPager = (JazzyViewPager)findViewById(R.id.index_product_container);
	}
	

	private void initViews() {
		self = MainActivity.this;
		
		mInflater = LayoutInflater.from(this);
		
		cover = mInflater.inflate(R.layout.index_resume_cover, null);
		view1 = mInflater.inflate(R.layout.index_resume_1, null);
		view2 = mInflater.inflate(R.layout.index_resume_2, null);
		view3 = mInflater.inflate(R.layout.index_resume_3, null);
		view4 = mInflater.inflate(R.layout.index_resume_4, null);
		view5 = mInflater.inflate(R.layout.index_resume_5, null);
		view6 = mInflater.inflate(R.layout.index_resume_6, null);
		view7 = mInflater.inflate(R.layout.index_resume_7, null);
		view8 = mInflater.inflate(R.layout.index_resume_8, null);
		
		// 添加封面
		coverlayout = (LinearLayout)cover.findViewById(R.id.coverlayout);
		mViewList.add(cover);
		
		initView1(view1);
		
		initView2(view2);
		
		initView3(view3);
		
		initView4(view4);
		
		initView5(view5);
		
		initView6(view6);
		
		initView7(view7);
		
		initView8(view8);
		
	}
	
	private void showViews() {
		if (showEffect) {
			jazzyViewPager.setTransitionEffect(TransitionEffect.Tablet);
		}

		jazzyViewPager.setCurrentItem(0);

		if (sp.getInt("autoShow", 0) == 1) {
			mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO, VIEW_CHANGE_TIME);
		}

		jazzyViewPager.setAdapter(new MyPagerAdapter(mViewList));
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 基本信息
	 * @param view
	 */
	private void initView1(View view){
		index1layout = (LinearLayout) view.findViewById(R.id.index1layout);
		index_1_realname = ((TextView) view.findViewById(R.id.index_1_realname));
		index_1_info = ((TextView) view.findViewById(R.id.index_1_info));
		index_1_where = ((TextView) view.findViewById(R.id.index_1_where));
		index_1_lisence = ((TextView) view.findViewById(R.id.index_1_lisence));
		index_1_phone = ((TextView) view.findViewById(R.id.index_1_phone));
		index_1_email = ((TextView) view.findViewById(R.id.index_1_email));
		
		initTopView(view,R.string.resume_baseinfo,"BaseInfoActivity");
		
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = "+ BaseActivity.kId +" limit 1";
		Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			
			index1layout.setBackgroundColor(CommUtil.getColorValue(self,
					CommUtil.parseInt(commMapArray.get("bgcolor")[0])));
			
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
			
			info = commMapArray.get("license")[0];
			if(RegexUtil.checkNotNull(info)){
				index_1_lisence.setText("身份证："+commMapArray.get("license")[0]);
			}
			index_1_phone.setText("手机号："+commMapArray.get("phone")[0]);
			index_1_email.setText("E-mail："+commMapArray.get("email")[0]);
		}
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 工作经历
	 * @param view
	 */
	private void initView2(View view){
		index2layout = (LinearLayout) view.findViewById(R.id.index2layout);
		weListview = (ListView) view.findViewById(R.id.weListview);
		initTopView(view,R.string.resume_workexperience,"WorkExperienceActivity");
		queryWhere = "select * from " + CommonText.WORKEXPERIENCE + " where userId = "+ BaseActivity.kId +" order by id desc";
		final Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			
			index2layout.setBackgroundColor(CommUtil.getColorValue(self,
					CommUtil.parseInt(commMapArray.get("bgcolor")[0])));
			
//			int we_show_nav = getPreferenceData("we_show_nav", 1);
			int LayoutID = R.layout.index_2_list_item;
//			if (we_show_nav == 1) {
//				LayoutID = R.layout.index_2_list_item;
//			} else if (we_show_nav == 2) {
//				LayoutID = R.layout.index_2_list2_item;
//			} else if (we_show_nav == 3) {
//
//			}
			
			commMapAdapter = new CommForMapArrayBaseAdapter(
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
		index3layout = (LinearLayout) view.findViewById(R.id.index3layout);
		tagFlowLayout = (TagFlowLayout) view.findViewById(R.id.flowlayout);
		self_evaluation = (TextView)view.findViewById(R.id.self_evaluation);
		initTopView(view,R.string.resume_evaluation,"EvaluationActivity");
		queryWhere = "select * from " + CommonText.EVALUATION + " where userId = "+ BaseActivity.kId +" order by id desc";
		Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			index3layout.setBackgroundColor(CommUtil.getColorValue(self,
					CommUtil.parseInt(commMapArray.get("bgcolor")[0])));
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
			tview.setTextSize(CommUtil.getFloatValue(self, R.dimen.tiny_text_size));
			tview.setTextColor(CommUtil.getIntValue(self, R.color.red));
			tview.setTypeface(Typeface.SERIF);
			tview.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_tag_text_select));
			tview.setPadding(6, 8, 6, 8);
			tagFlowLayout.addView(tview, lp);
		}
		
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 求职意向
	 */
	private void initView4(View view){
		index4layout = (LinearLayout) view.findViewById(R.id.index4layout);
		index_4_info1 = (TextView)view.findViewById(R.id.index_4_info1);
		index_4_info2 = (TextView)view.findViewById(R.id.index_4_info2);
		index_4_info3 = (TextView)view.findViewById(R.id.index_4_info3);
		index_4_info4 = (TextView)view.findViewById(R.id.index_4_info4);
		index_4_info5 = (TextView)view.findViewById(R.id.index_4_info5);
		index_4_info6 = (TextView)view.findViewById(R.id.index_4_info6);
		initTopView(view,R.string.resume_jobintension,"JobIntensionActivity");
		queryWhere = "select * from " + CommonText.JOBINTENSION + " where userId = "+ BaseActivity.kId +" order by id desc";
		Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			index4layout.setBackgroundColor(CommUtil.getColorValue(self,
					CommUtil.parseInt(commMapArray.get("bgcolor")[0])));
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			index_4_info1.setText(Html.fromHtml("<strong>工作性质：</strong>"+commMapArray.get("expworkingproperty")[0]));
			index_4_info2.setText(Html.fromHtml("<strong>期望职业：</strong>"+commMapArray.get("expworkcareer")[0]));
			index_4_info3.setText(Html.fromHtml("<strong>期望行业：</strong>"+commMapArray.get("expworkindustry")[0]));
			index_4_info4.setText(Html.fromHtml("<strong>工作地区：</strong>"+commMapArray.get("expdworkplace")[0]));
			index_4_info5.setText(Html.fromHtml("<strong>期望月薪：</strong>"+commMapArray.get("expmonthlysalary")[0]));
			index_4_info6.setText(Html.fromHtml("<strong>目前状况：</strong>"+commMapArray.get("workingstate")[0]));
		}
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 项目经验
	 */
	private void initView5(View view){
		index5layout = (LinearLayout) view1.findViewById(R.id.index5layout);
		
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 教育&培训
	 */
	private void initView6(View view){
		index6layout = (LinearLayout) view.findViewById(R.id.index6layout);
		edListview = (CustomListView)view.findViewById(R.id.edListview);
		trListview = (CustomListView)view.findViewById(R.id.trListview);
		index6_trLayout = (LinearLayout)view.findViewById(R.id.index6_trLayout);
		initTopView(view,R.string.resume_education,"EducationActivity");
		queryWhere = "select * from " + CommonText.EDUCATION + " where userId = "+ BaseActivity.kId +" order by id desc";
		final Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			index6layout.setBackgroundColor(CommUtil.getColorValue(self,
					CommUtil.parseInt(commMapArray.get("bgcolor")[0])));
			
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			
			commMapAdapter = new CommForMapArrayBaseAdapter(
					self, commMapArray, R.layout.index_6_list_item, "userId") {

				@Override
				public void convert(ViewHolder holder, String[] item,
						int position) {
					holder.setText(R.id.item1,
							commMapArray.get("educationtimestart")[position] + " — "
									+ commMapArray.get("educationtimeend")[position]);
					StringBuffer sbStr = new StringBuffer();
					String info = commMapArray.get("school")[position];
					sbStr.append(info);
					sbStr.append(" | ");
					
					info = commMapArray.get("majorname")[position];
					sbStr.append(info);
					sbStr.append(" | ");
					
					info = commMapArray.get("degree")[position];
					sbStr.append(info);
					
					holder.setText(R.id.item2, sbStr.toString());
				}
			};

			edListview.setAdapter(commMapAdapter);
			
			queryWhere = "select * from " + CommonText.EDUCATION_TRAIN + " where userId = "+ BaseActivity.kId +" order by id desc";
			final Map<String, String[]> comm2MapArray = dbUtil.queryData(self, queryWhere);
			if (comm2MapArray != null && comm2MapArray.get("userId").length > 0) {
				index6_trLayout.setVisibility(View.VISIBLE);
				
				if (!mViewList.contains(view6)) {
					mViewList.add(view6);
				}
				
				commMapAdapter = new CommForMapArrayBaseAdapter(
						self, comm2MapArray, R.layout.index_62_list_item, "userId") {
					
					@Override
					public void convert(ViewHolder holder, String[] item,
							int position) {
						holder.setText(R.id.item1,
								comm2MapArray.get("trainingtimestart")[position] + " — "
										+ comm2MapArray.get("trainingtimeend")[position]);
						
						String info = comm2MapArray.get("trainingorganization")[position];
						holder.setText(R.id.item2, info.toString());
						
						StringBuffer sbStr = new StringBuffer();
						info = comm2MapArray.get("trainingclass")[position];
						sbStr.append("<strong>培训课程：</strong>"+ info + "<br/>");
						info = comm2MapArray.get("certificate")[position];
						if(RegexUtil.checkNotNull(info)){
							sbStr.append("<strong>所获证书：</strong>"+ info + "<br/>");
						}
						info = comm2MapArray.get("description")[position];
						if(RegexUtil.checkNotNull(info)){
							sbStr.append("<strong>培训描述：</strong>"+ info );
						}
						
						holder.setTextForHtml(R.id.item3, sbStr.toString());
						
					}
				};
				trListview.setAdapter(commMapAdapter);
			}else{
				index6_trLayout.setVisibility(View.GONE);
			}
		}
		
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 其他信息
	 */
	private void initView7(View view){
		index7layout = (LinearLayout) view.findViewById(R.id.index7layout);
		
		index7_layout1= (LinearLayout) view.findViewById(R.id.index7_layout1);
		index7_layout2= (LinearLayout) view.findViewById(R.id.index7_layout2);
		index7_layout3= (LinearLayout) view.findViewById(R.id.index7_layout3);
		listview1 = (CustomListView) view.findViewById(R.id.listview1);
		listview2= (CustomListView) view.findViewById(R.id.listview2);
		listview3= (CustomListView) view.findViewById(R.id.listview3);
		
		initTopView(view,R.string.resume_otherinfo,"OtherInfoActivity");
		
		queryWhere = "select * from " + CommonText.OTHERINFO + " where userId = "+ BaseActivity.kId +" order by id desc";
		final Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
        if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	index7layout.setBackgroundColor(CommUtil.getColorValue(self,
					CommUtil.parseInt(commMapArray.get("bgcolor")[0])));
        	index7_layout1.setVisibility(View.VISIBLE);
        	if (!mViewList.contains(view)) {
        		mViewList.add(view);
			}
        	
        	commMapAdapter = new CommForMapArrayBaseAdapter(
					self, commMapArray, R.layout.index7_listview_item1_text, "userId") {

				@Override
				public void convert(ViewHolder holder, String[] item,
						int position) {
					StringBuffer sbStr = new StringBuffer();
					String info = commMapArray.get("language")[position];
					sbStr.append(info);
					sbStr.append(" :");
					
					info = commMapArray.get("literacyskills")[position];
					sbStr.append(CommUtil.getStrValue(self, R.string.ot_info_literacyskills)+info);
					sbStr.append(" | ");
					
					info = commMapArray.get("listeningspeaking")[position];
					sbStr.append(CommUtil.getStrValue(self, R.string.ot_info_listeningspeaking)+info);
					
					holder.setText(R.id.item1, sbStr.toString());
				}
			};

			listview1.setAdapter(commMapAdapter);
        	
        }else{
        	index7_layout1.setVisibility(View.GONE);
        }
        
        queryWhere = "select * from " + CommonText.OTHERINFO1 + " where userId = 1 order by id desc";
        final Map<String, String[]> commMapArray2 = dbUtil.queryData(self, queryWhere);
        if (commMapArray2!= null && commMapArray2.get("userId").length > 0) {
        	index7_layout2.setVisibility(View.VISIBLE);
        	if (!mViewList.contains(view)) {
        		mViewList.add(view);
			}
        	commMapAdapter = new CommForMapArrayBaseAdapter(
					self, commMapArray2, R.layout.index7_listview_item1_text, "userId") {

				@Override
				public void convert(ViewHolder holder, String[] item,
						int position) {
					StringBuffer sbStr = new StringBuffer();
					String info = commMapArray2.get("certificate")[position];
					sbStr.append(info);
					sbStr.append(" | ");
					info = commMapArray2.get("certificatetime")[position];
					sbStr.append(info);
					holder.setText(R.id.item1, sbStr.toString());
				}
			};

			listview2.setAdapter(commMapAdapter);
        	
        }else{
        	index7_layout2.setVisibility(View.GONE);
        }
        
        queryWhere = "select * from " + CommonText.OTHERINFO2 + " where userId = 1 order by id desc";
        final Map<String, String[]> commMapArray3 = dbUtil.queryData(self, queryWhere);
        if (commMapArray3!= null && commMapArray3.get("userId").length > 0) {
        	index7_layout3.setVisibility(View.VISIBLE);
        	if (!mViewList.contains(view)) {
        		mViewList.add(view);
			}
        	
        	commMapAdapter = new CommForMapArrayBaseAdapter(
					self, commMapArray3, R.layout.index7_listview_item3_text, "userId") {

				@Override
				public void convert(ViewHolder holder, String[] item,
						int position) {
					String info = commMapArray3.get("title")[position];
					holder.setText(R.id.item1, info);
					
					info = commMapArray3.get("description")[position];
					holder.setText(R.id.item2, info);
				}
			};

			listview3.setAdapter(commMapAdapter);
        	
        }else{
        	index7_layout3.setVisibility(View.GONE);
        }
		
	}

	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 备注信息
	 * @param view
	 */
	private void initView8(View view){
		mViewList.add(view8);
		
		goHome = (ImageView) view8.findViewById(R.id.gohome);
		
		goHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				sp.edit().putInt("startVerytime", 0).commit();
				ActivityUtils.startActivity(self, Constants.PACKAGENAME
						+ ".ui.HomeActivity");
			}
		});
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 初始化topUI
	 * @param view
	 * @param redId
	 * @param src
	 */
	private void initTopView(View view,int redId,final String src){
		main_top_title =  (TextView) view.findViewById(R.id.main_top_title);
		main_top_title.setText(CommUtil.getStrValue(self, redId));
		main_top_edit = (ImageView) view.findViewById(R.id.main_top_edit);
		
		if (sp.getBoolean("edit_mode", false) == true) {
			main_top_edit.setVisibility(View.VISIBLE);
		}else{
			main_top_edit.setVisibility(View.GONE);
		}
		
		main_top_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				ActivityUtils.startActivity(self, Constants.PACKAGENAME + ".ui." + src);
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
