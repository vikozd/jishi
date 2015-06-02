package com.zhangdong.JiShi;

import java.util.ArrayList;
import java.util.List;
import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.Button;

public class GuideActivity extends FinalActivity implements OnTouchListener {
	private GestureDetector gestureDetector; // 用户滑动
	/** 记录当前分页ID */
	private int flaggingWidth;// 互动翻页所需滚动的长度是当前屏幕宽度的1/3
	private ViewPager viewPager;
	private List<View> listView;
	private List<View> listDots;

	private int thePos = 0; // 当前的View的索引
	private int oldPosition;

	@ViewInject(id = R.id.drump, click = "bnt_drump_click")
	Button bnt_drump;

	public void bnt_drump_click(View v) {
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), MainActivity.class);

		startActivity(intent);
		this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
			finish();
			return;
		}

		setContentView(R.layout.activity_guide);
		gestureDetector = new GestureDetector(new GuideViewTouch());

		// 获取分辨率
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		flaggingWidth = dm.widthPixels / 3;
		initViewPager();
		initDots();
		if (isFirstStart()) {
			setTarget();
			setContentView(R.layout.activity_guide);
			initViewPager();
			initDots();

		} else {
			Intent intent = new Intent();
			intent.setClass(this, LoadActivity.class);
			startActivity(intent);
			finish();
		}
	}

	/** 判断是否是第一次启动 */
	private boolean isFirstStart() {
		SharedPreferences share = getSharedPreferences("fs", MODE_PRIVATE);
		String target = share.getString("isfs", "0");
		if (target.equals("0")) {
			// 是第一次启动
			return true;
		} else {
			// 不是第一次启动
			return false;
		}
	}

	/** 第一次启动后设置标志 */
	private void setTarget() {
		SharedPreferences share = getSharedPreferences("fs", MODE_PRIVATE);
		Editor editor = share.edit();
		editor.putString("isfs", "no");
		editor.commit();
	}

	private void initViewPager() {
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		listView = new ArrayList<View>();
		View page1 = getLayoutInflater().inflate(R.layout.guide_1, null);
		View page2 = getLayoutInflater().inflate(R.layout.guide_2, null);

		listView.add(page1);
		listView.add(page2);

		for (int i = 0; i < listView.size(); i++) {
			View view = (View) listView.get(i);

			view.setOnTouchListener(this);
		}

		viewPager.setAdapter(new MyPagerAdapter(listView));
		viewPager.setOnPageChangeListener(new MyPagerChangeListener());

	}

	private void initDots() {
		listDots = new ArrayList<View>();
		listDots.add(findViewById(R.id.dot01));
		listDots.add(findViewById(R.id.dot02));
	}

	public class MyPagerChangeListener implements OnPageChangeListener {

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int position) {
			((View) listDots.get(position))
					.setBackgroundResource(R.drawable.dot_focused);
			((View) listDots.get(oldPosition))
					.setBackgroundResource(R.drawable.dot_normal);
			oldPosition = position;
			thePos = position;
			System.out.println(thePos);
		}

	}

	public class MyPagerAdapter extends PagerAdapter {
		private List<View> list;

		public MyPagerAdapter(List<View> list) {
			this.list = list;
		}

		public void destroyItem(View view, int index, Object arg2) {
			((ViewPager) view).removeView(list.get(index));
		}

		@Override
		public int getCount() {
			return list.size();
		}

		public Object instantiateItem(View view, int index) {
			((ViewPager) view).addView(list.get(index), 0);
			return list.get(index);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

	private class GuideViewTouch extends SimpleOnGestureListener {
		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (viewPager.getCurrentItem() == 1) {

				if (Math.abs(e1.getX() - e2.getX()) > Math.abs(e1.getY()
						- e2.getY())
						&& (e1.getX() - e2.getX() <= (-flaggingWidth) || e1
								.getX() - e2.getX() >= flaggingWidth)) {
					if (e1.getX() - e2.getX() >= flaggingWidth) {
						startActivity(new Intent(getApplicationContext(),
								MainActivity.class));
						GuideActivity.this.finish();
						return true;
					}
				}

			}

			return false;
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (gestureDetector.onTouchEvent(event)) {
			event.setAction(MotionEvent.ACTION_CANCEL);
		}
		return super.dispatchTouchEvent(event);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		return false;
	}

}
