package com.zhangdong.JiShi;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangdong.JiShi.Path.Path;
import com.zhangdong.JiShi.Tools.MyScrollView;
import com.zhangdong.JiShi.Tools.XmlParser;
import com.zhangdong.JiShi.Tools.MyScrollView.OnScrollListener;
import com.zhangdong.JiShi.Tools.VerticalSeekBar;
import com.zhangdong.JiShi.Fragment.Frag1;
import com.zhangdong.JiShi.Fragment.Frag1.PlayOther;
import com.zhangdong.JiShi.Fragment.Frag2;
import com.zhangdong.JiShi.Fragment.Frag3;
import com.zhangdong.JiShi.Fragment.Frag4;
import com.zhangdong.util.OneVideoMore;
import com.zhangdong.util.Video;
import com.zhangdong.util.VideoTagData;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.OrientationEventListener;
import android.view.OrientationListener;
import android.view.SurfaceHolder;
import android.view.Window;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class VideoActivity extends FragmentActivity implements
		com.zhangdong.JiShi.Tools.VerticalSeekBar.OnSeekBarChangeListener,
		OnScrollListener, OnClickListener, PlayOther {
	private Animation animation;
	private ImageView bt; // 用于开始和暂停的按钮
	private SurfaceView pView; // 绘图容器对象，用于把视频显示在屏幕上
	private String url; // 视频播放地址
	private MediaPlayer mediaPlayer; // 播放器控件
	private int postSize; // 保存义播视频位子
	private SeekBar seekbar; // 进度条控件
	private boolean flag = false; // 用于判断视频是否在播放中
	private LinearLayout ll, ll0, lltag;
	private boolean display; // 用于是否显示其他按钮
	private ProgressBar pb; // ProgressBar
	private upDateSeekBar update; // 更新进度条用
	private ImageView imageView1, iv_back;// 放大缩小
	private TextView time, title, nowtime, viewtitle, viewdetail, playnum,
			zannum;
	private LinearLayout ll2, ll_top;
	Map<String, Object> m, m2;
	List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
	VerticalSeekBar soundBar;
	private TextView soundvalues;
	private AudioManager audiomanage;
	private int maxVolume;
	private int currentVolume;
	private TextView text1, text2, text3, text4, text5, text6, text7, text8,
			tv_one;
	private MyScrollView myScrollView;
	private LinearLayout mtitleLayout;
	private LinearLayout mTopBtltleLayout;
	Frag1 fg1;
	Frag2 fg2;
	Frag3 fg3;
	Frag4 fg4;
	// TextView[] texts = new TextView[8];
	TextView[] texts = new TextView[6];
	List<Video> listVideo, listVideo1, listVideo2, listVideo3, listVideo4;
	private SharedPreferences sp;
	private String loginname;
	private String VID;

	private ImageView shoucang, shoucang2, zanzan, zhuanfa, zhuanfa2;
	boolean blsc;// false未被收藏
	private Video video;
	boolean b;// 按钮控制视屏状态；

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN); // 全屏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON); // 应用运行时，保持屏幕高亮，不锁屏

		init(); // 初始化数据
		setListener(); // 绑定相关事件
		

	}

	int width, height;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startListener();
		Toast.makeText(this, "onResume", 0).show();
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		if (!sp.getString("openid", "").equals("")) {
			loginname = sp.getString("openid", "");
		} else {
			loginname = sp.getString("loginname", "");
		}
		WindowManager wm = this.getWindowManager();
		if (width <= 0) {
			if (VideoActivity.this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				height = wm.getDefaultDisplay().getWidth();
				width = wm.getDefaultDisplay().getHeight();

				LayoutParams layoutParams = ll2.getLayoutParams();
				layoutParams.width = height;
				layoutParams.height = width;
				ll2.setLayoutParams(layoutParams);
			} else {

				width = wm.getDefaultDisplay().getWidth();
				height = wm.getDefaultDisplay().getHeight();
			}

		}

	}

	// 横屏竖屏

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Toast.makeText(this, "onPause", 0).show();
		if (mediaPlayer.isPlaying()) {
			bt.setImageResource(R.drawable.movieplay);
			mediaPlayer.pause();
			flag = false;
			postSize = mediaPlayer.getCurrentPosition();
		}
		mOrientationListener.disable();

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		// 检测屏幕的方向：纵向或横向

		// TODO Auto-generated method stub
		if (VideoActivity.this.getResources().getConfiguration().orientation

		== Configuration.ORIENTATION_LANDSCAPE) {
			// 当前为横屏， 在此处添加额外的处理代码
			ViewGroup.LayoutParams lp = ll2.getLayoutParams();
			lp.width = height;
			lp.height = width;
			ll2.setLayoutParams(lp);
			imageView1.setImageResource(R.drawable.minscreen);
			display = true;

		}

		else if (VideoActivity.this.getResources().getConfiguration().orientation

		== Configuration.ORIENTATION_PORTRAIT) {

			// 当前为竖屏， 在此处添加额外的处理代码

			ViewGroup.LayoutParams lp = ll2.getLayoutParams();
			lp.width = width;
			lp.height = DipToPixels(this, 250);
			;
			ll2.setLayoutParams(lp);
			display = true;
			imageView1.setImageResource(R.drawable.maxscreen);

		}
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				getwiandHeiht();
			}
		}, 100);

	}

	@Override
	public void onScroll(int scrollY) {
		int mBuyLayout2ParentTop = Math.max(scrollY, mtitleLayout.getTop());
		mTopBtltleLayout.layout(0, mBuyLayout2ParentTop,
				mTopBtltleLayout.getWidth(), mBuyLayout2ParentTop
						+ mTopBtltleLayout.getHeight());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		/*
		 * case R.id.text1: select(0); switchFragment(1); break; case
		 * R.id.text2: select(1); switchFragment(2); break; case R.id.text3:
		 * select(2); switchFragment(3); break; case R.id.text4: select(3);
		 * switchFragment(4); break; case R.id.text5: select(4);
		 * switchFragment(1); break; case R.id.text6: select(5);
		 * switchFragment(2); break; case R.id.text7: select(6);
		 * switchFragment(3); break; case R.id.text8: select(7);
		 * switchFragment(4); break;
		 */
		case R.id.text1:
			select(0);
			switchFragment(1);
			break;
		case R.id.text2:
			select(1);
			switchFragment(2);
			break;
		case R.id.text3:
			select(2);
			switchFragment(3);
			break;
		case R.id.text5:
			select(3);
			switchFragment(1);
			break;
		case R.id.text6:
			select(4);
			switchFragment(2);
			break;
		case R.id.text7:
			select(5);
			switchFragment(3);
			break;
		case R.id.shoucang:
			shoucang();
			break;
		case R.id.shoucang2:
			shoucang();
			break;
		case R.id.zanzan:
			updateVideoCounter(VID);
			break;
		case R.id.zhuanfa:
			showShare(video);
			break;
		case R.id.zhuanfa2:
			showShare(video);
			break;
		case R.id.iv_back:
			VideoActivity.this.finish();
			break;

		}

	}

	/*
	 * public void select(int textId) { if (textId <= 3) { for (int i = 0; i <
	 * texts.length; i++) {
	 * texts[i].setTextColor(getResources().getColor(R.color.light_black));
	 * texts[textId].setTextColor(getResources().getColor(R.color.red));
	 * texts[textId + 4].setTextColor(getResources().getColor(R.color.red)); } }
	 * else { for (int i = 0; i < texts.length; i++) {
	 * texts[i].setTextColor(getResources().getColor(R.color.light_black));
	 * texts[textId].setTextColor(getResources().getColor(R.color.red));
	 * texts[textId - 4].setTextColor(getResources().getColor(R.color.red)); } }
	 * }
	 */
	public void select(int textId) {
		if (textId <= 2) {
			for (int i = 0; i < texts.length; i++) {
				texts[i].setTextColor(getResources().getColor(
						R.color.light_black));
				texts[textId]
						.setTextColor(getResources().getColor(R.color.red));
				texts[textId + 3].setTextColor(getResources().getColor(
						R.color.red));
			}
		} else {
			for (int i = 0; i < texts.length; i++) {
				texts[i].setTextColor(getResources().getColor(
						R.color.light_black));
				texts[textId]
						.setTextColor(getResources().getColor(R.color.red));
				texts[textId - 3].setTextColor(getResources().getColor(
						R.color.red));
			}
		}
	}

	public void switchFragment(int fg) {

		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction tran = fm.beginTransaction();
		switch (fg) {
		case 1:
			tran.replace(R.id.frag, fg1);
			fg1.getListVideo(listVideo1);
			break;
		case 2:
			tran.replace(R.id.frag, fg2);
			fg2.getListVideo(listVideo2);
			break;
		case 3:
			tran.replace(R.id.frag, fg3);
			fg3.getListVideo(listVideo3);
			break;
		case 4:
			tran.replace(R.id.frag, fg4);
			fg4.getListVideo(listVideo4);
			break;

		}

		tran.commit();

	}

	// 初始化

	@SuppressWarnings("deprecation")
	private void init() {

		String videojson = getIntent().getStringExtra("videojson");
		Gson g = new Gson();
		listVideo = g.fromJson(videojson, new TypeToken<List<Video>>() {
		}.getType());
		listVideo1 = new ArrayList<Video>();
		listVideo2 = new ArrayList<Video>();
		listVideo3 = new ArrayList<Video>();
		listVideo4 = new ArrayList<Video>();
		mediaPlayer = new MediaPlayer(); // 创建一个播放器对象
		update = new upDateSeekBar(); // 创建更新进度条对象
		setContentView(R.layout.activity_video); // 加载布局文件

		seekbar = (SeekBar) findViewById(R.id.seekbar); // 进度条
		seekbar.setEnabled(false);
		bt = (ImageView) findViewById(R.id.play);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		bt.setEnabled(false); // 刚进来，设置其不可点击
		pView = (SurfaceView) findViewById(R.id.mSurfaceView);
		pView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS); // 不缓冲
		pView.getHolder().setKeepScreenOn(true); // 保持屏幕高亮
		pView.getHolder().addCallback(new surFaceView()); // 设置监听事件
		ll = (LinearLayout) findViewById(R.id.ll);
		ll0 = (LinearLayout) findViewById(R.id.ll0);
		ll2 = (LinearLayout) findViewById(R.id.ll2);
		lltag = (LinearLayout) findViewById(R.id.lltag);
		ll_top = (LinearLayout) findViewById(R.id.ll_top);
		pb = (ProgressBar) findViewById(R.id.pb);
		imageView1 = (ImageView) findViewById(R.id.imageView1);
		shoucang = (ImageView) findViewById(R.id.shoucang);
		shoucang2 = (ImageView) findViewById(R.id.shoucang2);
		zanzan = (ImageView) findViewById(R.id.zanzan);
		zhuanfa = (ImageView) findViewById(R.id.zhuanfa);
		zhuanfa2 = (ImageView) findViewById(R.id.zhuanfa2);
		shoucang.setOnClickListener(this);
		shoucang2.setOnClickListener(this);
		zanzan.setOnClickListener(this);
		zhuanfa.setOnClickListener(this);
		zhuanfa2.setOnClickListener(this);
		iv_back.setOnClickListener(this);
		time = (TextView) findViewById(R.id.time);
		tv_one = (TextView) findViewById(R.id.tv_one);
		animation = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.zanjiayi);
		nowtime = (TextView) findViewById(R.id.nowtime);
		title = (TextView) findViewById(R.id.title);
		soundBar = (VerticalSeekBar) findViewById(R.id.sound); // 音量设置
		soundvalues = (TextView) findViewById(R.id.soundvalues);
		audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		maxVolume = audiomanage.getStreamMaxVolume(AudioManager.STREAM_MUSIC); // 获取系统最大音量
		soundBar.setMax(maxVolume); // 拖动条最高值与系统最大声匹配
		currentVolume = audiomanage.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
		soundBar.setProgress(currentVolume);
		ll_top.setFocusable(true);
		ll_top.setFocusableInTouchMode(true);
		ll_top.requestFocus();
		soundvalues.setText(currentVolume * 100 / maxVolume + " %");
		fg1 = new Frag1();
		fg2 = new Frag2();
		fg3 = new Frag3();
		fg4 = new Frag4();
		text1 = (TextView) findViewById(R.id.text1);
		text2 = (TextView) findViewById(R.id.text2);
		text3 = (TextView) findViewById(R.id.text3);
		// text4 = (TextView) findViewById(R.id.text4);
		text5 = (TextView) findViewById(R.id.text5);
		text6 = (TextView) findViewById(R.id.text6);
		text7 = (TextView) findViewById(R.id.text7);
		// text8 = (TextView) findViewById(R.id.text8);
		viewdetail = (TextView) findViewById(R.id.viewdetail);
		viewtitle = (TextView) findViewById(R.id.viewtitle);
		zannum = (TextView) findViewById(R.id.zannum);
		playnum = (TextView) findViewById(R.id.playnum);
		/*
		 * texts[0] = text1; texts[1] = text2; texts[2] = text3; texts[3] =
		 * text4; texts[4] = text5; texts[5] = text6; texts[6] = text7; texts[6]
		 * = text7; texts[7] = text8;
		 */
		texts[0] = text1;
		texts[1] = text2;
		texts[2] = text3;
		texts[3] = text5;
		texts[4] = text6;
		texts[5] = text7;

		for (int i = 0; i < texts.length; i++) {
			texts[i].setOnClickListener(this);
		}

		myScrollView = (MyScrollView) findViewById(R.id.scrollView);
		mtitleLayout = (LinearLayout) findViewById(R.id.title1);
		mTopBtltleLayout = (LinearLayout) findViewById(R.id.top_title);
		for (Video v : listVideo) {
			int vtid = Integer.parseInt(v.getVTID());
			switch (vtid) {
			case 1:
				listVideo1.add(v);
				break;
			case 2:
				listVideo2.add(v);
				break;
			case 3:
				listVideo3.add(v);
				break;
			case 4:
				listVideo4.add(v);
				break;

			}

		}
		select(0);
		switchFragment(1);
		myScrollView.setOnScrollListener(this);
		findViewById(R.id.parent_layout).getViewTreeObserver()
				.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						onScroll(myScrollView.getScrollY());
						System.out.println(myScrollView.getScrollY());
					}
				});

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
			Animation animation1 = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.out_to_top);
			Animation animation2 = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.out_to_bottom);

			pb.setVisibility(View.GONE); // 准备完成后，隐藏控件
			bt.setVisibility(View.GONE);
			ll.setAnimation(animation1);
			ll.setVisibility(View.GONE);
			soundBar.setVisibility(View.GONE);
			ll0.setAnimation(animation2);
			ll0.setVisibility(View.GONE);
			display = false;
			if (mediaPlayer != null) {
				mediaPlayer.start(); // 开始播放视频
				// seekbar.setMax(mediaPlayer.getDuration());
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
			Toast.makeText(VideoActivity.this, "surfaceChanged", 0).show();

		}

		@Override
		public void surfaceCreated(SurfaceHolder holder) { // 创建完成后调用
			if (postSize > 0 && url != null) { // 说明，停止过activity调用过pase方法，跳到停止位置播放
				new PlayMovie(postSize, url).start();
				flag = true;
				int sMax = seekbar.getMax();
				int mMax = mediaPlayer.getDuration();
				seekbar.setProgress(postSize * sMax / mMax);
				Toast.makeText(VideoActivity.this, "surfaceCreated 旧activity", 0).show();

				// seekbar.setProgress(mediaPlayer.getCurrentPosition());
				pb.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(VideoActivity.this, "surfaceCreated 新activity", 0).show();

				video = listVideo.get(0);
				VID = listVideo.get(0).getVID();
				url = listVideo.get(0).getvURL();
				title.setText(listVideo.get(0).getvTitle());
				viewtitle.setText(listVideo.get(0).getvTitle());
				viewdetail.setText(video.getvDoctorName() + " "
						+ video.getvDoctorDepartment() + " "
						+ video.getvDoctorHospital());

				GetVideoInfoByVID(VID, loginname);

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

		soundBar.setOnSeekBarChangeListener(VideoActivity.this);

		imageView1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mClick = true;
				if (!mIsLand) {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					mIsLand = true;
					mClickLand = false;
				} else {
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					mIsLand = false;
					mClickPort = false;
				}

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
						// TODO Auto-generated method stub
						getwiandHeiht();

					}
				});

		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() { // 视频播放完成
					@Override
					public void onCompletion(MediaPlayer mp) {
						flag = false;
						bt.setImageResource(R.drawable.movieplay);
					}
				});
		mediaPlayer.setOnInfoListener(new OnInfoListener() {

			@Override
			public boolean onInfo(MediaPlayer arg0, int what, int extra) {
				// TODO Auto-generated method stub
				if (what == MediaPlayer.MEDIA_INFO_BAD_INTERLEAVING) {
					Log.i("tag", "Media info,Media info bad interleaveing "
							+ extra);
				} else if (what == MediaPlayer.MEDIA_INFO_METADATA_UPDATE) {
					Log.i("tag", "Media info,Media info metadata update "
							+ extra);
				} else if (what == MediaPlayer.MEDIA_INFO_NOT_SEEKABLE) {
					Log.i("tag", "Media info,Media info not_seekable " + extra);
				} else if (what == MediaPlayer.MEDIA_INFO_UNKNOWN) {
					Log.i("tag", "Media info,Media info unknown " + extra);
				} else if (what == MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING) {
					Log.i("tag", "Media info,Media info video track lagging "
							+ extra);
				}
				return false;
			}
		});
		mediaPlayer.setOnErrorListener(new OnErrorListener() {

			@Override
			public boolean onError(MediaPlayer arg0, int what, int extra) {
				// TODO Auto-generated method stub
				Log.i("tag", "===onError()===");
				if (what == MediaPlayer.MEDIA_ERROR_SERVER_DIED) {
					Log.i("tag", "Media Error,Server Died! " + extra);
				} else if (what == MediaPlayer.MEDIA_ERROR_UNKNOWN) {
					Log.i("tag", "Media Error, Unknow Error! " + extra);
				}
				return false;
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
					bt.setImageResource(R.drawable.movieplay);
					mediaPlayer.pause();
					flag = false;
					postSize = mediaPlayer.getCurrentPosition();
				} else {
					flag = true;
					mediaPlayer.start();
					bt.setImageResource(R.drawable.moviepause);
					new Thread(update).start(); // 重新启动线程，更新进度条
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
				// mediaPlayer.seekTo(seekbar.getProgress());

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
					Animation animation1 = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.out_to_top);
					Animation animation2 = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.out_to_bottom);
					bt.setVisibility(View.GONE);
					ll.setAnimation(animation1);
					ll0.setAnimation(animation2);
					ll.setVisibility(View.GONE);
					ll0.setVisibility(View.GONE);
					soundBar.setVisibility(View.GONE);
					display = false;
				} else {
					Animation animation3 = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.in_from_top);
					Animation animation4 = AnimationUtils.loadAnimation(
							getApplicationContext(), R.anim.in_from_bottom);
					ll.setAnimation(animation3);
					ll0.setAnimation(animation4);
					ll.setVisibility(View.VISIBLE);
					ll0.setVisibility(View.VISIBLE);
					bt.setVisibility(View.VISIBLE);
					pView.setVisibility(View.VISIBLE);
					// soundBar.setVisibility(View.VISIBLE);
					display = true;
				}

			}
		});

	}

	/**
	 * 根据视屏Id,uid查询视屏其他相关信息
	 */
	List<VideoTagData> vtd;

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
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						Toast.makeText(VideoActivity.this, strMsg, 0).show();
					}

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
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
								shoucang.setImageResource(R.drawable.star);
								shoucang2
										.setImageResource(R.drawable.shoucang2);

							} else {

								shoucang.setImageResource(R.drawable.bt_star_a);
								shoucang2
										.setImageResource(R.drawable.shoucang1);

							}
							zannum.setText(ovm.get(0).getCounterGood());
							playnum.setText(ovm.get(0).getCounterPlay());
							if ("1".equals(ovm.get(0).getIsFavorite())) {
								blsc = true;
							} else {
								blsc = false;
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
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
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						Toast.makeText(VideoActivity.this, strMsg, 0).show();
					}

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						try {
							JSONObject joResult = new JSONObject(XmlParser
									.xmltojson(t));
							String ErrorDesc = joResult.getString("ErrorDesc");
							if (ErrorDesc.equals("收藏成功")) {
								shoucang.setImageResource(R.drawable.star);
								shoucang2
										.setImageResource(R.drawable.shoucang2);
								blsc = true;
								Toast.makeText(VideoActivity.this, ErrorDesc, 0)
										.show();
							} else {
								shoucang.setImageResource(R.drawable.bt_star_a);
								shoucang2
										.setImageResource(R.drawable.shoucang1);

								blsc = false;
								Toast.makeText(VideoActivity.this, "取消收藏", 0)
										.show();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

				});
	}

	/**
	 * 点赞
	 */
	void updateVideoCounter(String VID) {

		FinalHttp http = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("VID", VID);
		params.put("Type", "2");// 2表示点赞
		http.post(Path.getPath()
				+ "WS_UpdateVideoCounter.asmx/UpdateVideoCounter", params,
				new AjaxCallBack<String>() {

					@Override
					public void onFailure(Throwable t, String strMsg) {
						// TODO Auto-generated method stub
						super.onFailure(t, strMsg);
						Toast.makeText(VideoActivity.this, strMsg, 0).show();
					}

					@Override
					public void onSuccess(String t) {
						// TODO Auto-generated method stub
						super.onSuccess(t);
						zanzan.setImageResource(R.drawable.zan2);
						tv_one.setVisibility(View.VISIBLE);
						tv_one.startAnimation(animation);
						new Handler().postDelayed(new Runnable() {
							public void run() {
								tv_one.setVisibility(View.GONE);
							}
						}, 1000);
						zannum.setText(String.valueOf(Integer.parseInt(zannum
								.getText().toString()) + 1));
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
	int mintues, nowmintues, seconds, nowseconds;
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
				/*
				 * time.setText(mMax / 3600000 + ":" + mMax % 3600000 / 60000 +
				 * ":" + mMax % 3600000 % 60000 / 1000);
				 * nowtime.setText(position / 3600000 + ":" + position % 3600000
				 * / 60000 + ":" + position % 3600000 % 60000 / 1000);
				 */
				mintues = mMax % 3600000 / 60000;
				seconds = mMax % 3600000 % 60000 / 1000;
				nowmintues = position % 3600000 / 60000;
				nowseconds = position % 3600000 % 60000 / 1000;

				if (mintues >= 10) {
					mintuesString = mintues + "";
				} else {
					mintuesString = "0" + mintues;
				}
				if (seconds >= 10) {
					secondsString = seconds + "";
				} else {
					secondsString = "0" + seconds;
				}

				if (nowmintues >= 10) {
					nowmintuesString = nowmintues + "";
				} else {
					nowmintuesString = "0" + nowmintues;
				}
				if (nowseconds >= 10) {
					nowsecondsString = nowseconds + "";
				} else {
					nowsecondsString = "0" + nowseconds;
				}
				time.setText(mintuesString + ":" + secondsString);
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
		Toast.makeText(this, "onDestroy", 0).show();
		

	}

	@Override
	public void onStopTrackingTouch(SeekBar VerticalSeekBar) {
		switch (VerticalSeekBar.getId()) {
		case R.id.sound:

			Log.v("ww", "www");

			break;

		}

	}

	@Override
	public void onStartTrackingTouch(SeekBar VerticalSeekBar) {
		switch (VerticalSeekBar.getId()) {
		case R.id.sound:

			Log.v("ww", "www1");

			break;

		}

	}

	@Override
	public void onProgressChanged(SeekBar VerticalSeekBar, int progress,
			boolean fromUser) {

		switch (VerticalSeekBar.getId()) {
		case R.id.sound:

			Log.v("ww", "www2");

			audiomanage.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
			currentVolume = audiomanage
					.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前值
			soundBar.setProgress(currentVolume);
			soundvalues.setText(currentVolume * 100 / maxVolume + " %");
			break;

		}

	}

	// 音量物理键控制

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
			audiomanage.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND);
			soundBar.setProgressAndThumb(audiomanage
					.getStreamVolume(AudioManager.STREAM_MUSIC));
			return true;
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			audiomanage.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
			soundBar.setProgressAndThumb(audiomanage
					.getStreamVolume(AudioManager.STREAM_MUSIC));
			return true;
		case KeyEvent.KEYCODE_BACK:
			if (mIsLand == true) {
				setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
				/**
				 * 设置播放为非全屏
				 */
				ViewGroup.LayoutParams lp = ll2.getLayoutParams();
				lp.height = 0;
				ll2.setLayoutParams(lp);
				display = true;
				mIsLand = false;

			} else {
				this.finish();
			}
			return true;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	public void shoucang() {
		if (loginname.equals("")) {
			startActivity(new Intent(VideoActivity.this, LoginActivity.class));
		} else {
			if (blsc) {
				updateMyFavorites(loginname, VID, "2");
			} else {
				updateMyFavorites(loginname, VID, "1");
			}
		}
	}

	@Override
	public void playotherMovie(Video v) {
		// TODO Auto-generated method stub
		video = v;
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			flag = false;
		}
		pb.setVisibility(View.VISIBLE);
		VID = v.getVID();
		url = v.getvURL();
		title.setText(v.getvTitle());
		viewtitle.setText(v.getvTitle());
		viewdetail.setText(video.getvDoctorName() + " "
				+ video.getvDoctorDepartment() + " "
				+ video.getvDoctorHospital());

		GetVideoInfoByVID(VID + "", loginname);
		new PlayMovie(0, v.getvURL()).start();
		lltag.removeAllViews();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Toast.makeText(VideoActivity.this, "onStop", 0).show();
	}

	private void showShare(Video v) {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();
		// 关闭sso授权
		oks.disableSSOWhenAuthorize();

		// 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(v.getvTitle());
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		// oks.setTitleUrl("http://sharesdk.cn");
		// text是分享文本，所有平台都需要这个字段
		oks.setText(v.getvTitle());
		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImagePath("file://android_asset/dabai.png");// 确保SDcard下面存在此张图片
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl("www.baidu.com");
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		// oks.setComment("我是测试评论文本");
		// site是分享此内容的网站名称，仅在QQ空间使用
		// oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		// oks.setSiteUrl("http://sharesdk.cn");

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

		if (heightRatio > widthRatio) {// 以高为准
			mVideoHeight = (int) Math.ceil((float) mVideoHeight
					/ (float) heightRatio);
			mVideoWidth = (int) Math.ceil((float) mVideoWidth
					/ (float) heightRatio);
		} else {// 以宽为准
			mVideoHeight = (int) Math.ceil((float) mVideoHeight
					/ (float) widthRatio);
			mVideoWidth = (int) Math.ceil((float) mVideoWidth
					/ (float) widthRatio);
		}

		ViewGroup.LayoutParams lp = pView.getLayoutParams();
		lp.width = mVideoWidth;
		lp.height = mVideoHeight;
		pView.setLayoutParams(lp);
		lltag.setLayoutParams(lp);
	}

	int i;

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
					View view = v.findViewById(R.id.ll_tag);
					tagTextView.setText(vtd.get(vtd.size() - 1 - i)
							.getVtTagText());

					final VideoTagData vv = vtd.get(vtd.size() - 1 - i);
					v.setAnimation(animation);
					lltag.addView(v);
					view.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) { // TODOAuto-generated
														// method stub
							Intent intent = new Intent(VideoActivity.this,
									VideoMainActivity.class);
							intent.putExtra("VID", vv.getVideoID());
							startActivity(intent);
						}
					});
					vtd.remove(vtd.size() - 1 - i);
				}
			}

		}

	}

	// dip转像素
	public int DipToPixels(Context context, int dip) {
		final float SCALE = context.getResources().getDisplayMetrics().density;

		float valueDips = dip;
		int valuePixels = (int) (valueDips * SCALE + 0.5f);

		return valuePixels;

	}

	/**
	 * 开启监听器
	 */
	private boolean mIsLand = false; // 是否是横屏
	private boolean mClick = false; // 是否点击
	private boolean mClickLand = true; // 点击进入横屏
	private boolean mClickPort = true; // 点击进入竖屏
	private OrientationEventListener mOrientationListener; // 屏幕方向改变监听器
	private final void startListener() {
		mOrientationListener = new OrientationEventListener(this) {
			@Override
			public void onOrientationChanged(int rotation) {
				// 设置竖屏
				Log.v("qq", rotation + "");

				if (((rotation >= 0) && (rotation <= 45))
						|| ((rotation > 135) && (rotation < 225))
						|| ((rotation > 315) && (rotation < 360))) {
					if (mClick) {
						if (mIsLand && !mClickLand) {
							return;
						} else {
							mClickPort = true;
							mClick = false;
							mIsLand = false;
						}
					} else {
						if (mIsLand) {
							setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
							mIsLand = false;
							mClick = false;
						}
					}
				}

				// 横屏

				if (((rotation > 45) && (rotation <= 135))
						|| ((rotation >= 225) && (rotation <= 315))) {
					if (mClick) {
						if (!mIsLand && !mClickPort) {
							return;
						} else {
							mClickLand = true;
							mClick = false;
							mIsLand = true;
						}
					} else {
						if (!mIsLand) {
							setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
							mIsLand = true;
							mClick = false;
						}
					}
				}

			}

		};
		mOrientationListener.enable();
	}

}