package com.zhangdong.JiShi.wxapi;

import cn.sharesdk.wechat.utils.WXAppExtendObject;
import cn.sharesdk.wechat.utils.WXMediaMessage;
import cn.sharesdk.wechat.utils.WechatHandlerActivity;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.zhangdong.JiShi.R;
import com.zhangdong.JiShi.Tools.SpeechApp;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class WXEntryActivity extends WechatHandlerActivity implements
		IWXAPIEventHandler {
	private WXEntryActivity context = WXEntryActivity.this;
	public static String code;

	/*
	 * 处理微信发出的向第三方应用请求app message <p> 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
	 * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可 做点其他的事情，包括根本不打开任何页面
	 */

	public void onGetMessageFromWXReq(WXMediaMessage msg) {
		Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(
				getPackageName());
		startActivity(iLaunchMyself);
	}

	/*
	 * 处理微信向第三方应用发起的消息<p> 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
	 * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作 回调。
	 * <p> 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
	 */

	public void onShowMessageFromWXReq(WXMediaMessage msg) {
		if (msg != null && msg.mediaObject != null
				&& (msg.mediaObject instanceof WXAppExtendObject)) {
			WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
			Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
		}
	}

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
		if (resp instanceof SendAuth.Resp) {

			String result = "";

			switch (resp.errCode) {
			case BaseResp.ErrCode.ERR_OK:
				result = "发送成功";
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

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		handleIntent(intent);
		finish();
	}

}
