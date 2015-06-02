package com.zhangdong.JiShi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangdong.JiShi.MainActivity.MyOnPageChangeListener;
import com.zhangdong.JiShi.Path.Path;
import com.zhangdong.JiShi.Tools.MyListInSearchAdapter;
import com.zhangdong.JiShi.Tools.SpeechApp;
import com.zhangdong.JiShi.Tools.XmlParser;
import com.zhangdong.util.Keywords;
import com.zhangdong.util.SqliteMethod;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import android.widget.Toast;

public class SearchActivity extends FinalActivity {
	MyOnPageChangeListener myOnPageChangeListener;
	EditText inpuText;
	ListView listView;

	MyListInSearchAdapter myListInSearchAdapter, myListInSearchAdapter2;
	SqliteMethod sqlm;
	@ViewInject(id = R.id.bnt_quxiaoInSearch, click = "bnt_quxiaoInSearch")
	Button bnt_quxiaoInSearch;
	List<Keywords> historyKeyWord = new ArrayList<Keywords>();

	public void bnt_quxiaoInSearch(View v) {
		inpuText.setText("");
	}

	@ViewInject(id = R.id.bnt_back, click = "bnt_back_click")
	Button bnt_back;

	public void bnt_back_click(View v) {
		SpeechApp.index = 1;
		SpeechApp.v.setCurrentItem(SpeechApp.index);
	}

	@ViewInject(id = R.id.bnt_clearsearch, click = "bnt_clear_click")
	Button bnt_clear;

	public void bnt_clear_click(View v) {
		sqlm.deleteAll();
		historyKeyWord.clear();
		myListInSearchAdapter2.setDate(historyKeyWord);
		myListInSearchAdapter2.notifyDataSetChanged();

	}

	@ViewInject(id = R.id.bnt_searchInSearch, click = "bnt_searchInSearch")
	Button bnt_searchInSearch;
	private LinearLayout llpb;

	public void bnt_searchInSearch(View v) {
		if (!"".equals(inpuText.getText().toString())) {
			GetKeywords(inpuText.getText().toString());
		} else {
			myListInSearchAdapter2.notifyDataSetChanged();
			Toast.makeText(SearchActivity.this, "输入内容", 0).show();
		}

	}

	private void initListView() {
		listView = (ListView) findViewById(R.id.lv_listviewInSearch);
		llpb = (LinearLayout) findViewById(R.id.llpb);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				Keywords keywords = (Keywords) parent
						.getItemAtPosition(position);
				Cursor cursor = sqlm.getBykeywords(keywords.getKeyword());
				if (!cursor.moveToNext()) {
					sqlm.insert(keywords.getKeyword());
				}
				SearchVideoByKeywords(keywords.getKeyword());

			}
		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		inpuText = (EditText) findViewById(R.id.et_inputInSearch);
		sqlm = new SqliteMethod(SearchActivity.this);
		initListView();

	}

	// 查询关键字
	public void GetKeywords(String input) {
		FinalHttp http = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("InputKey", input);
		http.post(Path.getPath() + "WS_GetKeywords.asmx/GetKeywords", params,
				new AjaxCallBack<String>() {

					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						showeandhide(1);
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						JSONObject joResult;
						showeandhide(0);
						try {
							joResult = new JSONObject(XmlParser.xmltojson(t));
							if (joResult.getString("Error").equals("true")) {
								Toast.makeText(SearchActivity.this, "没有相关结果",
										Toast.LENGTH_SHORT).show();
							} else {
								Gson g = new Gson();
								List<Keywords> l = g.fromJson(
										joResult.getString("Data"),
										new TypeToken<List<Keywords>>() {
										}.getType());
								myListInSearchAdapter = new MyListInSearchAdapter(
										SearchActivity.this, l);
								listView.setAdapter(myListInSearchAdapter);

							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						Toast.makeText(SearchActivity.this, "检查网络",
								Toast.LENGTH_SHORT).show();
						showeandhide(0);
					}

				});

	}

	// @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (sqlm != null) {
			Cursor cursor = sqlm.getAll();
			historyKeyWord.clear();
			while (cursor.moveToNext()) {
				Log.v("tag", "value");
				Keywords k = new Keywords();
				k.setKeyword(cursor.getString(1));
				historyKeyWord.add(k);
			}
			if ("".equals(inpuText.getText().toString())) {
				myListInSearchAdapter2 = new MyListInSearchAdapter(
						SearchActivity.this, historyKeyWord);
				listView.setAdapter(myListInSearchAdapter2);
			}
		}
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
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						showeandhide(1);
					}

					@Override
					public void onSuccess(String t) {
						super.onSuccess(t);
						JSONObject joResult;
						showeandhide(0);
						try {
							joResult = new JSONObject(XmlParser.xmltojson(t));
							if (joResult.getString("Error").equals("true")) {
								Toast.makeText(SearchActivity.this, "没有相应的视频",
										1).show();
							} else {
								String DataJson = joResult.getString("Data");
								Intent i = new Intent(SearchActivity.this,
										VideoActivity.class);
								i.putExtra("videojson", DataJson);
								startActivity(i);
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onFailure(Throwable t, String strMsg) {
						super.onFailure(t, strMsg);
						Toast.makeText(SearchActivity.this, "检查网络",
								Toast.LENGTH_SHORT).show();
						showeandhide(0);

					}

				});

	}

	// 如果0隐藏，1显示loading
	public void showeandhide(int i) {

		if (i == 0) {
			bnt_clear.setVisibility(View.VISIBLE);
			listView.setVisibility(View.VISIBLE);
			llpb.setVisibility(View.GONE);
		} else {
			bnt_clear.setVisibility(View.GONE);
			listView.setVisibility(View.GONE);
			llpb.setVisibility(View.VISIBLE);
		}

	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(getApplicationContext(), MainActivity.class);
			startActivity(intent);
			return false;
		}

		return super.onKeyDown(keyCode, event);
	}

}
