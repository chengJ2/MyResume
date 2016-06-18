package com.me.resume.ui;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.me.resume.BaseActivity;
import com.me.resume.R;
import com.me.resume.comm.CalendarViewAdapter;
import com.me.resume.model.CustomDate;
import com.me.resume.views.CalendarCardView;
import com.me.resume.views.CalendarCardView.OnCellClickListener;

/**
 * 行事历
 * @author Administrator
 * 
 */
public class TodoActivity extends BaseActivity implements OnClickListener,
		OnCellClickListener {

	private TextView curMonth;
	private TextView preMonth;
	private TextView nextMonth;
	private ViewPager mViewPager;
	private int mCurrentIndex = 498;
	private CalendarCardView[] mShowViews;
	private CalendarViewAdapter<CalendarCardView> adapter;
	private SildeDirection mDirection = SildeDirection.NO_SILDE;

	public enum SildeDirection {
		RIGHT, LEFT, NO_SILDE;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		boayLayout.removeAllViews();
		View v = View.inflate(self, R.layout.activity_todolist_layout, null);
		boayLayout.addView(v);

		initViews();

		initData();
	}

	private void initViews() {
		setMsgHide();
		setTopBarVisibility(View.GONE);
		setfabLayoutVisible(View.GONE);

		mViewPager = findView(R.id.calendarPager);
		curMonth = findView(R.id.curMonth);
		preMonth = findView(R.id.preMonth);
		nextMonth = findView(R.id.nextMonth);

		preMonth.setOnClickListener(this);
		curMonth.setOnClickListener(this);
		nextMonth.setOnClickListener(this);

	}

	private void initData() {
		CalendarCardView[] views = new CalendarCardView[3];
		for (int i = 0; i < 3; i++) {
			views[i] = new CalendarCardView(self);
		}
		adapter = new CalendarViewAdapter<CalendarCardView>(views);
		setViewPager();
	}

	@Override
	public void clickDate(CustomDate date) {
		String str = date.year + "-" + date.month + "-" + date.day
				+ " 00:00:00";
	}

	@Override
	public void changeDate(CustomDate date) {
		curMonth.setText(date.year + "年" + date.month + "月");
	}

	/**
	 * 计算方向
	 * 
	 * @param arg0
	 */
	private void measureDirection(int arg0) {
		if (arg0 > mCurrentIndex) {
			mDirection = SildeDirection.RIGHT;

		} else if (arg0 < mCurrentIndex) {
			mDirection = SildeDirection.LEFT;
		}
		mCurrentIndex = arg0;
	}

	// 更新日历视图
	private void updateCalendarView(int arg0) {
		mShowViews = adapter.getAllItems();
		if (mDirection == SildeDirection.RIGHT) {
			mShowViews[arg0 % mShowViews.length].rightSlide();
		} else if (mDirection == SildeDirection.LEFT) {
			mShowViews[arg0 % mShowViews.length].leftSlide();
		}
		mDirection = SildeDirection.NO_SILDE;
	}

	private void setViewPager() {
		mViewPager.setAdapter(adapter);
		mViewPager.setCurrentItem(498);
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				measureDirection(position);
				updateCalendarView(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.preMonth:
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
			break;
		case R.id.curMonth:
			mViewPager.setCurrentItem(mViewPager.getCurrentItem());
			break;
		case R.id.nextMonth:
			mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
			break;
		case R.id.newAlert:
			break;
		default:
			break;
		}

	}
}
