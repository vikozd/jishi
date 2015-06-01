package com.zhangdong.JiShi;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class OutofLoginActivity extends FinalActivity {
	@ViewInject(id=R.id.bnt_outoflogin,click="bnt_outoflogin_click")
	Button button;
	public void bnt_outoflogin_click(View v) {
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), LoginActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_outoflogin);
	}

	

}
