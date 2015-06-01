package com.zhangdong.JiShi.Fragment;

import java.util.List;

import net.tsz.afinal.FinalBitmap;

import com.zhangdong.JiShi.R;
import com.zhangdong.JiShi.Fragment.Frag1.Listadapte;
import com.zhangdong.JiShi.Tools.NoScrollListView;
import com.zhangdong.util.Video;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Frag4 extends Fragment {

	Activity activity;
	private View view1;
	LinearLayout lll;
	NoScrollListView list;
	LayoutInflater inflater;
	List<Video> listVideo;
	TextView title, vDoctorName, vDoctorDepartment, vDoctorHospital;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		this.activity = activity;
		super.onAttach(activity);
	}

	public void getListVideo(List<Video> listVideo) {
		this.listVideo = listVideo;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.inflater = inflater;
		view1 = inflater.inflate(R.layout.frag4, container, false);
		list = (NoScrollListView) view1.findViewById(R.id.list);
		// lll = (LinearLayout) view1.findViewById(R.id.viko);
		Listadapte adapter = new Listadapte();
		list.setAdapter(adapter);
		/*
		 * for (int i = 0; i < l.size(); i++) { // LayoutInflater iInflater2 =
		 * LayoutInflater.from(activity); view2 =
		 * inflater.inflate(R.layout.title, container, false); // img =
		 * (ImageView) view2.findViewById(R.id.viewpic); describle = (TextView)
		 * view2.findViewById(R.id.describle); tag = (TextView)
		 * view2.findViewById(R.id.tag);
		 * describle.setText(l.get(i).get("name").toString());
		 * tag.setText(l.get(i).get("tag").toString()); lll.addView(view2); }
		 */

		// return inflater.inflate(R.layout.frag1, container, false);
		return view1;
	}

	class Listadapte extends BaseAdapter {
		FinalBitmap fb = FinalBitmap.create(activity);

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return listVideo.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return listVideo.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			FinalBitmap fb = FinalBitmap.create(activity);
			Video v = listVideo.get(position);
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.title, null);
			}
			ImageView img = (ImageView) convertView.findViewById(R.id.viewpic);
			if (v.getvPreviewImageURL() != null
					&& !"".equals(v.getvPreviewImageURL())) {
				fb.display(img, v.getvPreviewImageURL());
			}
			title = (TextView) convertView.findViewById(R.id.vTitle);
			title.setText(v.getvTitle().toString());
			vDoctorName = (TextView) convertView.findViewById(R.id.vDoctorName);
			vDoctorName.setText(v.getvDoctorName().toString());
			vDoctorDepartment = (TextView) convertView
					.findViewById(R.id.vDoctorDepartment);
			vDoctorDepartment.setText(v.getvDoctorDepartment().toString());
			vDoctorHospital = (TextView) convertView
					.findViewById(R.id.vDoctorHospital);
			vDoctorHospital.setText(v.getvDoctorHospital().toString());
			return convertView;
		}

	}

}
