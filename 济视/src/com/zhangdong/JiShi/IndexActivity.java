package com.zhangdong.JiShi;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.sunflower.FlowerCollector;

import com.zhangdong.JiShi.MainActivity.MyOnPageChangeListener;
import com.zhangdong.JiShi.Path.Path;
import com.zhangdong.JiShi.Tools.JsonParser;
import com.zhangdong.JiShi.Tools.SpeechApp;
import com.zhangdong.JiShi.Tools.XmlParser;
import com.zhangdong.util.vkKeyword;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import android.os.Bundle;
import android.os.Environment;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class IndexActivity extends FinalActivity implements OnClickListener {
	MyOnPageChangeListener myOnPageChangeListener;
	private static String TAG = MainActivity.class.getSimpleName();
	// 语音听写对象
	private SpeechRecognizer mIat;
	// 用HashMap存储听写结果
	private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

	private EditText mResultText;
	private Toast mToast;
	private SharedPreferences mSharedPreferences;
	private SharedPreferences sp;
	private String loginname;
	// 引擎类型
	private String mEngineType = SpeechConstant.TYPE_CLOUD;

	Button bt1, bt2, bt3, bt4;
	RelativeLayout ll_help;
	LinearLayout ll, linear1, linear2;
	Boolean b = true;
	String likeUsers = "";
	View view;
	TextView mTextView, txt2,textView1,textView2;
	ImageView img1, img7;
	RelativeLayout rll, show;
	// Frame动画
	private AnimationDrawable animDance;
    String []chniese={"您好！有什么可以帮到您吗？","您有什么健康问题想了解吗？","您想了解什么疾病的内容？"};
    String []english={"Hello, what can I do for you?","Do you have any medical problems？","what kind of disease do you want to know？"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_index);
		
		initLayout();
        
		// 初始化识别对象
		mIat = SpeechRecognizer.createRecognizer(this, null);
		mSharedPreferences = getSharedPreferences("com.iflytek.setting",
				Activity.MODE_PRIVATE);
		mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

	}
	
	

	/**
	 * 初始化Layout。
	 */
	private void initLayout() {
		bt1 = (Button) findViewById(R.id.bnt_talk);
		// bt1.setOnClickListener(this);
        
		bt2 = (Button) findViewById(R.id.bnt_body);
		bt2.setOnClickListener(this);
		bt3 = (Button) findViewById(R.id.bnt_search);
		bt3.setOnClickListener(this);
		bt4 = (Button) findViewById(R.id.bnt_user);
		bt4.setOnClickListener(this);
		ll = (LinearLayout) findViewById(R.id.ll);
        int a=(int) (Math.random()*3);
		LayoutInflater iInflater = LayoutInflater.from(getApplicationContext());
		view = iInflater.inflate(R.layout.askandanswer, null);
		rll = (RelativeLayout) view.findViewById(R.id.RelativeLayout1);
		linear1 = (LinearLayout) view.findViewById(R.id.linear1);
		linear2 = (LinearLayout) view.findViewById(R.id.linear2);
		ll_help=(RelativeLayout) findViewById(R.id.ll_help);
		mResultText = (EditText) view.findViewById(R.id.textView1);
		textView1=(TextView) findViewById(R.id.textView1);
		textView2=(TextView) findViewById(R.id.textView2);
        int i= (int) (Math.random()*2);
        textView1.setText(chniese[i]);
        textView2.setText(english[i]);

		mResultText.setImeOptions(EditorInfo.IME_ACTION_SEARCH);  
		mResultText.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					// 此处为得到焦点时的处理内容

				} else {
					// 此处为失去焦点时的处理内容
					if (mResultText.getEditableText() != null
							&& !"".equals(mResultText.getEditableText()
									.toString())) {
						GetKeywordsByVoice(mResultText.getEditableText()
								.toString());
					}
				}
			}
		});
		mResultText.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				mResultText.setFocusable(true);
				mResultText.setBackgroundColor(Color.WHITE);
				mResultText.setFocusableInTouchMode(true);
				return false;
			}
		});
		show = (RelativeLayout) view.findViewById(R.id.show);
		img1 = (ImageView) view.findViewById(R.id.imageView1);
		img7 = (ImageView) view.findViewById(R.id.imageView7);
		txt2 = (TextView) view.findViewById(R.id.textView2);
		mTextView = (TextView) view.findViewById(R.id.textView3);
		ll.addView(view);
		bt1.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				rll.setVisibility(View.VISIBLE);
				mIatResults.clear();
				mResultText.setText("");
				showhide(0);
				// 设置参数
				setParam();
				mTextView.setText("");
				img7.setVisibility(View.GONE);
				ret = mIat.startListening(recognizerListener);
				if (ret != ErrorCode.SUCCESS) {
					showTip("听写失败,错误码：" + ret);
					//finishWork();
				} else {
					showTip(getString(R.string.text_begin));
				}
				return false;
			}
		});
		bt1.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {

				case MotionEvent.ACTION_UP:
					mIat.stopListening();
					showTip("停止听写");
					break;

				}
				return false;
			}

		});

	}

	int ret = 0; // 函数调用返回值

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		mResultText.setFocusable(false);
		mResultText.setBackgroundColor(Color.TRANSPARENT);
		mResultText.setFocusableInTouchMode(false);
		// 关闭软键盘
		View view = getWindow().peekDecorView();
		if (view != null) {
			InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
		return false;
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.bnt_body:

			SpeechApp.index=0;
			SpeechApp.v.setCurrentItem(SpeechApp.index);
			break;
		case R.id.bnt_search:
			SpeechApp.index=2;
			SpeechApp.v.setCurrentItem(SpeechApp.index);
			break;

		case R.id.bnt_user:
			if(loginname==""){
				Intent intent3=new Intent();
				intent3.setClass(getApplicationContext(), LoginActivity.class);
				startActivity(intent3);
			}else {
				Intent intent2=new Intent();
				intent2.setClass(getApplicationContext(), MyCenterActivity.class);
				startActivity(intent2);
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 听写监听器。
	 */
	private RecognizerListener recognizerListener = new RecognizerListener() {

		@Override
		public void onBeginOfSpeech() {
			showTip("开始说话");

			animDance = (AnimationDrawable) img1.getBackground();
			img1.setVisibility(View.VISIBLE);
			txt2.setVisibility(View.VISIBLE);
			ll_help.setVisibility(View.GONE);
			txt2.setText("录入中...");
			txt2.setVisibility(View.VISIBLE);
			animDance.start();
		}

		public void onError(SpeechError error) {
			// Tips：
			// 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
			// 如果使用本地功能（语音+）需要提示用户开启语音+的录音权限。
			showTip(error.getPlainDescription(true));
			//finishWork();
			showhide(1);
			mResultText.setText("你没有说任何话");
			img7.setVisibility(View.VISIBLE);

		}

		@Override
		public void onEndOfSpeech() {
			showTip("结束说话");
			b = true;
		}

		@Override
		public void onResult(RecognizerResult results, boolean isLast) {

			printResult(results);
			if (isLast) {
				//finishWork();
				//showhide(1);
				String resulet = mResultText
						.getText()
						.toString()
						.substring(0,
								mResultText.getText().toString().length() - 1);
				txt2.setText("搜索中...");
				GetKeywordsByVoice(resulet);
				img7.setVisibility(View.VISIBLE);

			}
		}

		@Override
		public void onVolumeChanged(int volume) {
			showTip("当前正在说话，音量大小：" + volume);

			b = false;

		}

		@Override
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
		}

	};

	int i;
	/*private SpannableStringBuilder addClickablePart(final String str) {

		SpannableStringBuilder ssb = new SpannableStringBuilder();
		ssb.append(str);

		final String[] likeUsers = str.split(",");

		if (likeUsers.length > 0) {
			// 最后一个
			for (i = 0; i < likeUsers.length; i++) {
				final String name = likeUsers[i];
				final int start = str.indexOf(name);
				ssb.setSpan(new ClickableSpan() {

					@Override
					public void onClick(View widget) {
						//SearchVideoByKeywords(name);
						SearchVideoByKeywords(str);

					}

					@Override
					public void updateDrawState(TextPaint ds) {
						super.updateDrawState(ds);
						ds.setColor(Color.RED); // 设置文本颜色
						// 去掉下划线
						ds.setUnderlineText(false);
					}

				}, start, start + name.length(),

				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return ssb.append("等");
	}*/

	/*private void finishWork() {
		animDance.stop();
		img1.setVisibility(View.GONE);
		txt2.setVisibility(View.GONE);
	}*/

	private void showhide(int i) {
		if (i == 0) {
			linear1.setVisibility(View.GONE);
			linear2.setVisibility(View.VISIBLE);
			mResultText.setVisibility(View.GONE);
			show.setVisibility(View.VISIBLE);
		} else {
			animDance.stop();
			linear1.setVisibility(View.VISIBLE);
			linear2.setVisibility(View.GONE);
			mResultText.setVisibility(View.VISIBLE);
			show.setVisibility(View.GONE);
		}
	}

	private void printResult(RecognizerResult results) {
		String text = JsonParser.parseIatResult(results.getResultString());

		String sn = null;
		// 读取json结果中的sn字段
		try {
			JSONObject resultJson = new JSONObject(results.getResultString());
			sn = resultJson.optString("sn");
		} catch (JSONException e) {
			e.printStackTrace();
		}

		mIatResults.put(sn, text);

		StringBuffer resultBuffer = new StringBuffer();
		for (String key : mIatResults.keySet()) {
			resultBuffer.append(mIatResults.get(key));
		}

		mResultText.setText(resultBuffer.toString());
		mResultText.setSelection(mResultText.length());
	}

	private void showTip(final String str) {
		mToast.setText(str);
		mToast.show();
	}

	/**
	 * 参数设置
	 * 
	 * @param param
	 * @return
	 */
	public void setParam() {
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);

		// 设置听写引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		String lag = mSharedPreferences.getString("iat_language_preference",
				"mandarin");
		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		} else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT, lag);
		}
		// 设置语音前端点
		mIat.setParameter(SpeechConstant.VAD_BOS,
				mSharedPreferences.getString("iat_vadbos_preference", "4000"));
		// 设置语音后端点
		mIat.setParameter(SpeechConstant.VAD_EOS,
				mSharedPreferences.getString("iat_vadeos_preference", "1000"));
		// 设置标点符号
		mIat.setParameter(SpeechConstant.ASR_PTT,
				mSharedPreferences.getString("iat_punc_preference", "1"));
		// 设置音频保存路径
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH,
				Environment.getExternalStorageDirectory()

				+ "/iflytek/wavaudio.pcm");

		// 设置听写结果是否结果动态修正，为“1”则在听写过程中动态递增地返回结果，否则只在听写结束之后返回最终结果
		// 注：该参数暂时只对在线听写有效
		mIat.setParameter(SpeechConstant.ASR_DWA,
				mSharedPreferences.getString("iat_dwa_preference", "1"));

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 退出时释放连接
		mIat.cancel();
		mIat.destroy();
	}

	
	protected void onResume() {
		// 移动数据统计分析
		FlowerCollector.onResume(IndexActivity.this);
		FlowerCollector.onPageStart(TAG);
		sp = getSharedPreferences("userinfo", MODE_PRIVATE);
		loginname = sp.getString("loginname", "");
		super.onResume();
	}

	@Override
	protected void onPause() {
		// 移动数据统计分析
		FlowerCollector.onPageEnd(TAG);
		FlowerCollector.onPause(IndexActivity.this);
		super.onPause();
	}

	// 关键字获取
	public void GetKeywordsByVoice(String s) {
		FinalHttp http = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("VoiceText", s);
		http.post(Path.getPath()
				+ "WS_GetKeywordsByVoiceText.asmx/GetKeywordsByVoiceText",
				params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				JSONObject joResult;
				likeUsers = "";
				try {
					joResult = new JSONObject(XmlParser.xmltojson(t));
					if (joResult.getString("Error").equals("true")) {
						
						mTextView.setText("没有相关结果");
						 showhide(1);
					} else {
						Gson g = new Gson();
						List<vkKeyword> l = g.fromJson(
								joResult.getString("Data"),
								new TypeToken<List<vkKeyword>>() {
								}.getType());
						for (vkKeyword vk : l) {
							likeUsers += vk.getVkKeyword() + ",";
						}
						likeUsers = likeUsers.substring(0,
								likeUsers.lastIndexOf(","));
						mTextView.setMovementMethod(LinkMovementMethod
								.getInstance());
						//先构造SpannableString
						SpannableStringBuilder spanString1 = new SpannableStringBuilder("您要问的是:"+likeUsers+"么？");
						//SpannableStringBuilder spanString2 = new SpannableStringBuilder(likeUsers);
						//SpannableStringBuilder spanString3 = new SpannableStringBuilder("么?");
				       //再构造一个改变字体颜色的Span
				        /*ForegroundColorSpan span = new ForegroundColorSpan(); 
				        Color c= new Color(168,0,30);*/
				        //将这个Span应用于指定范围的字体
						//Toast.makeText(IndexActivity.this, likeUsers+"length="+likeUsers.length(), 0).show();
				        spanString1.setSpan(new ClickableSpan() {
							  
							  @Override public void onClick(View widget) {
							            SearchVideoByKeywords(likeUsers);
							  }
							  @Override public void updateDrawState(TextPaint ds) {
							  super.updateDrawState(ds); ds.setColor(0xffa8001e); // 设置文本颜色
							  //  下划线 
							   ds.setUnderlineText(true); }
							  
							  }, 6, 6+likeUsers.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE); 
				        //spanString.append("等");
				        
				        //spanString1.append(spanString2);
						//Toast.makeText(IndexActivity.this, "spanString2="+spanString2, 0).show();

				         //  Toast.makeText(this, spanString2.g, 0).show();

				        //spanString1.append(spanString3);
				        
				           mTextView.setText(spanString1);
				           //Toast.makeText(this, spanString1, 0).show();
				           showhide(1);

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				mTextView.setText(strMsg);
				
			}

		});
	}

	// 关键查询视屏
	public void SearchVideoByKeywords(String keyword) {
		FinalHttp http = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("Keyword", keyword);
		http.post(Path.getPath()
				+ "WS_SearchVideoByKeywords.asmx/SearchVideoByKeywords",
				params, new AjaxCallBack<String>() {
			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);
				JSONObject joResult;
				try {
					joResult = new JSONObject(XmlParser.xmltojson(t));
					if (joResult.getString("Error").equals("true")) {
						Toast.makeText(IndexActivity.this, "没有相应的视频", Toast.LENGTH_SHORT)
						.show();
					} else {
						String DataJson = joResult.getString("Data");
						Intent i = new Intent(IndexActivity.this,
								VideoActivity.class);
						i.putExtra("videojson", DataJson);
						startActivity(i);
					}

				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				Toast.makeText(IndexActivity.this, strMsg,
						Toast.LENGTH_SHORT).show();

			}

		});

	}

}
