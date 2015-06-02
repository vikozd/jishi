package com.zhangdong.JiShi;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangdong.JiShi.Path.Path;
import com.zhangdong.JiShi.Tools.XmlParser;
import com.zhangdong.util.OneVideoMore;
import com.zhangdong.util.Video;
import com.zhangdong.util.VideoTagData;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.Window;
import android.view.SurfaceHolder.Callback;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class VideoMainActivity extends FinalActivity{
	private Toast toast;
	private Button bt; // 用于开始和暂停的按钮
	private SurfaceView pView; // 绘图容器对象，用于把视频显示在屏幕上
	private String url; // 视频播放地址
	private MediaPlayer mediaPlayer; // 播放器控件
	private int postSize; // 保存义播视频位子
	private SeekBar seekbar; // 进度条控件
	private  boolean flag = false; // 用于判断视频是否在播放中
	private LinearLayout ll, ll0,lltag,ll2;
	private boolean display; // 用于是否显示其他按钮
	private ProgressBar pb; // ProgressBar
	private upDateSeekBar update; // 更新进度条用
	private Button bnt_back;// 放大缩小
	private TextView time, title, nowtime
	;
	private SharedPreferences sp;
	private String loginname;
	private String VID;

	private Button shoucang;
	boolean blsc;// false未被收藏
	private String vTitle;
	Button zhuanfa;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 应用运行时，保持屏幕高亮，不锁屏

		init(); // 初始化数据
		setListener(); // 绑定相关事件

	}


	// 初始化

	@SuppressWarnings("deprecation")
	private void init() {
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		loginname = sp.getString("loginname", "");
		VID=getIntent().getStringExtra("VID");
		vTitle=getIntent().getStringExtra("vTitle");
		url=getIntent().getStringExtra("vURL");

		mediaPlayer = new MediaPlayer(); // 创建一个播放器对象
		update = new upDateSeekBar(); // 创建更新进度条对象
		setContentView(R.layout.activity_videomain); // 加载布局文件
		GetVideoInfoByVID(VID,loginname);
		seekbar = (SeekBar) findViewById(R.id.seekbar); // 进度条
		seekbar.setEnabled(false);
		bt = (Button) findViewById(R.id.play);
		bnt_back = (Button) findViewById(R.id.bnt_back);
		bt.setEnabled(false); // 刚进来，设置其不可点击
		pView = (SurfaceView) findViewById(R.id.mSurfaceView);
		pView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 不缓冲
		pView.getHolder().setKeepScreenOn(true); // 保持屏幕高亮
		pView.getHolder().addCallback(new surFaceView()); // 设置监听事件
		ll = (LinearLayout) findViewById(R.id.ll);
		ll0 = (LinearLayout) findViewById(R.id.ll0);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		lltag= (LinearLayout) findViewById(R.id.lltag);
		pb = (ProgressBar) findViewById(R.id.pb);
		shoucang = (Button) findViewById(R.id.shoucang);
		zhuanfa= (Button) findViewById(R.id.share);
		time = (TextView) findViewById(R.id.time);
		nowtime = (TextView) findViewById(R.id.nowtime);
		title = (TextView) findViewById(R.id.tv_vediotitle);
		title.setText(vTitle);
		

	}

	// 播放视频的线程
	class PlayMovie extends Thread {

		int post = 0;
		String url;

		public PlayMovie(int post, String url) {
			this.post = post;
			this.url = url;

		}

		@Override
		public void run() {
			Message message = Message.obtain();
			try {
				Log.i("hck", "runrun  " + url);
				mediaPlayer.reset(); // 回复播放器默认
				mediaPlayer.setDataSource(url); // 设置播放路径
				mediaPlayer.setDisplay(pView.getHolder()); // 把视频显示在SurfaceView上
				mediaPlayer.setOnPreparedListener(new Ok(post)); // 设置监听事件
				mediaPlayer.prepareAsync(); // 准备播放
			} catch (Exception e) {
				message.what = 2;
				Log.e("hck", e.toString());
			}

			super.run();
		}
	}

	class Ok implements OnPreparedListener {
		int postSize;

		public Ok(int postSize) {
			this.postSize = postSize;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out_to_top);
			Animation animation2=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out_to_bottom);


			pb.setVisibility(View.GONE); // 准备完成后，隐藏控件
			bt.setVisibility(View.GONE);
			ll.setAnimation(animation1);
			ll.setVisibility(View.GONE);
			ll0.setAnimation(animation2);
			ll0.setVisibility(View.GONE);
			display = false;
			if (mediaPlayer != null) {
				mediaPlayer.start(); // 开始播放视频
				//seekbar.setMax(mediaPlayer.getDuration());
				bt.setEnabled(true);
				flag = true;
				seekbar.setEnabled(true);
			} else {
				return;
			}
			if (postSize > 0) { // 说明中途停止过（activity调用过stop方法，不是用户点击停止按钮），跳到停止时候位置开始播放
				Log.i("hck", "seekTo ");
				mediaPlayer.seekTo(postSize); // 跳到postSize大小位置处进行播放
			}
			new Thread(update).start(); // 启动线程，更新进度条
		}
	}

	private class surFaceView implements Callback { // 上面绑定的监听的事件

		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width,
				int height) {
		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) { // 创建完成后调用
			if (postSize > 0 && url != null) { // 说明，停止过activity调用过pase方法，跳到停止位置播放
				new PlayMovie(postSize, url).start();
				flag = true;

				int sMax = seekbar.getMax(); int mMax =
						mediaPlayer.getDuration(); seekbar.setProgress(postSize *
								sMax / mMax);

						//seekbar.setProgress(mediaPlayer.getCurrentPosition());
						pb.setVisibility(View.VISIBLE);
			} else {
				
				new PlayMovie(0, url).start(); // 表明是第一次开始播放

			}
		}

		@Override
		public void surfaceDestroyed(SurfaceHolder holder) { // activity调用过pase方法，保存当前播放位置
			if (mediaPlayer != null && mediaPlayer.isPlaying()) {
				postSize = mediaPlayer.getCurrentPosition();
				mediaPlayer.stop();
				flag = false;
				pb.setVisibility(View.VISIBLE);
			}
		}
	}

	private void setListener() {

		bnt_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				VideoMainActivity.this.finish();
			}
		});

		zhuanfa.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Video v=new Video();
				v.setVID(VID);
				v.setvTitle(vTitle);
				v.setvURL(url);
				showShare(v);
				
			}
		});
		mediaPlayer
		.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
			@Override
			public void onBufferingUpdate(MediaPlayer mp,
					int bufferingProgress) {
				seekbar.setSecondaryProgress(bufferingProgress);
			}
		});
		mediaPlayer
		.setOnVideoSizeChangedListener(new OnVideoSizeChangedListener() {

			@Override
			public void onVideoSizeChanged(MediaPlayer mp, int width,
					int height) {
				getwiandHeiht();

			}
		});


		mediaPlayer
		.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // 视频播放完成
			@Override
			public void onCompletion(MediaPlayer mp) {
				flag = false;
				bt.setBackgroundResource(R.drawable.movieplay);
			}
		});


		/**
		 * 如果视频在播放，则调用mediaPlayer.pause();，停止播放视频，反之，mediaPlayer.start()
		 * ，同时换按钮背景
		 */
		bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mediaPlayer.isPlaying()) {
					bt.setBackgroundResource(R.drawable.movieplay);
					mediaPlayer.pause();
					flag = false;
					postSize = mediaPlayer.getCurrentPosition();
				} else {
					flag = true;
					mediaPlayer.start();
					bt.setBackgroundResource(R.drawable.moviepause);
					new Thread(update).start(); // 重新启动线程，更新进度条
				}
			}
		});

		shoucang.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (blsc) {
					updateMyFavorites(loginname, VID, "2");
				} else {
					updateMyFavorites(loginname, VID, "1");
				}
			}
		});
		
		seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {


				int value = seekbar.getProgress() * mediaPlayer.getDuration()
						/ seekbar.getMax();
				// 计算进度条需要前进的位置数据大小
				mediaPlayer.seekTo(value);
				showViewTag();
				//mediaPlayer.seekTo(seekbar.getProgress());

			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				showViewTag();
			}
		});
		/**
		 * 点击屏幕，切换控件的显示，显示则应藏，隐藏，则显示
		 */
		pView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (display) {
					Animation animation1=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out_to_top);
					Animation animation2=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.out_to_bottom);					
					bt.setVisibility(View.GONE);
					ll.setAnimation(animation1);
					ll0.setAnimation(animation2);
					ll.setVisibility(View.GONE);
					ll0.setVisibility(View.GONE);
					display = false;
				} else {
					Animation animation3=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_from_top);
					Animation animation4=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_from_bottom);
					ll.setAnimation(animation3);
					ll0.setAnimation(animation4);
					ll.setVisibility(View.VISIBLE);
					ll0.setVisibility(View.VISIBLE);
					bt.setVisibility(View.VISIBLE);
					pView.setVisibility(View.VISIBLE);
					//					soundBar.setVisibility(View.VISIBLE);
					display = true;
				}

			}
		});

	}


	/**
	 * 根据视屏Id,uid查询视屏其他相关信息
	 */
	void GetVideoInfoByVID(String VID, String LoginID) {
		FinalHttp http = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("VID", VID);
		params.put("LoginID", LoginID);
		http.post(Path.getPath()
				+ "WS_GetVideoInfoByVID.asmx/GetVideoInfoByVID", params,
				new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				Toast.makeText(VideoMainActivity.this, strMsg, 0).show();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject joResult = new JSONObject(XmlParser
							.xmltojson(t));
					Gson g = new Gson();
					List<OneVideoMore> ovm = g.fromJson(
							joResult.getString("VideoData"),
							new TypeToken<List<OneVideoMore>>() {
							}.getType());
					vtd = g.fromJson(
							joResult.getString("VideoTagData"),
							new TypeToken<List<VideoTagData>>() {
							}.getType());
					if (ovm.get(0).getIsFavorite() != null
							&& ovm.get(0).getIsFavorite().equals("1")) {
						shoucang.setBackgroundResource(R.drawable.star);

					}else{

						shoucang.setBackgroundResource(R.drawable.bt_star_a);

					}
					
					if ("1".equals(ovm.get(0).getIsFavorite())) {
						blsc = true;
					} else {
						blsc = false;
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
	}


	/**
	 * 添加、删除我的收藏
	 */
	void updateMyFavorites(String uLoginID, String VID, String Type) {
		FinalHttp http = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("uLoginID", uLoginID);
		params.put("VID", VID);
		params.put("UFID", "0");
		params.put("Type", Type);
		params.put("UID", "0");
		http.post(Path.getPath()
				+ "WS_UpdateMyFavorites.asmx/UpdateMyFavorites", params,
				new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				Toast.makeText(VideoMainActivity.this, strMsg, 0).show();
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				try {
					JSONObject joResult = new JSONObject(XmlParser
							.xmltojson(t));
					String ErrorDesc = joResult.getString("ErrorDesc");
					if (ErrorDesc.equals("收藏成功")) {
						shoucang.setBackgroundResource(R.drawable.star);
						blsc = true;
						if(toast == null)
							toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
						toast.setText("收藏成功");
						toast.show();
						
					} else {
						shoucang.setBackgroundResource(R.drawable.bt_star_a);

						blsc = false;
						if(toast == null)
							toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
						toast.setText("取消收藏");
						toast.show();
						
					}


				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
	}


	/**
	 * 更新进度条
	 */
	String mintuesString;
	String secondsString;
	String nowmintuesString;
	String nowsecondsString;
	int mintues,nowmintues,seconds,nowseconds;
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			if (mediaPlayer == null) {
				flag = false;
			} else if (mediaPlayer.isPlaying()) {
				flag = true;
				int position = mediaPlayer.getCurrentPosition();
				int mMax = mediaPlayer.getDuration();
				int sMax = seekbar.getMax();
				seekbar.setProgress(position * sMax / mMax);
				/*time.setText(mMax / 3600000 + ":" + mMax % 3600000 / 60000
						+ ":" + mMax % 3600000 % 60000 / 1000);
				nowtime.setText(position / 3600000 + ":" + position % 3600000
						/ 60000 + ":" + position % 3600000 % 60000 / 1000);*/
				 mintues=mMax % 3600000 / 60000;
				 seconds=mMax % 3600000
						% 60000 / 1000;
				 nowmintues=position % 3600000
							/ 60000;
				 nowseconds=position % 3600000 % 60000 / 1000;
				
				if(mintues>=10){
					mintuesString=mintues+"";
				}else {
					mintuesString="0"+mintues;
				}
				if(seconds>=10){
					secondsString=seconds+"";
				}else{
					secondsString="0"+seconds;
				}
				
				if(nowmintues>=10){
					nowmintuesString=nowmintues+"";
				}else {
					nowmintuesString="0"+nowmintues;
				}
				if(nowseconds>=10){
					nowsecondsString=nowseconds+"";
				}else{
					nowsecondsString="0"+nowseconds;
				}
				time.setText(mintuesString + ":" +secondsString);
				nowtime.setText(nowmintuesString + ":" + nowsecondsString);

			}
		};
	};

	
	
	class upDateSeekBar implements Runnable {

		@Override
		public void run() {
			mHandler.sendMessage(Message.obtain());
			if (flag) {
				mHandler.post(update);
			}
		}
	}

	@Override
	protected void onDestroy() { // activity销毁后，释放资源
		super.onDestroy();
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}

	}




	public void shoucang(){
		if (loginname.equals("")) {
			startActivity(new Intent(VideoMainActivity.this,
					LoginActivity.class));
		} else {
			if (blsc) {
				updateMyFavorites(loginname, VID, "2");
			} else {
				updateMyFavorites(loginname, VID, "1");
			}
		}
	}


	private void showShare(Video v) {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		//关闭sso授权
		oks.disableSSOWhenAuthorize(); 

		// 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
		//oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(v.getvTitle());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(v.getvTitle());
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("file://android_asset/dabai.png");//确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("www.baidu.com");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		// oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		//oks.setSiteUrl("http://sharesdk.cn");

		// 启动分享GUI
		oks.show(this);
	}

	void getwiandHeiht() {
		// 设置表面大小以匹配视频或显示器大小，取决于那个大小较小
		int mVideoWidth = mediaPlayer.getVideoWidth();
		int mVideoHeight = mediaPlayer.getVideoHeight();
		int screenWidth = ll2.getWidth();
		int screenHeight = ll2.getHeight();
		// 如果视频宽度大于显示器大小，得到比率
		float heightRatio = (float) mVideoHeight / (float) screenHeight;
		float widthRatio = (float) mVideoWidth / (float) screenWidth;

		if (heightRatio > widthRatio) {//以高为准 
			mVideoHeight = (int) Math.ceil((float)mVideoHeight / (float)heightRatio); 
			mVideoWidth = (int) Math.ceil((float)mVideoWidth / (float)heightRatio);    
		} else {//以宽为准 
			mVideoHeight = (int) Math.ceil((float)mVideoHeight / (float)widthRatio); 
			mVideoWidth = (int) Math.ceil((float)mVideoWidth / (float)widthRatio); 
		} 


		ViewGroup.LayoutParams lp = pView.getLayoutParams();
		lp.width = mVideoWidth;
		lp.height = mVideoHeight;
		pView.setLayoutParams(lp);
		lltag.setLayoutParams(lp);
	}

	int i;
	List<VideoTagData> vtd;
	void showViewTag() {
		int position = mediaPlayer.getCurrentPosition();
		Animation animation=AnimationUtils.loadAnimation(getApplicationContext(), R.anim.in_from_right);
		
		if (vtd != null && vtd.size() > 0) {

			for (i = 0; i < vtd.size(); i++) {

				if (Integer.parseInt(vtd.get(vtd.size() - 1 - i)
						.getVtDisplayTime()) * 1000 < position

						) {
					View v = getLayoutInflater().inflate(R.layout.tag, null);
					TextView tagTextView = (TextView) v
							.findViewById(R.id.textView1);
					RelativeLayout ll_tag=(RelativeLayout) v.findViewById(R.id.ll_tag);
					tagTextView.setText(vtd.get(vtd.size() - 1 - i)
							.getVtTagText());
					ll_tag.setAnimation(animation);
					final VideoTagData vv = vtd.get(vtd.size() - 1 - i);

					lltag.addView(v);
					v.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) { // TODOAuto-generated
														// method stub
							Intent intent=new Intent(VideoMainActivity.this,
									  VideoMainActivity.class);
							intent.putExtra("VID", vv.getVideoID());
							intent.putExtra("vTitle", vv.getVtTagText());
							intent.putExtra("vURL", vv.getvURL());
							  startActivity(intent);
							 
							  Toast.makeText(VideoMainActivity.this, "qq", 0).show();

						}
					});

					vtd.remove(vtd.size() - 1 - i);

				}
			}

		}

	}





}