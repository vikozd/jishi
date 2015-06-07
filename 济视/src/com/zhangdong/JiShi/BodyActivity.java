package com.zhangdong.JiShi;


import java.util.Timer;
import java.util.TimerTask;

import com.zhangdong.JiShi.MainActivity.MyOnPageChangeListener;
import com.zhangdong.JiShi.Tools.SpeechApp;

import net.tsz.afinal.FinalActivity;

import net.tsz.afinal.annotation.view.ViewInject;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.annotation.SuppressLint;
import android.content.Intent;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.View.OnTouchListener;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BodyActivity extends FinalActivity {
	private Timer timer;
	MyOnPageChangeListener myOnPageChangeListener;
	ImageView imageView;
	private double x;
	private double y;
	PopupWindow popupWindow;
	TextView tv_bodypart;
	RelativeLayout relativeLayout;
	TextView tv_bodypart_pop;
	boolean[] array=new boolean[11];
	private TimerTask timerTask;

	@ViewInject(id=R.id.bnt_back,click="bnt_back_click") Button bnt_back;
	public void bnt_back_click(View v) {
		SpeechApp.index=1;
		SpeechApp.v.setCurrentItem(SpeechApp.index);
	}

	@ViewInject(id=R.id.bnt_quanshen,click="bnt_quanshen_click") Button bnt_quanshen;
	public void bnt_quanshen_click(View v) {
		final Intent intent=new Intent();
		intent.setClass(getApplicationContext(), GridViewActivity.class);
		intent.putExtra("bodyid", 12);
		intent.putExtra("bodypart","全身");
		startActivity(intent);
	}

	@SuppressLint("ShowToast")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_body_part);
		tv_bodypart=(TextView)findViewById(R.id.tv_bodypart);     
		initImage();

	}




	private void  initImage() {
		imageView=(ImageView) findViewById(R.id.imageView1);
		int w=View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int h=View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		imageView.measure(w, h);
		final double  width=imageView.getMeasuredWidth();
		final double height=imageView.getMeasuredHeight();
//		final double radius=0.0978*width;
		final double radius=0.07*width;

		final double headX=0.5*width;
		final double headY=0.08*height;	

		final double neckX=0.5*width;
		final double neckY=0.1454*height;

		final double shoulderX=0.3203*width;
		final double shoulderY=0.2036*height;

		final double chestX=0.5*width;
		final double chestY=0.2895*height;

		final double forearmX=0.7408*width;
		final double forearmY=0.3781*height;

		final double stomachX=0.5*width;
		final double stomachY=0.3892*height;

		final double handX=0.1736*width;
		final double handY=0.4834*height;

		final double pelvicX=0.5*width;
		final double pelvicY=0.5*height;

		final double legX=0.4230*width;
		final double legY=0.5831*height;

		final double kneeX=0.5648*width;
		final double kneeY=0.7299*height;

		final double footX=0.4572*width;
		final double footY=0.8878*height;


		imageView.setOnTouchListener(new OnTouchListener() {



			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {						
				case MotionEvent.ACTION_DOWN:

					x=event.getX();
					y= event.getY();
					if((x-headX)*(x-headX)+(y-headY)*(y-headY)<=radius*radius){
						tv_bodypart.setText(R.string.head);
						tv_bodypart.setX((float) (headX-radius*1.4));
						tv_bodypart.setY((float) (headY+radius*0.5));
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}

						timerTask = new TimerTask() {

							@Override
							public void run() {

								if (  !array[0]) {  
									array[0]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 

								} else { 
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 1);
									intent.putExtra("bodypart","头部");
									startActivity(intent); 
									array[0]=false;
									stopTimer();
								}
							}
						};
						timer=new Timer();

						timer.schedule(timerTask, 0);

					}else if ((x-neckX)*(x-neckX)+(y-neckY)*(y-neckY)<=radius*radius) {
						tv_bodypart.setX((float) (neckX-radius*1.4));
						tv_bodypart.setY((float) (neckY));
						tv_bodypart.setText(R.string.neck);
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}
						Toast.makeText(BodyActivity.this, "暂无数据", 0).show();
						/*timerTask = new TimerTask() {

							@Override
							public void run() {

								if (  !array[1]) {  
									array[1]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 
								} else { 
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 2);
									intent.putExtra("bodypart","颈部");
									startActivity(intent); 

									array[1]=false;
									stopTimer();
								}
							}
						};
						timer=new Timer();

						timer.schedule(timerTask, 0);*/

					}else if ((x-shoulderX)*(x-shoulderX)+(y-shoulderY)*(y-shoulderY)<=radius*radius) {
						tv_bodypart.setText(R.string.shoulder);
						tv_bodypart.setX((float) (shoulderX-radius*1.4));
						tv_bodypart.setY((float) (shoulderY));
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}
						Toast.makeText(BodyActivity.this, "暂无数据", 0).show();

						/*timerTask=new TimerTask() {

							@Override
							public void run() {

								if( !array[2]){

									array[2]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 
								}else{
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 3);
									intent.putExtra("bodypart","肩部");
									startActivity(intent); 
									array[2]=false;
									stopTimer();
								}  
							}
						};
						timer=new Timer();

						timer.schedule(timerTask, 0);*/

					}else if ((x-forearmX)*(x-forearmX)+(y-forearmY)*(y-forearmY)<=radius*radius) {
						tv_bodypart.setText(R.string.forearm);
						tv_bodypart.setX((float) (forearmX-radius*1.4));
						tv_bodypart.setY((float) (forearmY));
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}
						Toast.makeText(BodyActivity.this, "暂无数据", 0).show();

						/*timerTask=new TimerTask() {

							@Override
							public void run() {

								if(!array[3]){
									array[3]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 
								}else{ 
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 4);
									intent.putExtra("bodypart","手臂");
									startActivity(intent); 
									array[3]=false;
									stopTimer();
								}  
							}
						};
						timer=new Timer();

						timer.schedule(timerTask, 0);*/

					}else if ((x-handX)*(x-handX)+(y-handY)*(y-handY)<=radius*radius) {
						tv_bodypart.setText(R.string.hand);
						tv_bodypart.setX((float) (handX-radius*1.4));
						tv_bodypart.setY((float) (handY));
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}
						Toast.makeText(BodyActivity.this, "暂无数据", 0).show();

						/*timerTask=new TimerTask() {

							@Override
							public void run() {

								if(  !array[4]){

									array[4]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 
								}else{
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 5);
									intent.putExtra("bodypart","手部");
									startActivity(intent); 

									array[4]=false;
									stopTimer();
								}  
							}
						};
						timer=new Timer();

						timer.schedule(timerTask, 0);*/

					}else if ((x-chestX)*(x-chestX)+(y-chestY)*(y-chestY)<=radius*radius) {
						tv_bodypart.setText(R.string.chest);
						tv_bodypart.setX((float) (chestX-radius*1.4));
						tv_bodypart.setY((float) (chestY));
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}
						Toast.makeText(BodyActivity.this, "暂无数据", 0).show();

						/*timerTask=new TimerTask() {

							@Override
							public void run() {
								if(  !array[5]){

									array[5]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 
								}else{ 
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 6);
									intent.putExtra("bodypart","胸部");
									startActivity(intent); 

									array[5]=false;
									stopTimer();
								}  

							}
						};
						timer=new Timer();

						timer.schedule(timerTask, 0);
*/
					}else if ((x-pelvicX)*(x-pelvicX)+(y-pelvicY)*(y-pelvicY)<=radius*radius) {
						tv_bodypart.setText(R.string.pelvic);
						tv_bodypart.setX((float) (pelvicX-radius*1.4));
						tv_bodypart.setY((float) (pelvicY));
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}
						Toast.makeText(BodyActivity.this, "暂无数据", 0).show();

						/*timerTask=new TimerTask() {

							@Override
							public void run() {

								if( !array[6]){
									array[6]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 
								}else{
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 8);
									intent.putExtra("bodypart","盆腔 ");
									startActivity(intent); 
									array[6]=false;
									stopTimer();
								}  
							}
						};
						timer=new Timer();

						timer.schedule(timerTask, 0);*/

					}else if ((x-stomachX)*(x-stomachX)+(y-stomachY)*(y-stomachY)<=radius*radius) {
						tv_bodypart.setText(R.string.stomach);
						tv_bodypart.setX((float) (stomachX-radius*1.4));
						tv_bodypart.setY((float) (stomachY));
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}
						timerTask=new TimerTask() {

							@Override
							public void run() {

								if(!array[7]){
									array[7]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 
								}else{ 
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 7);
									intent.putExtra("bodypart","腹部");
									startActivity(intent); 
									array[7]=false;
									stopTimer();
								}  
							}
						};
						timer=new Timer();						
						timer.schedule(timerTask, 0);

					}else if ((x-legX)*(x-legX)+(y-legY)*(y-legY)<=radius*radius) {
						tv_bodypart.setText(R.string.leg);
						tv_bodypart.setX((float) (legX-radius*1.4));
						tv_bodypart.setY((float) (legY));
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}
						Toast.makeText(BodyActivity.this, "暂无数据", 0).show();

						/*timerTask=new TimerTask() {

							@Override
							public void run() {
								if(!array[8]){
									array[8]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 
								}else{ 
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 9);
									intent.putExtra("bodypart","大腿");
									startActivity(intent);
									array[8]=false;
									stopTimer();
								}  
							}
						};
						timer=new Timer();					
						timer.schedule(timerTask, 0);*/

					}else if ((x-kneeX)*(x-kneeX)+(y-kneeY)*(y-kneeY)<=radius*radius) {
						tv_bodypart.setText(R.string.knee);
						tv_bodypart.setX((float) (kneeX-radius*1.4));
						tv_bodypart.setY((float) (kneeY));
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}
						Toast.makeText(BodyActivity.this, "暂无数据", 0).show();

						/*timerTask=new TimerTask() {

							@Override
							public void run() {

								if(  !array[9]){
									array[9]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 
								}else{
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 10);
									intent.putExtra("bodypart","小腿");
									startActivity(intent); 
									array[9]=false;
									stopTimer();
								}  
							}
						};
						timer=new Timer();

						timer.schedule(timerTask, 0);*/

					}else if ((x-footX)*(x-footX)+(y-footY)*(y-footY)<=radius*radius) {
						tv_bodypart.setText(R.string.foot);
						tv_bodypart.setX((float) (footX-radius*1.4));
						tv_bodypart.setY((float) (footY));
						tv_bodypart.setVisibility(View.VISIBLE);
//						if(timer!=null){
//							stopTimer();
//						}
						Toast.makeText(BodyActivity.this, "暂无数据", 0).show();

						/*timerTask=new TimerTask() {

							@Override
							public void run() {
								if(!array[10]){
									array[10]=true;
									handler.sendEmptyMessageDelayed(0, 2000); 
								}else{  
									Intent intent=new Intent();
									intent.setClass(getApplicationContext(), GridViewActivity.class);
									intent.putExtra("bodyid", 11);
									intent.putExtra("bodypart","足部");
									startActivity(intent); 
									array[10]=false;
									stopTimer();
								}  
							}
						};
						timer=new Timer();

						timer.schedule(timerTask, 0);*/

					}else {
						tv_bodypart.setVisibility(View.GONE);
						array[0]=false;
						array[1]=false;
						array[2]=false;
						array[3]=false;
						array[4]=false;
						array[5]=false;
						array[6]=false;
						array[7]=false;
						array[8]=false;
						array[9]=false;
						array[10]=false;

						if(timer!=null){
							stopTimer();
						}
					}
					break;

				default:
					break;
				}
				return false ;
			}
		});

		/*imageView.setOnLongClickListener(new OnLongClickListener() {		

			@Override
			public boolean onLongClick(View arg0) {
				double longClick_X= x;
				double longClick_Y=y;
				if((longClick_X-headX)*(longClick_X-headX)+(longClick_Y-headY)*(longClick_Y-headY)<=radius*radius){
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 1);
					intent.putExtra("bodypart","头部");
					startActivity(intent);
				}else if ((x-neckX)*(x-neckX)+(y-neckY)*(y-neckY)<=radius*radius) {
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 2);
					intent.putExtra("bodypart","颈部");
					startActivity(intent);
				}else if ((x-shoulderX)*(x-shoulderX)+(y-shoulderY)*(y-shoulderY)<=radius*radius) {
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 3);
					intent.putExtra("bodypart","肩部");
					startActivity(intent);
				}else if ((x-forearmX)*(x-forearmX)+(y-forearmY)*(y-forearmY)<=radius*radius) {
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 4);
					intent.putExtra("bodypart","手臂");
					startActivity(intent);
				}else if ((x-handX)*(x-handX)+(y-handY)*(y-handY)<=radius*radius) {
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 5);
					intent.putExtra("bodypart","手部");
					startActivity(intent);
				}else if ((x-chestX)*(x-chestX)+(y-chestY)*(y-chestY)<=radius*radius) {
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 6);
					intent.putExtra("bodypart","胸部");
					startActivity(intent);
				}else if ((x-stomachX)*(x-stomachX)+(y-stomachY)*(y-stomachY)<=radius*radius) {
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 7);
					intent.putExtra("bodypart","腹部");
					startActivity(intent);
				}else if ((x-pelvicX)*(x-pelvicX)+(y-pelvicY)*(y-pelvicY)<=radius*radius) {
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 8);
					intent.putExtra("bodypart","盆腔");
					startActivity(intent);
				}else if ((x-legX)*(x-legX)+(y-legY)*(y-legY)<=radius*radius) {
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 9);
					intent.putExtra("bodypart","大腿");
					startActivity(intent);
				}else if ((x-kneeX)*(x-kneeX)+(y-kneeY)*(y-kneeY)<=radius*radius) {
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 10);
					intent.putExtra("bodypart","小腿");
					startActivity(intent);
				}else if ((x-footX)*(x-footX)+(y-footY)*(y-footY)<=radius*radius) {
					final Intent intent=new Intent();
					intent.setClass(getApplicationContext(), GridViewActivity.class);
					intent.putExtra("bodyid", 11);
					intent.putExtra("bodypart","足部");
					startActivity(intent);
				}
				return false;
			}
		});	*/	
	}

	Handler handler = new Handler() {  		  
		@Override  
		public void handleMessage(Message msg) {  
			super.handleMessage(msg);  
			array[0]=false;
			array[1]=false;
			array[2]=false;
			array[3]=false;
			array[4]=false;
			array[5]=false;
			array[6]=false;
			array[7]=false;
			array[8]=false;
			array[9]=false;
			array[10]=false;
		}   
	};
	
	

	private void stopTimer(){
		if (timer!= null) {  
			timer.cancel();  
			timer = null;  
		}  

		if (timerTask!= null) {  
			timerTask.cancel();  
			timerTask = null;  
		}     
	}
}






















