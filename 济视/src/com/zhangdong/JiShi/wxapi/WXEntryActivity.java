package com.zhangdong.JiShi.wxapi;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.zhangdong.JiShi.R;
import com.zhangdong.JiShi.Tools.SpeechApp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	private Context context=WXEntryActivity.this;
	public static String code;

    
	private void handleIntent(Intent paramIntent) {
		SpeechApp.api.handleIntent(paramIntent, this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_wxentry);
		handleIntent(getIntent());
	}


	@Override
	public void onReq(BaseReq req) {
		finish();
	}

	@Override
	public void onResp(BaseResp resp) {
		String result = "";

		switch(resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result ="发送成功";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			
			code = ((SendAuth.Resp) resp).code;
			if (resp != null) {
				SpeechApp.resp2 = resp;
			}
			System.out.println("微信确认登录返回的code：" + code);
			System.out.println(SpeechApp.resp2);
			finish();
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "发送取消";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "发送被拒绝";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		default:
			result = "发送返回";
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			finish();
			break;
		}
		
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
		finish();
	}
	
	

}
