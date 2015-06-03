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

import com.zhangdong.JiShi.Bean.MyHistory;
import com.zhangdong.JiShi.Path.Path;
import com.zhangdong.JiShi.Tools.MyListInHistoryAdapter;
import com.zhangdong.JiShi.Tools.MyListInHistoryAdapter.onRightItemClickListener;
import com.zhangdong.JiShi.Tools.SwipeListView;
import com.zhangdong.JiShi.Tools.XmlAndJson;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SeeHistoryActivity extends FinalActivity {
	private SharedPreferences sp;
	private String loginname;
	private Editor editor;
	private FinalHttp http;
	private String datajson="";
	@ViewInject(id =R.id.tv_nodata) TextView tv_nodata;
	@ViewInject(id =R.id.progressbar) View progressView;
	@ViewInject(id=R.id.lv_history) SwipeListView lv_history;
	private MyListInHistoryAdapter myListInHistoryAdapter=null;
	private List<MyHistory> ll;



	@ViewInject(id=R.id.rl_clear,click="rl_clear_click") RelativeLayout rl_clear;
	public void rl_clear_click(View v) {
		if(ll.size()==0)
		{
			Toast.makeText(getApplicationContext(), "无数据", Toast.LENGTH_SHORT).show();
		}else {
			new AlertDialog.Builder(this).setTitle("是否清空？")  
			.setIcon(android.R.drawable.ic_dialog_info)  
			.setPositiveButton("确定", new DialogInterface.OnClickListener() {  
				@Override  
				public void onClick(DialogInterface dialog, int which) {  
					// 点击“确认”后的操作  
					String httpUrl=Path.getPath()+"WS_RemoveAllMyRecoredsByLoginID.asmx/RemoveAllMyRecoredsByLoginID";
					AjaxParams params=new AjaxParams();
					params.put("uLoginID", loginname);
					params.put("Type", "2");
					http.post(httpUrl, params,new AjaxCallBack<String>() {

						@Override
						public void onFailure(Throwable t, String strMsg) {
							super.onFailure(t, strMsg);
						}

						@Override
						public void onSuccess(String t) {
							super.onSuccess(t);
							String Error="";
							String ErrorDesc="";
							try {
								String jsonString=XmlAndJson.cc(t);
								if(jsonString!=null){
									JSONObject jsonObject = new JSONObject(jsonString);
									Error = jsonObject.getString("Error");
									ErrorDesc=jsonObject.getString("ErrorDesc");
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
							if(Error.equals("false")){
								tv_nodata.setVisibility(View.VISIBLE);
								Toast.makeText(getApplicationContext(), ErrorDesc, Toast.LENGTH_SHORT).show();
								lv_history.setAdapter(null);
								myListInHistoryAdapter.notifyDataSetChanged();

							}
						}

					});
				}  
			})  
			.setNegativeButton("取消", new DialogInterface.OnClickListener() {  

				@Override  
				public void onClick(DialogInterface dialog, int which) {  
					// 点击“返回”后的操作,这里不设置没有任何操作  
				}  
			}).show();
		}

	}

	@ViewInject(id=R.id.bnt_back,click="bnt_back_click") Button bnt_back;
	public void bnt_back_click(View v) {
		SeeHistoryActivity.this.finish();
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_seehistory);
		sp=this.getSharedPreferences("userinfo",MODE_PRIVATE);
		editor = sp.edit();
		if(!sp.getString("openid", "").equals("")){
			loginname=sp.getString("openid", "");
		}else {
			loginname=sp.getString("loginname", "");
		}

		http=new FinalHttp();

		initListView();

	}



	private void initListView(){

		String httpUrl=Path.getPath()+"WS_GetMyHistory.asmx/GetMyHistory";
		AjaxParams params=new AjaxParams();
		params.put("uloginid", loginname);
		http.post(httpUrl, params,new AjaxCallBack<String>() {

			@Override
			public void onFailure(Throwable t, String strMsg) {
				super.onFailure(t, strMsg);
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
				String jsonString=XmlAndJson.cc(t);
				try {
					JSONObject jsonObject1 = new JSONObject(jsonString);

					if(jsonObject1.getString("ErrorDesc")!=null && !jsonObject1.getString("ErrorDesc").equals("") ){
						tv_nodata.setVisibility(View.VISIBLE);
					}else {
						datajson  = jsonObject1.getString("Data");
						ll=new ArrayList<MyHistory>();
						JSONArray jsonArray = new JSONArray(datajson);
						for(int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							//						String vPreviewImageURL = jsonObject.getString("vPreviewImageURL");
							String vTitle=jsonObject.getString("vTitle");
							String vURL = jsonObject.getString("vURL");
							String VID=jsonObject.getString("VID");
							String UHID=jsonObject.getString("UHID");
							MyHistory myHistory = new MyHistory(vTitle,vURL, VID,UHID);
							ll.add(myHistory);
						}
						
						if(ll.size()>0)		
						{			
							myListInHistoryAdapter=new MyListInHistoryAdapter(getApplicationContext(),ll,lv_history.getRightViewWidth());
							lv_history.setAdapter(myListInHistoryAdapter);
							
							ListViewClickListenr listViewClickListenr=new ListViewClickListenr();
							lv_history.setOnItemClickListener(listViewClickListenr);
							
							ListViewOnRightItemClickListener listViewOnRightItemClickListener=new ListViewOnRightItemClickListener();
							myListInHistoryAdapter.setOnRightItemClickListener(listViewOnRightItemClickListener);
						}
						
						
					}
					progressView.setVisibility(View.GONE);
				}catch (JSONException e) {
					e.printStackTrace();
				}


			}

		});
	}


	class ListViewClickListenr implements OnItemClickListener{


		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			String UHID=ll.get(arg2).getUHID();	
			String vTitle=ll.get(arg2).getvTitle();
			String vURL = ll.get(arg2).getvURL();
			String VID=ll.get(arg2).getVID();
			Intent intent=new Intent();
			intent.putExtra("loginname", loginname);
			intent.putExtra("UHID", UHID);
			intent.putExtra("VID", VID);
			intent.putExtra("vTitle", vTitle);
			intent.putExtra("vURL", vURL);
			intent.setClass(getApplicationContext(), VideoMainActivity.class);
			startActivity(intent);
		}

	}

	class ListViewOnRightItemClickListener implements onRightItemClickListener{

		@Override
		public void onRightItemClick(View v, int position) {

			String UHID = ll.get(position).getUHID();
			String httpUrl=Path.getPath()+"WS_UpdateMyHistory.asmx/UpdateMyHistory";
			AjaxParams params=new AjaxParams();
			params.put("uLoginID", loginname);
			params.put("VID", "0");
			params.put("UHID", UHID);
			params.put("Type", "2");
			params.put("UID", "0");
			http.post(httpUrl, params,new AjaxCallBack<String>() {
				@Override
				public void onFailure(Throwable t, String strMsg) {
					super.onFailure(t, strMsg);
				}

				@Override
				public void onSuccess(String t) {
					super.onSuccess(t);
					String Error="";
					String ErrorDesc="";
					try {
						String jsonString=XmlAndJson.cc(t);
						if(jsonString!=null){
							JSONObject jsonObject = new JSONObject(jsonString);
							Error = jsonObject.getString("Error");
							ErrorDesc=jsonObject.getString("ErrorDesc");
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
					if(Error.equals("false")){
						Toast.makeText(getApplicationContext(), ErrorDesc, Toast.LENGTH_SHORT).show();

					}
				}

			});
			lv_history.deleteItem(lv_history.getChildAt(position));
			ll.remove(position);
			if(ll.size()<1){
				tv_nodata.setVisibility(View.VISIBLE);
			}
			myListInHistoryAdapter.notifyDataSetChanged();



		}

	}

}
