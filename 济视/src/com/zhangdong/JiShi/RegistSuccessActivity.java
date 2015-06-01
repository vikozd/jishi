package com.zhangdong.JiShi;

import android.os.Bundle;
import android.app.Activity;

import android.content.Intent;
import android.view.Menu;
import android.view.Window;


public class RegistSuccessActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist_success);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					Thread.sleep(1500);
					Intent intent=new Intent();
					intent.setClass(getApplicationContext(), MainActivity.class);
					startActivity(intent);
					finish();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	
}
