package com.me.resume.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.utils.CommUtil;
import com.me.resume.views.CustomGridView;
import com.umeng.analytics.MobclickAgent;

/**
 * 更多简历封面
 * 
 * @author Administrator
 * 
 */
public class ResumeCoverMoreActivity extends BaseActivity implements
		OnClickListener {

	private CustomGridView covermoregridview, covermoregridview_update;
	private RelativeLayout msgLayout;
	private TextView msgText;
	private Button reloadBtn;

	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView textView1, textView2;
	private List<View> views;// Tab页面列表
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private int bmpW;// 动画图片宽度
	private View view1, view2;// 各个页卡

	// 构建cover本地数据
	private String[] id = { "1", "2", "3", "4", "5", "6" };
	private String[] note = { "心中的梦想", "书香气质", "自强不息", "释放音乐", "一花一世界", "自然清新" };
	private String[] url = { R.drawable.default_cover1 + "",
			R.drawable.default_cover2 + "", R.drawable.default_cover3 + "",
			R.drawable.default_cover4 + "", R.drawable.default_cover5 + "",
			R.drawable.default_cover6 + "" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		boayLayout.removeAllViews();
		View v = View.inflate(self, R.layout.resume_cover_more_layout, null);
		boayLayout.addView(v);

		setTopTitle(R.string.item_text61);
		setMsgHide();
		setRightIconVisible(View.GONE);
		setRight2IconVisible(View.GONE);
		setfabLayoutVisible(View.GONE);

		// msgText.setVisibility(View.VISIBLE);
		// msgText.setText(getStrValue(R.string.item_text43));

		initImageView();
		
		initViewPager();

		initTextView();
		
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		views = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		view1 = inflater.inflate(R.layout.resume_cover_local, null);
		view2 = inflater.inflate(R.layout.resume_cover_local, null);

		covermoregridview = (CustomGridView) view1.findViewById(R.id.covermoregridview);
		covermoregridview_update = (CustomGridView) view2.findViewById(R.id.covermoregridview);
		msgLayout = (RelativeLayout) view2.findViewById(R.id.msgLayout);
		msgText  = (TextView) view2.findViewById(R.id.msgText);
		reloadBtn = (Button) view2.findViewById(R.id.reloadBtn);
		setCoverView(true);
		
		if (CommUtil.isNetworkAvailable(self)) {
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					getReCoverMoreData();
				}
			}, 800);
		}else{
			msgLayout.setVisibility(View.VISIBLE);
			msgText.setText(getString(R.string.check_network));
			covermoregridview_update.setVisibility(View.GONE);
		}

		views.add(view1);
		views.add(view2);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}

	/**
	 * 初始化头标
	 */

	private void initTextView() {
		textView1 = (TextView) findViewById(R.id.text1);
		textView2 = (TextView) findViewById(R.id.text2);

		textView1.setOnClickListener(new MyOnClickListener(0));
		textView2.setOnClickListener(new MyOnClickListener(1));
		
		reloadBtn.setOnClickListener(this);
	}

	/**
	 * 2 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据 3
	 */

	private void initImageView() {
		imageView= (ImageView) findViewById(R.id.cursor);  
        bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.cursor).getWidth();// 获取图片宽度  
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        int screenW = dm.widthPixels;// 获取分辨率宽度  
        offset = (screenW / 2 - bmpW) / 2;// 计算偏移量  
        Matrix matrix = new Matrix();  
        matrix.postTranslate(offset, 0);  
        imageView.setImageMatrix(matrix);// 设置动画初始位置 
    }

	/**
	 * 
	 * 头标点击监听 3
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);
			if(index == 1){
				setRightIcon(R.drawable.icon_redo_del);
			}else{
				setRightIconVisible(View.GONE);
			}
		}

	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		//int two = one * 2;// 页卡1 -> 页卡3 偏移量

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			Animation animation = new TranslateAnimation(one * currIndex, one * arg0, 0, 0);
			currIndex = arg0;
			if(currIndex == 1){
				setRightIcon(R.drawable.icon_redo_del);
			}else{
				setRightIconVisible(View.GONE);
			}
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(300);
			imageView.startAnimation(animation);
		}

	}

	/**
	 * 初始化封面视图(default)
	 * 
	 * @param islocal
	 */
	private void setCoverView(boolean islocal) {
		Map<String, List<String>> map = new HashMap<String, List<String>>();

		List<String> idList = new ArrayList<String>();
		idList = Arrays.asList(id);
		map.put("id", idList);

		List<String> noteList = new ArrayList<String>();
		noteList = Arrays.asList(note);
		map.put("note", noteList);

		List<String> urlList = new ArrayList<String>();
		urlList = Arrays.asList(url);
		map.put("url", urlList);

		setCoverData(covermoregridview, map, islocal);
	}
	
	/**
	 * 面试分享心得
	 */
	private void getReCoverMoreData() {
		List<String> params = new ArrayList<String>();
		List<String> values = new ArrayList<String>();
		requestData("pro_getcover_info", 2, params, values, new HandlerData() {
			@Override
			public void error() {
				msgLayout.setVisibility(View.VISIBLE);
				msgText.setText(getString(R.string.timeout_network));
				covermoregridview_update.setVisibility(View.GONE);
				stopLoad();
			}

			public void success(Map<String, List<String>> map) {
				try {
					msgLayout.setVisibility(View.GONE);
					covermoregridview_update.setVisibility(View.VISIBLE);
					setCoverData(covermoregridview_update, map, false);
					stopLoad();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void noData() {
				stopLoad();
				covermoregridview_update.setVisibility(View.GONE);
			}
		});
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.left_lable:
			scrollToFinishActivity();
			break;
		case R.id.right_icon:
		case R.id.reloadBtn:
			loadWaitting();
			if (CommUtil.isNetworkAvailable(self)) {
				getReCoverMoreData();
			}else{
				msgLayout.setVisibility(View.VISIBLE);
				msgText.setText(getString(R.string.check_network));
				covermoregridview_update.setVisibility(View.GONE);
				stopLoad();
			}
			break;
		default:
			break;
		}
	}
	
}
