package com.example.weat;

import java.util.Calendar;

import com.example.weather.R;
import com.example.weather.mydbhelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.YuvImage;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class EditScheduleActivity extends Activity {
	// 定义控件
	EditText edt_sthemeold;//主题
	EditText edt_sareaold;//地点
	EditText edt_stimeold;//提醒时间
	Button but_stimeold;//设定时间按钮
	Button but_addscheduleold;//保存按钮
	CheckBox checkrepeatold;//是否重复checkbox
	Spinner spi_repeatintervalold;//设定重复时间下拉列表
	EditText edt_moreold;//备注
	String setremindtimeold;//提醒时间
	long remindtime;//闹钟响应时间
	int setday;//当前选定日期的天
	int timeinterval = 0;//闹钟重复响应时间
	int[] intervalsel = new int[] { 5, 15, 30, 60 };//重复响应时间整数数组：5,15.30.60分钟
	boolean repeat = false;//判断用户设定是否需要重复

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_schedule);
		// 取出date
		Intent in = this.getIntent();
		final Bundle bu = in.getExtras();
		final String sdate = bu.getInt("syear") + "-" + bu.getInt("smonth")
				+ "-" + bu.getInt("sday");
		//将今天是一个月的第几天，转化为这一年的第几天
		setday = bu.getInt("sday")
				+ addday(bu.getInt("smonth"), bu.getInt("syear"));
		// 绑定控件
		edt_sthemeold = (EditText) this.findViewById(R.id.edt_sthemeold);
		edt_sareaold = (EditText) this.findViewById(R.id.edt_sareaold);
		//edt_stimeold = (EditText) this.findViewById(R.id.edt_stimeold);
		but_stimeold = (Button) this.findViewById(R.id.but_stimeold);
		checkrepeatold = (CheckBox) this.findViewById(R.id.checkrepeatold);
		spi_repeatintervalold = (Spinner) this
				.findViewById(R.id.spi_repeatintervalold);
		edt_moreold = (EditText) this.findViewById(R.id.edt_moreold);
		but_addscheduleold = (Button) this
				.findViewById(R.id.but_addscheduleold);
		ShowDetail();
		// 为设定时间按钮设定时间对话
		but_stimeold.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(1);
			}
		});
		Button btn_back=(Button) this.findViewById(R.id.btn_back1);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent in = new Intent(EditScheduleActivity.this,ScheduleActivity.class);
				//in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				//startActivity(in);
				finish();	
			}
		});
		// 设定是否重复提醒
		checkrepeatold
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						repeat = arg1;
					}
				});
		// 设定选择的重复时间间隔
		spi_repeatintervalold
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int option, long arg3) {
						// TODO Auto-generated method stub
						timeinterval = intervalsel[option];
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});
		// 将Edit里面的内容取出来
		but_addscheduleold.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//取主题
				String stheme = edt_sthemeold.getText().toString();
				//取地点
				String sarea = edt_sareaold.getText().toString();
				//取备注
				String smore = edt_moreold.getText().toString();
				//更新表，保存编辑内容
				setremindtimeold=but_stimeold.getText().toString();
				Update(stheme, sarea, sdate, setremindtimeold, repeat,
						timeinterval, smore);
				//传入选定的日期进行跳转
				Intent in = new Intent(EditScheduleActivity.this,
						ScheduleActivity.class);
				in.putExtras(bu);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
				EditScheduleActivity.this.finish();
			}
		});
	
	}
	//显示要编辑的日程的以前的详细信息：主要是主题，地点，备注
	private void ShowDetail() {
		// TODO Auto-generated method stub
		Intent in = this.getIntent();
		Bundle bu = in.getExtras();
		//取出传入的值：主题，作为关键字查找
		String stheme = bu.getString("stheme");
		//定义四个控件
		edt_sthemeold = (EditText) this.findViewById(R.id.edt_sthemeold);
		edt_sareaold = (EditText) this.findViewById(R.id.edt_sareaold);
		//edt_stimeold = (EditText) this.findViewById(R.id.edt_stimeold);
		but_stimeold=(Button) this.findViewById(R.id.but_stimeold);
		edt_moreold = (EditText) this.findViewById(R.id.edt_moreold);
		//打开数据库
		mydbhelper mdh = new mydbhelper(EditScheduleActivity.this);
		SQLiteDatabase sdb = mdh.getReadableDatabase();
		//按主题作为关键字查找
		Cursor cur = sdb.query(mydbhelper.SCHEDULE_TABLE_NAME, new String[] {
				"stheme", "sarea", "sremindtime", "smore" }, "stheme=?",
				new String[] { stheme }, null, null, null);
		//循环结果集
		while (cur.moveToNext()) {
			// 显示基本信息
			String theme = cur.getString(0);
			edt_sthemeold.setText(theme);
			String area = cur.getString(1);
			edt_sareaold.setText(area);
			String retime = cur.getString(2);
			but_stimeold.setText(retime);
			String more = cur.getString(3);
			edt_moreold.setText(more);
		}// while.
		//关闭数据库
		sdb.close();
	}
	//更新数据库函数
	protected void Update(String stheme, String sarea, String sdate,
			String setremindtimeold2, boolean repeat2, int timeinterval2,
			String smore) {
		// TODO Auto-generated method stub
		//如果用户不设定重复，把重复时间设为0
		if( !repeat2)
		{
			timeinterval2 = 0;
		}
		Intent in = this.getIntent();
		Bundle bu = in.getExtras();
		//取传入参数主题，作为更新的关键字
		String getstheme = bu.getString("stheme");
		//打开数据库
		mydbhelper mdh = new mydbhelper(EditScheduleActivity.this);
		SQLiteDatabase sdb = mdh.getReadableDatabase();
		//将更新数据放入一个集合
		ContentValues cv = new ContentValues();
		cv.put("stheme", stheme);
		cv.put("sarea", sarea);
		cv.put("sdate", sdate);
		cv.put("sremindtime", setremindtimeold2);
		cv.put("isrepeat", repeat2);
		cv.put("repeatinterval", timeinterval2);
		cv.put("smore", smore);
		//执行数据库更新语句
		sdb.update(mydbhelper.SCHEDULE_TABLE_NAME, cv, "stheme=?",new String[] { getstheme });
		//初始化闹钟的id
		int id = 0;
		//通过日程主题找到日程的id
		Cursor cur = sdb.query(mydbhelper.SCHEDULE_TABLE_NAME, new String[]{"_id"}, "stheme=?", new String[]{stheme}, null, null, null);
		//将日程的id作为闹钟的id
		while(cur.moveToNext()){
			id = cur.getInt(0);
		}
		//如果不重复则只设定一次闹钟
		if( !repeat2)
		{
			Bundle buset = new Bundle();
			Intent intent = new Intent(EditScheduleActivity.this,AlarmshowActivity.class);
			//设定闹钟类型
			AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
			//传入日程主题作为闹钟响应时的参数
			buset.putInt("amid", id);
			buset.putString("msg",stheme);
			intent.putExtras(buset);
			//设定一次性闹钟
			PendingIntent pi = PendingIntent.getActivity(EditScheduleActivity.this, id, intent, 0);
			am.set(AlarmManager.RTC_WAKEUP,remindtime, pi);
		}
		//如果重复则设定重复闹钟
		else{
			Bundle buset = new Bundle();
			Intent intent = new Intent(EditScheduleActivity.this,AlarmshowActivity.class);
			//设定闹钟类型
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			//传入日程主题作为闹钟响应时的参数
			buset.putString("msg",stheme);
			buset.putInt("amid", id);
			intent.putExtras(buset);
			//设定重复闹钟
			PendingIntent pi = PendingIntent.getActivity(EditScheduleActivity.this, id, intent, 0);
			am.setRepeating(AlarmManager.RTC_WAKEUP, remindtime,timeinterval2*60*1000, pi);
		}
		//关闭数据库
		sdb.close();
	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		Dialog dialog = null;
		// 日期
		// 时间对话框
		if (id == 1) {
			//创建一个日历对象
			final Calendar c = Calendar.getInstance();
			//得到今天是一年之中的第几天
			final int today = c.get(Calendar.DAY_OF_YEAR);
			dialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker arg0, int hor, int min) {
							// TODO Auto-generated method stub
							//设定提醒时间以hh:mm的形式存放在数据库
							setremindtimeold = hor + ":" + min;
							but_stimeold.setText(setremindtimeold);
							//取得当前时间
							c.setTimeInMillis(System.currentTimeMillis());
							// 将自己设定的时间传入日历
							c.set(Calendar.HOUR_OF_DAY, hor);
							c.set(Calendar.MINUTE, min);
							c.set(Calendar.SECOND, 0);
							c.set(Calendar.MILLISECOND, 0);
							//计算闹钟响应时的柏林时间
							remindtime = c.getTimeInMillis() + (setday - today)
									* 24 * 60 * 60 * 1000;
						}
					}, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true);
			//显示对话框
			dialog.show();
		}
		return super.onCreateDialog(id, args);
	}
    //计算这一年第几月之前有多少天。用来计算一个月的第几天是一年中的第几天
	private int addday(int month, int year) {
		// TODO Auto-generated method stub
		if (year % 4 == 0)
			switch (month) {
			case 0:
				return 0;
			case 1:
				return 31;
			case 2:
				return 60;
			case 3:
				return 91;
			case 4:
				return 121;
			case 5:
				return 152;
			case 6:
				return 182;
			case 7:
				return 213;
			case 8:
				return 244;
			case 9:
				return 274;
			case 10:
				return 305;
			case 11:
				return 335;

			}
		else {
			switch (month) {
			case 0:
				return 0;
			case 1:
				return 31;
			case 2:
				return 59;
			case 3:
				return 90;
			case 4:
				return 120;
			case 5:
				return 151;
			case 6:
				return 181;
			case 7:
				return 212;
			case 8:
				return 243;
			case 9:
				return 273;
			case 10:
				return 304;
			case 11:
				return 334;
			}
		}
		return 0;
	}
}
