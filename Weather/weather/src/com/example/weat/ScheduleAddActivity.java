package com.example.weat;

import java.util.Calendar;

import com.example.weather.R;
import com.example.weather.mydbhelper;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

public class ScheduleAddActivity extends Activity {
	// 定义控件
	EditText edt_stheme;
	EditText edt_sarea;
	//EditText edt_stime;
	Button but_stime;
	Button but_addschedule;
	CheckBox checkrepeat;
	Spinner spi_repeatinterval;
	EditText edt_more;
	String setremindtime;
	int setday;
	long remindtime;
	int timeinterval = 0;
	int[] intervalsel = new int[] { 5, 15, 30, 60 };
	boolean repeat = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule_add);
		// 取出date
		Intent in = this.getIntent();
		final Bundle bu = in.getExtras();
		final String sdate = bu.getInt("syear") + "-" + bu.getInt("smonth")
				+ "-" + bu.getInt("sday");
		setday = bu.getInt("sday")
				+ addday(bu.getInt("smonth"), bu.getInt("syear"));
		// 绑定控件
		edt_stheme = (EditText) this.findViewById(R.id.edt_stheme);
		edt_sarea = (EditText) this.findViewById(R.id.edt_sarea);
		//edt_stime = (EditText) this.findViewById(R.id.edt_stime);
		but_stime = (Button) this.findViewById(R.id.but_stime);
		checkrepeat = (CheckBox) this.findViewById(R.id.checkrepeat);
		spi_repeatinterval = (Spinner) this
				.findViewById(R.id.spi_repeatinterval);
		edt_more = (EditText) this.findViewById(R.id.edt_more);
		but_addschedule = (Button) this.findViewById(R.id.but_addschedule);
		// 为设定时间按钮设定时间对话
		but_stime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(1);
			}
		});
		// 设定是否重复提醒
		checkrepeat.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				repeat = arg1;
			}
		});
		// 设定选择的重复时间间隔
		spi_repeatinterval
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
		but_addschedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//取主题
				String stheme = edt_stheme.getText().toString();
				//取地点
				String sarea = edt_sarea.getText().toString();
				//取备注
				String smore = edt_more.getText().toString();
				//增加表中数据，保存编辑内容
				AddSchedule(stheme, sarea, sdate, setremindtime, repeat,
						timeinterval, smore);
				//传入选定的日期进行跳转
				Intent in = new Intent(ScheduleAddActivity.this,
						ScheduleActivity.class);
				in.putExtras(bu);
				startActivity(in);
				ScheduleAddActivity.this.finish();
			}
		});
		Button btn_back=(Button) this.findViewById(R.id.btn_back1);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(ScheduleAddActivity.this,
						ScheduleActivity.class);
				in.putExtras(bu);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
			 finish();
			}
		});
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
	//增加日程的函数
	@SuppressLint("ShowToast")
	private void AddSchedule(String stheme, String sarea, String sdate,
			String setremindtime2, boolean repeat2, int timeinterval2,
			String smore) {
		// TODO Auto-generated method stub
		//如果用户不设定重复，把重复时间设为0
		if (!repeat2) {
			timeinterval2 = 0;
		}
		//打开数据库
		mydbhelper mdh = new mydbhelper(ScheduleAddActivity.this);
		SQLiteDatabase sdb = mdh.getReadableDatabase();
		//将增加数据放入一个集合
		ContentValues cv = new ContentValues();
		cv.put("stheme", stheme);
		cv.put("sarea", sarea);
		cv.put("sdate", sdate);
		cv.put("sremindtime", setremindtime2);
		cv.put("isrepeat", repeat2);
		cv.put("repeatinterval", timeinterval2);
		cv.put("smore", smore);
		//执行数据库增加语句
		sdb.insertOrThrow(mydbhelper.SCHEDULE_TABLE_NAME, null, cv);
		//初始化闹钟的id
		int id = 0;
		//通过日程主题找到日程的id
		Cursor cur = sdb.query(mydbhelper.SCHEDULE_TABLE_NAME,
				new String[] { "_id" }, "stheme=?", new String[] { stheme },
				null, null, null);
		//将日程的id作为闹钟的id
		while (cur.moveToNext()) {
			id = cur.getInt(0);
		}
		//如果不重复则只设定一次闹钟
		if (!repeat2) {
			Bundle bu = new Bundle();
			Intent intent = new Intent(ScheduleAddActivity.this,
					AlarmshowActivity.class);
			//设定闹钟类型
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			//传入日程主题和闹钟id作为闹钟响应时的参数
			bu.putInt("amid", id);
			bu.putString("msg", stheme);
			intent.putExtras(bu);
			//设定一次性闹钟
			PendingIntent pi = PendingIntent.getActivity(ScheduleAddActivity.this, id, intent, 0);
			am.set(AlarmManager.RTC_WAKEUP, remindtime, pi);
		}
		//如果重复则设定重复闹钟
		else {
			Bundle bu = new Bundle();
			Intent intent = new Intent(ScheduleAddActivity.this,
					AlarmshowActivity.class);
			//设定闹钟类型
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			//传入日程主题和闹钟id作为闹钟响应时的参数
			bu.putString("msg", stheme);
			bu.putInt("amid", id);
			intent.putExtras(bu);
			//设定重复闹钟
			PendingIntent pi = PendingIntent.getActivity(
					ScheduleAddActivity.this, id, intent, 0);
			am.setRepeating(AlarmManager.RTC_WAKEUP, remindtime,timeinterval2*60*1000, pi);
		}
		//显示提示数据
		Toast.makeText(ScheduleAddActivity.this, "日程添加成功！", 1).show();
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
							setremindtime = hor + ":" + min;
							but_stime.setText(setremindtime);
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

}
