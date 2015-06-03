package com.zhangdong.JiShi;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.annotation.view.ViewInject;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyCenterActivity extends FinalActivity {
	private SharedPreferences sp;
	private String loginname;
	private Editor editor;
	@ViewInject(id=R.id.tv_loginname)TextView tv_loginname;
	@ViewInject(id=R.id.ll_favorite,click="ll_favorite_click")
	LinearLayout ll_favorite;
	public void ll_favorite_click(View v) {
		Intent it=new Intent();
		it.setClass(getApplicationContext(), MyFavoritesActivity.class);
		it.putExtra("loginname", tv_loginname.getText().toString().trim());
		startActivity(it);		
	}
	
	
	@ViewInject(id=R.id.ll_history,click="ll_history_click")
	LinearLayout ll_history;
	public void ll_history_click(View v) {
		Intent it=new Intent();
		it.setClass(getApplicationContext(), SeeHistoryActivity.class);
		it.putExtra("loginname", tv_loginname.getText().toString().trim());
		startActivity(it);
		
	}
	
	
	@ViewInject(id=R.id.ll_userinfo,click="ll_userinfo_click")
	LinearLayout ll_userinfo_click; 
	public void ll_userinfo_click(View v){
		
	}
	
	@ViewInject(id=R.id.bnt_exit,click="bnt_exit_click")
	Button bnt_exit;
	public void bnt_exit_click(View v){
			new AlertDialog.Builder(this).setTitle("确认退出吗？")  
		    .setIcon(android.R.drawable.ic_dialog_info)  
		    .setPositiveButton("确定", new DialogInterface.OnClickListener() {  
		  
		        @Override  
		        public void onClick(DialogInterface dialog, int which) {  
		        // 点击“确认”后的操作  
		        	
		    		editor.clear().commit();
		    		
		    		Intent intent=new Intent();
		    		intent.setClass(getApplicationContext(), MainActivity.class);
		    		startActivity(intent);
		  
		        }  
		    })  
		    .setNegativeButton("返回", new DialogInterface.OnClickListener() {  
		  
		        @Override  
		        public void onClick(DialogInterface dialog, int which) {  
		        // 点击“返回”后的操作,这里不设置没有任何操作  
		        }  
		    }).show();
		
		
		
	}

	
	@ViewInject(id=R.id.bnt_back,click="bnt_back_click") Button bnt_back;
	public void bnt_back_click(View v) {
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), MainActivity.class);
		
		startActivity(intent);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_my_center);
		sp=this.getSharedPreferences("userinfo",MODE_PRIVATE);
		editor = sp.edit();
		if(!sp.getString("openid", "").equals("")){
			loginname=sp.getString("nickname", "");
			tv_loginname.setText(loginname);			
		}else {
			loginname=sp.getString("loginname", "");
			tv_loginname.setText(loginname);	
		}
		
		
	}
	
	

	

}
