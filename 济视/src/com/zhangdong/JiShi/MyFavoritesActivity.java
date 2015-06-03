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

import com.zhangdong.JiShi.Bean.MyFavorites;
import com.zhangdong.JiShi.Path.Path;
import com.zhangdong.JiShi.Tools.MyListInFavoritesAdapter;
import com.zhangdong.JiShi.Tools.MyListInFavoritesAdapter.onRightItemClickListener;
import com.zhangdong.JiShi.Tools.SwipeListView;
import com.zhangdong.JiShi.Tools.XmlAndJson;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MyFavoritesActivity extends FinalActivity implements OnPreparedListener{
	Toast toast;
	private SharedPreferences sp;
	private String loginname;
	private FinalHttp http;
	private MyListInFavoritesAdapter myListInFavoritesAdapter;
	List<MyFavorites> ll=null;
	private String datajson="";
	@ViewInject(id =R.id.tv_nodata) TextView tv_nodata;
	@ViewInject(id =R.id.lv_myfavorites) SwipeListView lv_myfavorites;
	@ViewInject(id =R.id.progressbar) View progressView;
	@ViewInject(id=R.id.rl_clear,click="rl_clear_click") RelativeLayout rl_clear;
	public void rl_clear_click(View v) {
		if(ll.size()==0){
			if(toast == null)
				toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
			toast.setText("无数据");
			toast.show();
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
					params.put("Type", "1");
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
								
								if(toast == null)
									toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
								toast.setText(ErrorDesc);
								toast.show();
								lv_myfavorites.setAdapter(null);
								myListInFavoritesAdapter.notifyDataSetChanged();


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

	@ViewInject(id=R.id.bnt_edit,click="bnt_edit_click") Button bnt_edit;
	public void bnt_edit_click(View v){

	}

	@ViewInject(id=R.id.bnt_back,click="bnt_back_click") Button bnt_back;
	public void bnt_back_click(View v) {
		MyFavoritesActivity.this.finish();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_myfavorites);
		sp=this.getSharedPreferences("userinfo",MODE_PRIVATE);
		if(!sp.getString("openid", "").equals("")){
			loginname=sp.getString("openid", "");
		}else {
			loginname=sp.getString("loginname", "");
		}
		http=new FinalHttp();
		//initListView();

	}



	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(ll!=null&&ll.size()>0){
			ll.clear();
		}
		initListView();


	}

	private void initListView(){
		String httpUrl=Path.getPath()+"WS_GetMyFavorites.asmx/GetMyFavorites";
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
						ll=new ArrayList<MyFavorites>();
						JSONArray jsonArray = new JSONArray(datajson);
						for(int i = 0; i < jsonArray.length(); i++) {
							JSONObject jsonObject = jsonArray.getJSONObject(i);
							String VID=jsonObject.getString("VID");
							String UFID=jsonObject.getString("UFID");
							String vTitle=jsonObject.getString("vTitle");
							String vURL = jsonObject.getString("vURL");
							String vPreviewImageURL = jsonObject.getString("vPreviewImageURL");
							String vDoctorName = jsonObject.getString("vDoctorName");
							String vDoctorDepartment = jsonObject.getString("vDoctorDepartment");
							String vDoctorHospital = jsonObject.getString("vDoctorHospital");
							MyFavorites myFavorites = new MyFavorites(vPreviewImageURL,vTitle,vURL, VID,UFID,vDoctorName,vDoctorDepartment,vDoctorHospital);
							ll.add(myFavorites);
						}
						if(ll.size()>0)		
						{
							myListInFavoritesAdapter=new MyListInFavoritesAdapter(getApplicationContext(),ll,lv_myfavorites.getRightViewWidth());
							lv_myfavorites.setAdapter(myListInFavoritesAdapter);
							myListInFavoritesAdapter.notifyDataSetChanged();

							ListViewClickListenr listViewClickListenr=new ListViewClickListenr();
							lv_myfavorites.setOnItemClickListener(listViewClickListenr);

							ListViewOnRightItemClickListener listViewOnRightItemClickListener=new ListViewOnRightItemClickListener();
							myListInFavoritesAdapter.setOnRightItemClickListener(listViewOnRightItemClickListener);
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
			String VID = ll.get(arg2).getVID();
			String UFID=ll.get(arg2).getUFID();
			String vTitle=ll.get(arg2).getvTitle();
			String vURL=ll.get(arg2).getvURL();
			Intent intent=new Intent();	
			intent.putExtra("VID", VID);
			intent.putExtra("UHID", UFID);
			intent.putExtra("vTitle", vTitle);
			intent.putExtra("vURL", vURL);
			intent.setClass(getApplicationContext(), VideoMainActivity.class);
			startActivity(intent);
		}
	}

	class ListViewOnRightItemClickListener implements onRightItemClickListener{

		@Override
		public void onRightItemClick(View v, int position) {


			String VID = ll.get(position).getVID();
			String httpUrl=Path.getPath()+"WS_UpdateMyFavorites.asmx/UpdateMyFavorites";
			AjaxParams params=new AjaxParams();
			params.put("uLoginID", loginname);
			params.put("VID", VID);
			params.put("UFID", "0");
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
						if(toast == null)
							toast = Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT);
						toast.setText(ErrorDesc);
						toast.show();
					}
				}

			});
			lv_myfavorites.deleteItem(lv_myfavorites.getChildAt(position));
			ll.remove(position);
			if(ll.size()<1){
				tv_nodata.setVisibility(View.VISIBLE);
			}
			myListInFavoritesAdapter.notifyDataSetChanged();


		}

	}

	@Override
	public void onPrepared(MediaPlayer arg0) {
		// TODO Auto-generated method stub
	}


}
