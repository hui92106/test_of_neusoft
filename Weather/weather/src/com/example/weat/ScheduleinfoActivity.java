package com.example.weat;

import com.example.weather.R;
import com.example.weather.mydbhelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScheduleinfoActivity extends Activity {
	//定义控件
	TextView tv_gettheme;
	TextView tv_getarea;
	TextView tv_gettime;
	TextView tv_getisrepeat;
	TextView tv_gettimeinterval;
	TextView tv_getmore;
	Button but_del;
	Button but_edt;
	Bundle buget;
	String curstheme;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scheduleinfo);
		//得到传入的日程主题
		Intent in = this.getIntent();
		Bundle bu = in.getExtras();
		buget = bu;
		String stheme = bu.getString("stheme");
		curstheme = stheme;
		//显示信息
		ShowSchedule();
		//删除按钮
		but_del = (Button) this.findViewById(R.id.but_del);
		but_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ScheduleinfoActivity.this)
				.setTitle("是否确认删除？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						//打开数据库
						mydbhelper mdh = new mydbhelper(ScheduleinfoActivity.this);
						SQLiteDatabase sdb = mdh.getReadableDatabase();
						//得到日程id，作为闹钟id用来覆盖之前的闹钟
						Cursor cur = sdb.query(mydbhelper.SCHEDULE_TABLE_NAME, new String[]{"_id"}, "stheme = ?", new String[]{curstheme}, null, null, null);
						int id=0;
						while(cur.moveToNext()){
							id = cur.getInt(0);
						}
						//关闭数据库
						sdb.close();
						//新建一个闹钟，覆盖之前闹钟，再删除当前闹钟，达到取消闹钟的效果
						Intent intent = new Intent(ScheduleinfoActivity.this,AlarmshowActivity.class);
						AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
						PendingIntent pi = PendingIntent.getActivity(ScheduleinfoActivity.this, id, intent, 0);
						am.set(AlarmManager.RTC_WAKEUP,0, pi);
						am.cancel(pi);
						DelSchedule();
					}})
					//点击取消，则对话框被取消，不执行删除操作
					.setNegativeButton("取消", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}}).create().show();
			}
		});
		//编辑按钮
		but_edt = (Button) this.findViewById(R.id.but_edt);
		but_edt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//传入参数，跳转到编辑页面
				Intent inset = new Intent(ScheduleinfoActivity.this,EditScheduleActivity.class);
				inset.putExtras(buget);
				startActivity(inset);
				finish();
			}
		});
		Button btn_back=(Button) this.findViewById(R.id.btn_back2);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent in=new Intent(ScheduleinfoActivity.this,ScheduleActivity.class);
				//startActivity(in);
				finish();	
			}
		});
		
	}
	//删除日程函数
	private void DelSchedule() {
		// TODO Auto-generated method stub
		//打开数据库
		mydbhelper mdb=new mydbhelper(ScheduleinfoActivity.this);
		SQLiteDatabase sdb=mdb.getReadableDatabase();
		//执行删除操作
		sdb.delete(mydbhelper.SCHEDULE_TABLE_NAME, "stheme=?", new String[]{curstheme});
		//关闭数据库
		sdb.close();
		Intent inset = new Intent(ScheduleinfoActivity.this,ScheduleActivity.class);
		
		//传入选定日期，跳转到今日日程显示界面
		inset.putExtras(buget);
		inset.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(inset);
		ScheduleinfoActivity.this.finish();
	}
	//显示选定日程的函数
	private void ShowSchedule() {
		// TODO Auto-generated method stub
		//定义显示空间
		tv_gettheme = (TextView) this.findViewById(R.id.tv_gettheme);
		tv_getarea = (TextView) this.findViewById(R.id.tv_getarea);
		tv_gettime = (TextView) this.findViewById(R.id.tv_gettime);
		tv_getisrepeat = (TextView) this.findViewById(R.id.tv_getisrepeat);
		tv_gettimeinterval = (TextView) this.findViewById(R.id.tv_gettimeinterval);
		tv_getmore = (TextView) this.findViewById(R.id.tv_getmore);
		//打开数据库
		mydbhelper mdb=new mydbhelper(ScheduleinfoActivity.this);
		final SQLiteDatabase sdb=mdb.getReadableDatabase();
		//以日程主题作为关键字查询要显示的日程
		Cursor cur=sdb.query(mydbhelper.SCHEDULE_TABLE_NAME, new String[]{"stheme","sarea","sremindtime","isrepeat","repeatinterval","smore"}, "stheme=?", new String[]{curstheme}, null, null, null);
		//遍历查询结果，得到要显示的信息
		while(cur.moveToNext()){
			//显示基本信息
			String theme = cur.getString(0);
			tv_gettheme.setText(theme);
			String area = cur.getString(1);
			tv_getarea.setText(area);
			String retime = cur.getString(2);
			tv_gettime.setText(retime);
			String isrepeat = cur.getString(3);
			//判断用户是否设定了重复提醒
			if(isrepeat.equals("1")){
				tv_getisrepeat.setText("是");
			}
			else {
				tv_getisrepeat.setText("否");
			}
			String timeinterval = cur.getString(4)+" 分钟";
			tv_gettimeinterval.setText(timeinterval);
			String more =  cur.getString(5);
			tv_getmore.setText(more);
		}//while.
		sdb.close();
	}
}
