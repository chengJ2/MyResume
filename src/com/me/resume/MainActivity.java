package com.me.resume;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.me.resume.comm.CommForMapArrayBaseAdapter;
import com.me.resume.comm.Constants;
import com.me.resume.comm.UserInfoCode;
import com.me.resume.comm.ViewHolder;
import com.me.resume.utils.ActivityUtils;
import com.me.resume.utils.CommUtil;
import com.me.resume.utils.FileUtils;
import com.me.resume.utils.ImageUtils;
import com.me.resume.utils.PreferenceUtil;
import com.me.resume.utils.RegexUtil;
import com.me.resume.utils.TimeUtils;
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
 * @date 2016/3/29 下午2:13:45
 * 
 */
public class MainActivity extends Activity {
	private MainActivity self;
	
	private MyPagerAdapter pagerAdapter = null;
	private JazzyViewPager jazzyViewPager;

	private LayoutInflater mInflater;
	private View cover,view1, view2, view3, view4, view5,view6,view7,view8;// 页卡视图
	
	private List<View> mViewList = new ArrayList<>();// 页卡视图集合
	
	private static final int MSG_CHANGE_PHOTO = 1;
	
	private CommForMapArrayBaseAdapter commMapAdapter = null;
	
	protected DbLocalUtil dbUtil = new DbUtilImplement();;// 本地数据库对象
	
	private String queryWhere = "";
	
	protected PreferenceUtil preferenceUtil;
	
	private TextView main_top_title;
	private ImageView main_top_edit;
	
	// cover
	private ImageView coverlayout;
	
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
	
	// View4
	private LinearLayout index4layout;
	private TextView index_4_info1,index_4_info2,index_4_info3,index_4_info4,index_4_info5,index_4_info6;
	
	// View5
	private LinearLayout index5layout;
	private ListView peListview;
	
	// View6
	private LinearLayout index6layout;
	private CustomListView edListview,trListview;
	
	private LinearLayout index6_trLayout;
	
	// View7
	private LinearLayout index7layout;
	private LinearLayout index7_layout1,index7_layout2,index7_layout3;
	private CustomListView listview1,listview2,listview3;
	
	private boolean goFlag = true;
	
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MSG_CHANGE_PHOTO:
				int index = jazzyViewPager.getCurrentItem();
				if (index == mViewList.size() - 1) {
					index = -1;
				}
				jazzyViewPager.setCurrentItem(index + 1);
				mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,Constants.DEFAULTIME);
				break;
			case 2:
				break;
			default:
				break;
			}
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		self = MainActivity.this;
		if(preferenceUtil == null)
			preferenceUtil = new PreferenceUtil(self);
		jazzyViewPager = (JazzyViewPager)findViewById(R.id.mainviewpager);
		jazzyViewPager.setFadeEnabled(true);//有淡入淡出效果
		jazzyViewPager.setCurrentItem(0);
		initViews();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (goFlag) {
			goFlag = false;
//			new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					runOnUiThread(new  Runnable() {
//						public void run() {
							initCover(cover);
							
							initView1(view1);
							
							initView2(view2);
							
							initView3(view3);
							
							initView4(view4);
							
							initView5(view5);
							
							initView6(view6);
							
							initView7(view7);
							
							initView8(view8);
							
							showViews();
//						}
//					});
//				}
//			}).start();
		}
	}

	
	/**
	 * 初始化UI
	 */
	private void initViews() {
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
		
		if (preferenceUtil.getPreferenceData(Constants.SET_AUTOSHOW)) {
			int switchDuration = preferenceUtil.getPreferenceData(Constants.SET_SWITCHEFFDURATION,Constants.DEFAULTIME);
			TransitionEffect effect = TransitionEffect.valueOf(preferenceUtil.getPreferenceData(Constants.SET_SWITCHANIM, "Standard"));
			jazzyViewPager.setTransitionEffect(effect);
			mHandler.sendEmptyMessageDelayed(MSG_CHANGE_PHOTO,switchDuration);
		}
	}
	
	
	/**
	 * 
	 * 显示预览界面
	 */
	private void showViews() {
		try {
			if (pagerAdapter == null) {
				pagerAdapter = new MyPagerAdapter(mViewList);
			}else{
				pagerAdapter.notifyDataSetChanged();
			}
			jazzyViewPager.setAdapter(pagerAdapter);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 显示封面
	 * @param view
	 */
	private void initCover(View view){
		coverlayout = (ImageView)view.findViewById(R.id.coverlayout);
		mViewList.add(cover);
		
		String coverName = preferenceUtil.getPreferenceData(Constants.COVER,"");
		if (RegexUtil.checkNotNull(coverName)) {
			if(preferenceUtil.getPreferenceData(Constants.ISLOCAL)){
				coverlayout.setImageResource(CommUtil.parseInt(coverName));
			}else{
				Bitmap bitmap = ImageUtils.getLoacalBitmap(FileUtils.COVER_DOWNLOAD_APKPATH + coverName);
				if (bitmap != null) {
					coverlayout.setImageBitmap(bitmap);
				}
			}
		}
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
		
		initTopView(view,R.string.resume_baseinfo,Constants.BASEINFO);
		
		queryWhere = "select * from " + CommonText.BASEINFO + " where userId = '"+ BaseActivity.uTokenId +"' limit 1";
		Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			
			initBgColor(index1layout,commMapArray.get("bgcolor")[0]);
			
			index_1_realname.setText(preferenceUtil.getPreferenceData(UserInfoCode.REALNAME, ""));
			
			StringBuffer sbStr = new StringBuffer();
			String info = commMapArray.get("gender")[0];
			if(info.equals("0")){
				sbStr.append(CommUtil.getStrValue(self, R.string.info_sex_1));
			}else{
				sbStr.append(CommUtil.getStrValue(self, R.string.info_sex_2));
			}
			sbStr.append("&nbsp;|&nbsp;");
			
			info = commMapArray.get("ismarry")[0];
			if(info.equals("1")){
				sbStr.append(CommUtil.getStrValue(self, R.string.info_maritalstatus_2));
			}else if(info.equals("2")){
				sbStr.append(CommUtil.getStrValue(self, R.string.info_maritalstatus_3));
			}else{
				sbStr.append(CommUtil.getStrValue(self, R.string.info_maritalstatus_1));
			}
			sbStr.append("&nbsp;|&nbsp;");
			info = commMapArray.get("brithday")[0];
			int theYear = CommUtil.parseInt(TimeUtils.theYear());
			if(RegexUtil.checkNotNull(info)){
				int age = theYear - CommUtil.parseInt(info.substring(0, 4));
				sbStr.append(age+"岁");
			}
			index_1_info.setText(Html.fromHtml(sbStr.toString()));
			
			sbStr = new StringBuffer();
			info = commMapArray.get("hometown")[0];
			if(RegexUtil.checkNotNull(info)){
				sbStr.append("家乡："+info);
				sbStr.append("&nbsp;|&nbsp;");
			}
			
			info = commMapArray.get("city")[0];
			if(RegexUtil.checkNotNull(info)){
				sbStr.append("现居地："+info);
			}
			
			index_1_where.setText(Html.fromHtml(sbStr.toString()));
			
			info = commMapArray.get("license")[0];
			if(RegexUtil.checkNotNull(info)){
				index_1_lisence.setVisibility(View.VISIBLE);
				index_1_lisence.setText("身份证："+ commMapArray.get("license")[0]);
			}else{
				index_1_lisence.setVisibility(View.GONE);
			}
			
			info = commMapArray.get("phone")[0];
			if(RegexUtil.checkNotNull(info)){
				index_1_phone.setVisibility(View.VISIBLE);
				index_1_phone.setText("手机号："+info);
			}else{
				index_1_phone.setVisibility(View.GONE);
			}
			info = commMapArray.get("email")[0];
			if(RegexUtil.checkNotNull(info)){
				index_1_email.setVisibility(View.VISIBLE);
				index_1_email.setText("E-mail："+info);
			}else{
				index_1_email.setVisibility(View.GONE);
			}
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
		initTopView(view,R.string.resume_workexperience,Constants.WORKEXPERIENCE);
		queryWhere = "select * from " + CommonText.WORKEXPERIENCE + " where userId = '"+ BaseActivity.uTokenId +"' order by id desc";
		final Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			initBgColor(index2layout,commMapArray.get("bgcolor")[0]);
			commMapAdapter = new CommForMapArrayBaseAdapter(
					self, commMapArray, R.layout.index_2_list_item, "userId") {

				@Override
				public void convert(ViewHolder holder, String[] item,
						int position) {
					holder.setText(R.id.item2, commMapArray.get("companyname")[position]);
					holder.setText(R.id.starttime, TimeUtils.toStrDate(commMapArray.get("worktimestart")[position]));
					holder.setText(R.id.duetime, TimeUtils.toStrDate(commMapArray.get("worktimeend")[position]));
				}
			};

			weListview.setAdapter(commMapAdapter);
			weListview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					ActivityUtils.startActivityPro(self, 
							Constants.PACKAGENAMECHILD + Constants.INFOMANAGER, 
							Constants.TYPE,CommonText.WORKEXPERIENCE);
				}
			});
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
		initTopView(view,R.string.resume_evaluation,Constants.EVALUATION);
		queryWhere = "select * from " + CommonText.EVALUATION + " where userId = '"+ BaseActivity.uTokenId +"' order by id desc";
		Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			initBgColor(index3layout,commMapArray.get("bgcolor")[0]);
			self_evaluation.setText(commMapArray.get("selfevaluation")[0]);
			
			String tag = commMapArray.get("character")[0];
			if (RegexUtil.checkNotNull(tag)) {
				fillTagFlowView(tag);
			}
		}
		
		queryWhere = "select * from " + CommonText.CHARACTER + " where userId = '"+ BaseActivity.uTokenId+"'";
		commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null) {
			
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			
			int count = commMapArray.get("userId").length;
			StringBuffer sb = new StringBuffer();
			for (int i = 0; i < count; i++) {
				sb.append(commMapArray.get("character")[i]).append(";");
			}
			
			String tag = CommUtil.getStringLable(sb.toString());
			fillTagFlowView(tag);
		}
	}
	
	/**
	 * 显示性格标签
	 * @param tag
	 */
	private void fillTagFlowView(String tag){
		String[] tags = tag.split(";");
		List<String> ll = Arrays.asList(tags);
		if (ll != null && ll.size() > 0) {
			MarginLayoutParams lp = new MarginLayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.leftMargin = 5;
			lp.rightMargin = 5;
			lp.topMargin = 5;
			lp.bottomMargin = 5;
			for (int i = 0,cun = ll.size(); i < cun; i++) {
				TextView tview = new TextView(this);
				tview.setText(ll.get(i).toString().trim());
				tview.setTextSize(CommUtil.getFloatValue(self, R.dimen.main_tiny_text));
				tview.setTextColor(CommUtil.getColorValue(self, getRanColor().get(new Random().nextInt(10))));
				tview.setTypeface(Typeface.SERIF);
//				tview.setBackgroundDrawable(getResources().getDrawable(R.drawable.home_tag_text_corner));
				tview.setBackgroundResource(R.drawable.home_tag_text_corner);
				tagFlowLayout.addView(tview, lp);
			}
		}
	}
	
	/**
	 * 显示性格标签颜色
	 */
	private List<Integer> getRanColor(){
		final TypedArray typedArray = getResources().obtainTypedArray(R.array.review_bgcolor);
		List<Integer> nList = new ArrayList<Integer>();
		for (int i = 0; i < typedArray.length(); i++) {
			nList.add(typedArray.getResourceId(i, 0));
		}
		return nList;
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
		initTopView(view,R.string.resume_jobintension,Constants.JOBINTENSION);
		queryWhere = "select * from " + CommonText.JOBINTENSION + " where userId = '"+ BaseActivity.uTokenId +"' order by id desc";
		Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			initBgColor(index4layout,commMapArray.get("bgcolor")[0]);
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			index_4_info1.setText("工作性质："+ commMapArray.get("expworkingproperty")[0]);
			index_4_info2.setText("期望职业："+commMapArray.get("expworkcareer")[0]);
			index_4_info3.setText("期望行业："+commMapArray.get("expworkindustry")[0]);
			index_4_info4.setText("工作地点："+commMapArray.get("expdworkplace")[0]);
			index_4_info5.setText("期望月薪："+commMapArray.get("expmonthlysalary")[0]);
			index_4_info6.setText(commMapArray.get("workingstate")[0]);
		}
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 项目经验
	 */
	private void initView5(View view){
		index5layout = (LinearLayout) view1.findViewById(R.id.index5layout);
		peListview = (ListView) view.findViewById(R.id.peListview);
		initTopView(view,R.string.resume_project_experience,Constants.PROJECTEXPERIENCE);
		queryWhere = "select * from " + CommonText.PROJECT_EXPERIENCE + " where userId = '"+ BaseActivity.uTokenId +"' order by id desc";
		final Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			initBgColor(index5layout,commMapArray.get("bgcolor")[0]);
			if (!mViewList.contains(view)) {
				mViewList.add(view);
			}
			commMapAdapter = new CommForMapArrayBaseAdapter(self, commMapArray,
					R.layout.index_5_list_item, "userId") {

				@Override
				public void convert(ViewHolder holder, String[] item,
						int position) {
					holder.setText(R.id.item1,"● " +commMapArray.get("worktimestart")[position] + " — " + commMapArray.get("worktimeend")[position]);
					String info_dutiesStr = commMapArray.get("duties")[position];
					holder.setText(R.id.item11, info_dutiesStr);
					String info_prokectdescStr = commMapArray.get("prokectdesc")[position];
					holder.setText(R.id.item12,info_prokectdescStr);
				}
			};
			
			peListview.setAdapter(commMapAdapter);
			peListview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					ActivityUtils.startActivityPro(self, 
							Constants.PACKAGENAMECHILD + Constants.INFOMANAGER, 
							Constants.TYPE,CommonText.PROJECT_EXPERIENCE);
				}
			});
		}
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
		initTopView(view,R.string.resume_education,Constants.EDUCATION);
		queryWhere = "select * from " + CommonText.EDUCATION + " where userId = '"+ BaseActivity.uTokenId +"' order by id desc";
		final Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
		if (commMapArray != null && commMapArray.get("userId").length > 0) {
			initBgColor(index6layout,commMapArray.get("bgcolor")[0]);
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
					sbStr.append("&nbsp;|&nbsp;");
					
					info = commMapArray.get("majorname")[position];
					sbStr.append(info);
					sbStr.append("&nbsp;|&nbsp;");
					
					info = commMapArray.get("degree")[position];
					sbStr.append(info);
					
					holder.setTextForHtml(R.id.item2, sbStr.toString());
				}
			};

			edListview.setAdapter(commMapAdapter);
			
			edListview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Bundle b = new Bundle();
					b.putInt(Constants.TAB, 0);
					b.putString(Constants.TYPE, CommonText.EDUCATION);
					ActivityUtils.startActivity(self, 
							Constants.PACKAGENAMECHILD + Constants.INFOMANAGER, 
							b,false);
				}
			});
			
			queryWhere = "select * from " + CommonText.EDUCATION_TRAIN + " where userId = '"+ BaseActivity.uTokenId +"' order by id desc";
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
			
			trListview.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					Bundle b = new Bundle();
					b.putInt(Constants.TAB, 1);
					b.putString(Constants.TYPE, CommonText.EDUCATION);
					ActivityUtils.startActivity(self, 
							Constants.PACKAGENAMECHILD + Constants.INFOMANAGER, 
							b,false);
				}
			});
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
		
		initTopView(view,R.string.resume_otherinfo,Constants.OTHERINFO);
		
		queryWhere = "select * from " + CommonText.OTHERINFO + " where userId = '"+ BaseActivity.uTokenId +"' order by id desc";
		final Map<String, String[]> commMapArray = dbUtil.queryData(self, queryWhere);
        if (commMapArray!= null && commMapArray.get("userId").length > 0) {
        	initBgColor(index7layout,commMapArray.get("bgcolor")[0]);
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
					sbStr.append("&nbsp;:&nbsp;");
					
					info = commMapArray.get("literacyskills")[position];
					sbStr.append(CommUtil.getStrValue(self, R.string.ot_info_literacyskills)+info);
					sbStr.append("&nbsp;|&nbsp;");
					
					info = commMapArray.get("listeningspeaking")[position];
					sbStr.append(CommUtil.getStrValue(self, R.string.ot_info_listeningspeaking)+info);
					
					holder.setTextForHtml(R.id.item1, sbStr.toString());
				}
			};

			listview1.setAdapter(commMapAdapter);
        	
        }else{
        	index7_layout1.setVisibility(View.GONE);
        }
        
        queryWhere = "select * from " + CommonText.OTHERINFO1 + " where userId = '"+ BaseActivity.uTokenId +"' order by id desc";
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
					sbStr.append("&nbsp;|&nbsp;");
					info = commMapArray2.get("certificatetime")[position];
					sbStr.append(info);
					holder.setTextForHtml(R.id.item1, sbStr.toString());
				}
			};

			listview2.setAdapter(commMapAdapter);
        	
        }else{
        	index7_layout2.setVisibility(View.GONE);
        }
        
        queryWhere = "select * from " + CommonText.OTHERINFO2 + " where userId = '"+ BaseActivity.uTokenId +"' order by id desc";
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
	 * 备注信息
	 * @param view
	 */
	private void initView8(View view){
		mViewList.add(view8);
		
		TextView words = (TextView) view8.findViewById(R.id.item2);
		String wordStr = preferenceUtil.getPreferenceData(Constants.MYWORDS,"");
		if (RegexUtil.checkNotNull(wordStr)) {
			words.setText(Html.fromHtml("&nbsp;&nbsp;"+wordStr));
		}else{
			words.setText(getResources().getString(R.string.resume_mywords));
		}
		
		words.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if (preferenceUtil.getPreferenceData(Constants.EDITMODE)) {
					goFlag = true;
					ActivityUtils.startActivity(self, Constants.PACKAGENAMECHILD + Constants.WORDS,false);
//				}
			}
		});
		ImageView goHome = (ImageView) view8.findViewById(R.id.gohome);
		goHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				preferenceUtil.setPreferenceData(Constants.SET_STARTVERYTIME, false);
				ActivityUtils.startActivity(self, Constants.PACKAGENAMECHILD + Constants.HOME,true);
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
		
		if (preferenceUtil.getPreferenceData(Constants.EDITMODE)) {
			main_top_edit.setVisibility(View.VISIBLE);
		}else{
			main_top_edit.setVisibility(View.GONE);
		}
		
		main_top_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				goFlag = true;
				ActivityUtils.startActivity(self, Constants.PACKAGENAMECHILD + src);
			}
		});
	}
	
	/**
	 * 
	 * @Title:MainActivity
	 * @Description: 设置View bgcolor
	 * @param ll
	 * @param commMapArray
	 */
	private void initBgColor(LinearLayout ll,String bgcolor){
		try {
			if (RegexUtil.checkNotNull(bgcolor)) {
				ll.setBackgroundColor(CommUtil.getColorValue(self,
						CommUtil.parseInt(bgcolor)));
			}
		} catch (Exception e) {
			ll.setBackgroundColor(CommUtil.getColorValue(self,R.color.red));
			e.printStackTrace();
		}
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
			jazzyViewPager.setObjectForPosition(mViewList.get(position), position); 
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
