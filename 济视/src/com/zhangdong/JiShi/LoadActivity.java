package com.zhangdong.JiShi;

import java.util.Timer;



import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

public class LoadActivity extends Activity {
	ImageView imageView;
	Timer timer=new Timer();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {  
			finish();
			return;
		}

		setContentView(R.layout.activity_load);
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(2000);
					Intent intent=new Intent();
					intent.setClass(getApplicationContext(), MainActivity.class);
					startActivity(intent);
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
		imageView=(ImageView) findViewById(R.id.imageView1);
		Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.load);
		imageView.setAnimation(animation);

	}





	//		TimerTask task=new TimerTask() {
	//			@Override
	//			public void run() {
	//				// TODO Auto-generated method stub
	//
	//				Intent intent=new Intent();
	//				intent.setClass(getApplicationContext(), MainActivity.class);
	//				startActivity(intent);
	//				finish();
	//			}
	//		};
	//		timer.schedule(task, 2500);


}
