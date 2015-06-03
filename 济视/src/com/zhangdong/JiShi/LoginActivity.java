package com.zhangdong.JiShi;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhangdong.JiShi.Path.Path;
import com.zhangdong.JiShi.Tools.SpeechApp;
import com.zhangdong.JiShi.Tools.XmlAndJson;
import com.zhangdong.JiShi.wxapi.WXEntryActivity;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends FinalActivity {

	private String loginName;
	private String loginPass;
	private SharedPreferences sp;
	private Editor editor;
	private static String weixinCode;
	private final static int LOGIN_WHAT_INIT = 1;
	private static String get_access_token = "";
	// 获取第一步的code后，请求以下链接获取access_token
	public static String GetCodeRequest = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	//获取用户个人信息
	public static String GetUserInfo="https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID";

	@ViewInject(id=R.id.et_loginname)
	private EditText et_loginname;

	@ViewInject(id=R.id.et_loginpass)
	private EditText et_loginpass;	

	private FinalHttp http;
	private Context mContext;

	@ViewInject(id=R.id.bnt_login,click="login") 
	private Button bnt_login;
	public void login(View v){
		validate();
	}

	@ViewInject(id=R.id.bnt_register,click="register") 
	private Button bnt_register;
	public void register(View v){
		Intent intent =new Intent();
		intent.setClass(getApplicationContext(), RegistActivity.class);
		startActivity(intent);
	}

	@ViewInject(id=R.id.bnt_weixindenglu,click="bnt_weixindenglu") 
	private Button bnt_weixindenglu;
	public void bnt_weixindenglu(View v){
		final SendAuth.Req req = new SendAuth.Req();
		req.scope = "snsapi_userinfo";
		req.state = "wechat_sdk_demo";
		SpeechApp.api.sendReq(req);
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
		setContentView(R.layout.activity_login);
		mContext=this;
		sp = this.getSharedPreferences("userinfo",MODE_PRIVATE);
		editor = sp.edit();
		http=new FinalHttp();
	}


	private void validate(){
		loginName=et_loginname.getText().toString().trim();
		loginPass=et_loginpass.getText().toString().trim();
		String httpUrl=Path.getPath()+"WS_GetUserInfo.asmx/GetUserInfo";
		if(loginName.isEmpty()||loginPass.isEmpty()){
			if(loginName.isEmpty()){
				Toast.makeText(mContext, "请输入用户名", Toast.LENGTH_SHORT).show();
			}else if(loginPass.isEmpty()){
				Toast.makeText(mContext, "请输入密码", Toast.LENGTH_SHORT).show();
			}else {
				Toast.makeText(mContext, "请输入用户名和密码", Toast.LENGTH_SHORT).show();
			}
		}else {
			AjaxParams params=new AjaxParams();
			params.put("uloginid", loginName);
			params.put("upwd", loginPass);
			http.post(httpUrl, params,new AjaxCallBack<String>() {

				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);

					String t1=t.substring(86, 91);
					if(t1.equals("false")){

						editor.putString("loginname", loginName);
						editor.putString("loginpass", loginPass);
						editor.commit();
						Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();

						LoginActivity.this.finish();
					}else{
						Toast.makeText(mContext, "用户名或密码错误", Toast.LENGTH_SHORT).show();
						et_loginname.setFocusable(true);
					}
				}

				@Override
				public void onFailure(Throwable t, String strMsg) {

					super.onFailure(t, strMsg);
					Toast.makeText(mContext, strMsg, Toast.LENGTH_SHORT).show();

				}
			});
		}
	}



	
		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
		
			if(SpeechApp.resp2!=null){
			  if (SpeechApp.resp2.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
				// code返回
				weixinCode = ((SendAuth.Resp)SpeechApp.resp2).code;
				/*
				 * 将你前面得到的AppID、AppSecret、code，拼接成URL
				 */
				get_access_token = getCodeRequest(weixinCode);
				
				
				Thread thread=new Thread(downloadRun);
				thread.start();
				try {
					thread.join();
//					Toast.makeText(getApplicationContext(), "openid="+openid+"nickname="+nickname, 0).show();
					String httpUrl=Path.getPath()+"WS_LoginByWeChat.asmx/APP_JiShi_LoginByWeChat";
					AjaxParams params=new AjaxParams();
					params.put("OpenID",openid);
					params.put("NickName", nickname);
					http.post(httpUrl, params,new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, String strMsg) {
							// TODO Auto-generated method stub
							super.onFailure(t, strMsg);
						}

						@Override
						public void onSuccess(String t) {
							// TODO Auto-generated method stub
							super.onSuccess(t);
							String t1=t.substring(86, 91);
							if(t1.equals("false")){
								editor.putString("openid", openid);
								editor.putString("nickname", nickname);
//								editor.putString("loginpass", null);
								editor.commit();
								Toast.makeText(mContext, "登录成功", Toast.LENGTH_SHORT).show();
								SpeechApp.resp2=null;
								LoginActivity.this.finish();
								
							}
							
						}
						
					});

				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			  }	
			}
		}
	
	

	/**
	 * 获取access_token的URL（微信）
	 * @param code 授权时，微信回调给的
	 * @return URL
	 */
	public static String getCodeRequest(String code) {
		String result = null;
		GetCodeRequest = GetCodeRequest.replace("APPID",
				urlEnodeUTF8(SpeechApp.WX_AppID));
		GetCodeRequest = GetCodeRequest.replace("SECRET",
				urlEnodeUTF8(SpeechApp.WX_AppSecret));
		GetCodeRequest = GetCodeRequest.replace("CODE",urlEnodeUTF8(code));
		result = GetCodeRequest;
		
		return result;
	}

	/**
	 * 获取用户个人信息的URL（微信）
	 * @param access_token 获取access_token时给的
	 * @param openid 获取access_token时给的
	 * @return URL
	 */
	public static String getUserInfo(String access_token,String openid){
		String result = null;
		GetUserInfo = GetUserInfo.replace("ACCESS_TOKEN",
				urlEnodeUTF8(access_token));
		GetUserInfo = GetUserInfo.replace("OPENID",
				urlEnodeUTF8(openid));
		result = GetUserInfo;
		System.out.println("result="+result);
		return result;
	}

	public static String urlEnodeUTF8(String str) {
		String result = str;
		try {
			result = URLEncoder.encode(str, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public  Runnable downloadRun = new Runnable() {

		@Override
		public void run() {
			WXGetAccessToken();

		}
	};
	private String openid;
	private String access_token;
	private String nickname;
	private String headimgurl;

	/**
	 * 获取access_token等等的信息(微信)
	 */
	private  void WXGetAccessToken(){
		HttpClient get_access_token_httpClient = new DefaultHttpClient();
		HttpClient get_user_info_httpClient = new DefaultHttpClient();
		access_token = "";
		openid = "";
		try {
			HttpPost postMethod = new HttpPost(get_access_token);
			HttpResponse response = get_access_token_httpClient.execute(postMethod); // 执行POST方法
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = response.getEntity().getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String str = "";
				StringBuffer sb = new StringBuffer();
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				is.close();
				String josn = sb.toString();
				JSONObject json1 = new JSONObject(josn);
				access_token = (String) json1.get("access_token");
				openid = (String) json1.get("openid");
                System.out.println("access_token="+access_token);
                System.out.println("openid="+openid);
			} else {
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String get_user_info_url=getUserInfo(access_token,openid);
		WXGetUserInfo(get_user_info_url);
	}

	/**
	 * 获取微信用户个人信息
	 * @param get_user_info_url 调用URL
	 */
	private void WXGetUserInfo(String get_user_info_url){
		HttpClient get_access_token_httpClient = new DefaultHttpClient();
		
		nickname = "";
		headimgurl = "";
		try {
			HttpGet getMethod = new HttpGet(get_user_info_url);
			HttpResponse response = get_access_token_httpClient.execute(getMethod); // 执行GET方法
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				InputStream is = response.getEntity().getContent();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(is));
				String str = "";
				StringBuffer sb = new StringBuffer();
				while ((str = br.readLine()) != null) {
					sb.append(str);
				}
				is.close();
				String josn = sb.toString();
				JSONObject json1 = new JSONObject(josn);
				openid = (String) json1.get("openid");
				nickname = (String) json1.get("nickname");
				headimgurl=(String)json1.get("headimgurl");
				System.out.println("openid="+openid);
				System.out.println("nickname="+nickname);
				System.out.println("headimgurl="+headimgurl);
			} else {
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	
	
	
}
