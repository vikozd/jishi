package com.zhangdong.JiShi.Tools;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;

public class LongPressView extends View{
	private static final String TAG = "LongPressView";  

	private int mLastMotionX, mLastMotionY;  
	private boolean isMoved;  
	//长按的runnable  
	private Runnable mLongPressRunnable;  
	//移动的阈值  
	private static final int TOUCH_SLOP = 20;  

	public LongPressView(final Context context) {  
		super(context);  

		mLongPressRunnable = new Runnable() {  
			@Override  
			public void run() {  

				Log.i(TAG, "mLongPressRunnable excuted");  

				performLongClick(); // 执行长按事件（如果有定义的话）  
			}  
		};  
	}  
	public boolean dispatchTouchEvent(MotionEvent event) {  
		int x = (int) event.getX();  
		int y = (int) event.getY();  

		switch(event.getAction()) {  
		case MotionEvent.ACTION_DOWN:  

			mLastMotionX = x;  
			mLastMotionY = y;  
			isMoved = false;  
			/* 
			 * 将mLongPressRunnable放进任务队列中，到达设定时间后开始执行 
			 * 这里的长按时间采用系统标准长按时间 
			 */  
			postDelayed(mLongPressRunnable, ViewConfiguration.getLongPressTimeout());  
			break;  
		case MotionEvent.ACTION_MOVE:  

			if( isMoved ) break;  

			if( Math.abs(mLastMotionX-x) > TOUCH_SLOP   
					|| Math.abs(mLastMotionY-y) > TOUCH_SLOP ) {  
				//移动超过阈值，则表示移动了  
				isMoved = true;  
				removeCallbacks(mLongPressRunnable);  
			}  
			break;  
		case MotionEvent.ACTION_UP:  
			//释放了  
			removeCallbacks(mLongPressRunnable);  
			break;  
		}  
		return true;  
	}  

}  


