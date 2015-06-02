package com.zhangdong.JiShi;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.annotation.view.ViewInject;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.zhangdong.JiShi.Bean.BodyPart;
import com.zhangdong.JiShi.MainActivity.MyOnPageChangeListener;
import com.zhangdong.JiShi.Path.Path;
import com.zhangdong.JiShi.Tools.MyGridViewAdapter;
import com.zhangdong.JiShi.Tools.XmlAndJson;
import com.zhangdong.JiShi.Tools.XmlParser;

import android.os.Bundle;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class GridViewActivity extends FinalActivity {
	MyOnPageChangeListener myOnPageChangeListener;
	private FinalHttp http;
	@ViewInject(id = R.id.gv_bodypart)
	GridView gv_body;
	@ViewInject(id = R.id.tv_bodypart)
	TextView tv_bodypart;
	private MyGridViewAdapter mGridViewAdapter = null;
	private String bpid;
	private String bodypartname;
	private String datajson = "";
	private List<BodyPart> ll = null;
	View progressView;
	@ViewInject(id = R.id.bnt_back, click = "bnt_back_click")
	Button bnt_back;

	public void bnt_back_click(View v) {
		GridViewActivity.this.finish();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_gridview);
		http = new FinalHttp();
		Intent intent = this.getIntent();
		bpid = String.valueOf(intent.getIntExtra("bodyid", 0));
		bodypartname = intent.getStringExtra("bodypart");
		tv_bodypart.setText(bodypartname);
		progressView = findViewById(R.id.progressbar);
		initGridView();

	}

	private void initGridView() {
		gv_body.setSelector(new ColorDrawable(Color.TRANSPARENT));
		String httpUrl = Path.getPath()
				+ "WS_GetVideoCategoryByBodyParts.asmx/GetVideoCategoryByBodyParts";
		AjaxParams params = new AjaxParams();
		params.put("bpid", bpid);
		http.post(httpUrl, params, new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
				Toast.makeText(getApplicationContext(), "没有网络",
						Toast.LENGTH_SHORT).show();
				progressView.setVisibility(View.GONE);

			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
				progressView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSuccess(String t) {
				super.onSuccess(t);

				String jsonString = XmlAndJson.cc(t);
				try {
					JSONObject jsonObject1 = new JSONObject(jsonString);
					datajson = jsonObject1.getString("Data");
					ll = new ArrayList<BodyPart>();
					JSONArray jsonArray = new JSONArray(datajson);
					for (int i = 0; i < jsonArray.length(); i++) {
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						String VCID = jsonObject.getString("VCID");
						String vcName = jsonObject.getString("vcName");
						String vcImageURL = jsonObject.getString("vcImageURL");
						BodyPart bPart = new BodyPart(VCID, vcName, vcImageURL);
						ll.add(bPart);
					}
					mGridViewAdapter = new MyGridViewAdapter(
							getApplicationContext(), ll);
					gv_body.setAdapter(mGridViewAdapter);
					GridViewClickListenr gridViewClickListenr = new GridViewClickListenr();
					gv_body.setOnItemClickListener(gridViewClickListenr);
					progressView.setVisibility(View.GONE);
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});
	}

	class GridViewClickListenr implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			mGridViewAdapter.setSeclection(arg2);
			mGridViewAdapter.notifyDataSetChanged();
			String VCID = ll.get(arg2).getVCID();
			FinalHttp http = new FinalHttp();
			AjaxParams params = new AjaxParams();
			params.put("vcid", VCID);
			http.post(Path.getPath()
					+ "WS_SearchVideoByCategory.asmx/SearchVideoByCategory",
					params, new AjaxCallBack<String>() {

						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							super.onStart();
							progressView.setVisibility(View.VISIBLE);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							JSONObject joResult;
							try {
								joResult = new JSONObject(XmlParser
										.xmltojson(t));
								if (joResult.getString("Error").equals("true")) {
									Toast.makeText(getApplicationContext(),
											"没有相应的视频", Toast.LENGTH_SHORT)
											.show();
								} else {
									String DataJson = joResult
											.getString("Data");
									Intent i = new Intent(
											getApplicationContext(),
											VideoActivity.class);
									i.putExtra("videojson", DataJson);
									startActivity(i);
								}
								progressView.setVisibility(View.GONE);

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						}

						@Override
						public void onFailure(Throwable t, String strMsg) {
							super.onFailure(t, strMsg);
							Toast.makeText(getApplicationContext(), "没有网络",
									Toast.LENGTH_SHORT).show();
							progressView.setVisibility(View.GONE);

						}

					});

		}

	}
}
