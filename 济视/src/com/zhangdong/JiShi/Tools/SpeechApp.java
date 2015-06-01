package com.zhangdong.JiShi.Tools;

import android.app.Application;
import android.support.v4.view.ViewPager;

import com.iflytek.cloud.SpeechUtility;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.zhangdong.JiShi.R;

public class SpeechApp extends Application {
	public static String WX_AppID="wx3e581a8fa7356b61";
	public static String WX_AppSecret="5e9e0e3df5f8862b121a2e3909131712";
	public static IWXAPI api;
	public static BaseResp resp2;
	
	public static int  index=1;
	 public static ViewPager v=null;
	@Override
	public void onCreate() {
		// 应用程序入口处调用,避免手机内存过小,杀死后台进程后通过历史intent进入Activity造成SpeechUtility对象为null
		// 注意：此接口在非主进程调用会返回null对象，如需在非主进程使用语音功能，请增加参数：SpeechConstant.FORCE_LOGIN+"=true"
		// 参数间使用“,”分隔。
		// 设置你申请的应用appid
		
		//注册微信
		api = WXAPIFactory.createWXAPI(this,WX_AppID, true);	
		api.registerApp(WX_AppID);
		SpeechUtility.createUtility(SpeechApp.this, "appid=" + getString(R.string.app_id));
		super.onCreate();
	}
}
