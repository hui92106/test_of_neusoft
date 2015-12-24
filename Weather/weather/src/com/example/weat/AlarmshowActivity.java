package com.example.weat;

import com.example.weather.R;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
//设定时间到的时候闹钟响应方式：对话框的形式
public class AlarmshowActivity extends Activity {
	MediaPlayer mMediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarmshow);
		//背景音乐设定
		mMediaPlayer = MediaPlayer.create(this, R.raw.schedule);
		//播放背景音乐
		mMediaPlayer.start();
		//显示对话框
		showDialog();
		
	}
	public void showDialog(){
		Intent in = this.getIntent();
		Bundle bu = in.getExtras();
		//取出闹钟响应时显示的日程名
		final String msg = bu.getString("msg");
		//取出响应闹钟的id，方便重置或者覆盖闹钟
		final int amid = bu.getInt("amid");
		new AlertDialog.Builder(AlarmshowActivity.this)
		//对话框的标题
		.setTitle("日程提醒："+msg)
		//第一个按钮：暂停再响，间隔为5分钟
		.setPositiveButton("暂停再响",new DialogInterface.OnClickListener(){

			@SuppressLint("ShowToast")
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(AlarmshowActivity.this, "五分钟之后继续提醒", 1);
				//设定响应的时间，从现在开始，5分钟之后
				long remindtime =System.currentTimeMillis()+5*60*1000;
				//传入跳转的参数：日程主题
				Intent intent = new Intent(AlarmshowActivity.this,AlarmshowActivity.class);
				Bundle buset = new Bundle();
				buset.putString("msg", msg);
				buset.putInt("amid", amid);
				PendingIntent pi = PendingIntent.getActivity(AlarmshowActivity.this, amid, intent, 0);
				//设定闹钟，覆盖之前
				AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
				am.set(AlarmManager.RTC_WAKEUP,remindtime, pi);
				//停止音乐
				mMediaPlayer.stop();
				//关闭当前页面
				finish();
				
			//取消按钮：设定了重复提醒，还是会继续响应，若只提醒一次，则闹钟不再响应	
			}}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//停止音乐
					mMediaPlayer.stop();
					//关闭页面
					finish();
				}
			//停止闹钟，闹钟被覆盖，不再提示，即使设定了重复提醒
			}).setNeutralButton("停止", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					//设定一个任意时刻的闹钟
					long remindtime =System.currentTimeMillis()+5*60*1000;
					Intent intent = new Intent(AlarmshowActivity.this,AlarmshowActivity.class);
					//创建一个闹钟响应时刻
					PendingIntent pi = PendingIntent.getActivity(AlarmshowActivity.this, amid, intent, 0);
					//设定一个闹钟
					AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
					//设定闹钟响应时刻
					am.set(AlarmManager.RTC_WAKEUP, remindtime, pi);
					//取消这个闹钟，则原有闹钟被覆盖，当前闹钟被取消，闹钟则不存在，停止闹钟的效果达到
					am.cancel(pi);
					//停止音乐
					mMediaPlayer.stop();
					//关闭当前页面
					finish();
				}
			}).create().show();
		
	}

}
