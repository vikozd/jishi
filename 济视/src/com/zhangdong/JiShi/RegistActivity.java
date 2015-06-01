package com.zhangdong.JiShi;



import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.zhangdong.JiShi.Path.Path;
import com.zhangdong.JiShi.Tools.XmlAndJson;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegistActivity extends FinalActivity {
	private FinalHttp http;
	private String uLoginID;
	private String uPassword;
	private String uName="";
	private String uPhone;
	private String uEmail;
	boolean bool=true;
	@ViewInject(id=R.id.et_uloginname) EditText et_uloginname;
	@ViewInject(id=R.id.et_uloginpass) EditText et_uloginpass;
	
	@ViewInject(id=R.id.et_uphone) EditText et_uphone;
	@ViewInject(id=R.id.et_email) EditText et_email;
	@ViewInject(id=R.id.bnt_regist_wancheng,click="bnt_regist_wancheng") Button bnt_regist_wancheng;

	public void bnt_regist_wancheng(View v){
		validate();

	}

	@ViewInject(id=R.id.bnt_back,click="bnt_back_click") Button bnt_back;
	public void bnt_back_click(View v) {
		Intent intent=new Intent();
		intent.setClass(getApplicationContext(), LoginActivity.class);
		
		startActivity(intent);
	}
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_regist);

		http=new FinalHttp();
		et_uloginname.setFocusable(true);
	}


	private void validate() {
		uLoginID=et_uloginname.getText().toString().trim();
		uPassword=et_uloginpass.getText().toString().trim();
		
		uPhone=et_uphone.getText().toString().trim();
		uEmail=et_email.getText().toString().trim();
		String httpUrl=Path.getPath()+"WS_AddNewUser1.asmx/AddNewUser1";
		if(uLoginID.isEmpty()||uPassword.isEmpty()||uName.isEmpty()||uPhone.isEmpty()||uEmail.isEmpty()){
			bool=false;
			if(uLoginID.isEmpty()){
				et_uloginname.setFocusable(true);
				et_uloginname.setFocusableInTouchMode(true);
				et_uloginname.requestFocus();
				Toast.makeText(getApplicationContext(), "用户名不能为空", Toast.LENGTH_SHORT).show();
			}else if(uPassword.isEmpty()){
				et_uloginpass.setFocusable(true);
				et_uloginpass.setFocusableInTouchMode(true);
				et_uloginpass.requestFocus();
				Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
			}else if(uPassword.length()<6 || uPassword.length()>16) {
				et_uloginpass.setFocusable(true);
				et_uloginpass.setFocusableInTouchMode(true);
				et_uloginpass.requestFocus();
				Toast.makeText(getApplicationContext(), "密码为6-16位", Toast.LENGTH_SHORT).show();
			}else if(uPhone.isEmpty()){
				et_uphone.setFocusable(true);
				et_uphone.setFocusableInTouchMode(true);
				et_uphone.requestFocus();
				Toast.makeText(getApplicationContext(), "手机号码不能为空", Toast.LENGTH_SHORT).show();
			}else if(uEmail.isEmpty()){
				et_email.setFocusable(true);
				et_email.setFocusableInTouchMode(true);
				et_email.requestFocus();
				Toast.makeText(getApplicationContext(), "邮箱不能为空", Toast.LENGTH_SHORT).show();
			}
		}else{
			bool=true;
		}
		if(!isMobileNO(uPhone)||!isEmail(uEmail)){

			bool=false;
			if (!isMobileNO(uPhone)) {
				et_uphone.setFocusable(true);
				et_uphone.setFocusableInTouchMode(true);
				et_uphone.requestFocus();
				Toast.makeText(getApplicationContext(), "请输入正确的手机号", Toast.LENGTH_SHORT).show();
			}else if(!isEmail(uEmail)){
				et_email.setFocusable(true);
				et_email.setFocusableInTouchMode(true);
				et_email.requestFocus();
				Toast.makeText(getApplicationContext(), "请输入正确的邮箱号码", Toast.LENGTH_SHORT).show();
			}
		}else{
			bool=true;
		}
		if(bool){
			AjaxParams params=new AjaxParams();
			params.put("uName", uName);
			params.put("uPhone", uPhone);
			params.put("uLoginId", uLoginID);
			params.put("uPassword", uPassword);
			params.put("CreatedBy", uLoginID);
			params.put("uEmail", uEmail);
			http.post(httpUrl, params,new AjaxCallBack<String>() {
				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					String ErrorDesc="";
					try {
						String jsonString=XmlAndJson.cc(t);
						if(jsonString!=null){
							JSONObject jsonObject = new JSONObject(jsonString);
							ErrorDesc = jsonObject.getString("ErrorDesc");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if(ErrorDesc!=null&&!"".equals(ErrorDesc)&&ErrorDesc.equals("成功")){
						Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
						Intent intent=new Intent();
						intent.setClass(getApplicationContext(), RegistSuccessActivity.class);		
						startActivity(intent);
					}else if (ErrorDesc!=null&&!"".equals(ErrorDesc)&&ErrorDesc.equals("帐号重复")) {
						et_uloginname.setFocusable(true);
						et_uloginname.setFocusableInTouchMode(true);
						et_uloginname.requestFocus();
						Toast.makeText(getApplicationContext(), "帐号重复", Toast.LENGTH_SHORT).show();
					}else if (ErrorDesc!=null&&!"".equals(ErrorDesc)&&ErrorDesc.equals("手机号重复")) {
						et_uphone.setFocusable(true);
						et_uphone.setFocusableInTouchMode(true);
						et_uphone.requestFocus();
						Toast.makeText(getApplicationContext(), "手机号重复", Toast.LENGTH_SHORT).show();
					}
				}
				@Override
				public void onFailure(Throwable t, String strMsg) {
					// TODO Auto-generated method stub
					super.onFailure(t, strMsg);
					Toast.makeText(getApplicationContext(), "无法连接网络", Toast.LENGTH_SHORT).show();
				}
			});
		}



	}

	//判断手机格式是否正确
	public boolean isMobileNO(String mobiles){
		Pattern p=Pattern.compile("[1][358]\\d{9}");
		Matcher m=p.matcher(mobiles);
		return m.matches();
	}

	//判断email格式是否正确
	public boolean isEmail(String email){
		String str="^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p=Pattern.compile(str);
		Matcher m=p.matcher(email);
		return m.matches();

	}




}
