package com.zhangdong.JiShi;

import java.util.ArrayList;
import java.util.List;

import com.zhangdong.JiShi.Tools.DrawerToast;
import com.zhangdong.JiShi.Tools.SpeechApp;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class MainActivity extends Activity {
	Toast toast;
	private boolean isExit; 
	public ViewPager viewPager;
	private Context context;
	public LocalActivityManager manager;
	private int offset = 0;// 偏移量
	//	private DrawerToast toast;  
	private static int currIndex=SpeechApp.index;// 页卡编号
	private int bmpW;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		//		toast = DrawerToast.getInstance(getApplicationContext());
		context = MainActivity.this;
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		initViewPager();

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SearchActivity sa=(SearchActivity) manager.getActivity("SearchActivity");
		sa.onResume();
		IndexActivity ia=(IndexActivity) manager.getActivity("IndexActivity");
		ia.onResume();

	}

	private void initViewPager() {
		//		String t=this.getIntent().getStringExtra("index");



		viewPager = (ViewPager) findViewById(R.id.vp_index);
		final ArrayList<View> list = new ArrayList<View>();

		Intent intent2 = new Intent(context, BodyActivity.class);
		list.add(getView("BodyActivity", intent2));

		Intent intent = new Intent(context, IndexActivity.class);
		list.add(getView("IndexActivity", intent));

		Intent intent3 = new Intent(context, SearchActivity.class);
		list.add(getView("SearchActivity", intent3));


		viewPager.setAdapter(new MyPagerAdapter(list));
		SpeechApp.v=viewPager;

		viewPager.setCurrentItem(1);


		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	private View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	public class MyPagerAdapter extends PagerAdapter {
		List<View> list = new ArrayList<View>();

		public MyPagerAdapter(ArrayList<View> list) {
			this.list = list;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			ViewPager pViewPager = ((ViewPager) container);
			pViewPager.removeView(list.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			ViewPager pViewPager = ((ViewPager) arg0);
			pViewPager.addView(list.get(arg1));
			return list.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {

		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}
	}

	/**
	 * 页卡切换监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
		int two = one * 2;// 页卡1 -> 页卡3 偏移量

		@Override
		public void onPageSelected(int arg0) {
			Animation animation = null;
			switch (arg0) {

			case 0:
				if (currIndex == 1) {
					animation = new TranslateAnimation(one, 0, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, 0, 0, 0);
				}
				break;
			case 1:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, one, 0, 0);
				} else if (currIndex == 2) {
					animation = new TranslateAnimation(two, one, 0, 0);
				}
				break;
			case 2:
				if (currIndex == 0) {
					animation = new TranslateAnimation(offset, two, 0, 0);
				} else if (currIndex == 1) {
					animation = new TranslateAnimation(one, two, 0, 0);
				}
				break;
			default:
				break;
			}
			currIndex = arg0;
			// animation.setFillAfter(false);// True:图片停在动画结束位置
			animation.setDuration(300);

		}

		@Override
		public void onPageScrollStateChanged(int arg0) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/**
	 * 头标点击监听
	 */
	public class MyOnClickListener implements View.OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	};

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if(viewPager.getCurrentItem()!=1){
				viewPager.setCurrentItem(1);
			}else {
				exit();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void exit(){  
		if (!isExit) {  
			isExit = true;
			if(toast == null)
				toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
			toast.setText("再按一次退出程序");
			toast.show();
			

			mHandler.sendEmptyMessageDelayed(0, 2000);  
		} else {  
			Intent intent = new Intent(Intent.ACTION_MAIN);  
			intent.addCategory(Intent.CATEGORY_HOME);  
			startActivity(intent);  
			System.exit(0);  
		}  

	}  

	Handler mHandler = new Handler() {  		  
		@Override  
		public void handleMessage(Message msg) {  
			// TODO Auto-generated method stub   
			super.handleMessage(msg);  
			isExit = false;  
		}   
	};

}
