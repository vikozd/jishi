package com.zhangdong.JiShi;



import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class BackOfBodyActivity extends FinalActivity {
	@ViewInject(id=R.id.bnt_body,click="bnt_bodyClick") Button bnt_body;
	
	public void bnt_bodyClick(View v){
		Intent intent=new Intent();
		intent.setClass(this, BodyActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_back_of_body);
		
	}

	

}
