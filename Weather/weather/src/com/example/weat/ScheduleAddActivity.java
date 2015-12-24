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
	// ����ؼ�
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
		// ȡ��date
		Intent in = this.getIntent();
		final Bundle bu = in.getExtras();
		final String sdate = bu.getInt("syear") + "-" + bu.getInt("smonth")
				+ "-" + bu.getInt("sday");
		setday = bu.getInt("sday")
				+ addday(bu.getInt("smonth"), bu.getInt("syear"));
		// �󶨿ؼ�
		edt_stheme = (EditText) this.findViewById(R.id.edt_stheme);
		edt_sarea = (EditText) this.findViewById(R.id.edt_sarea);
		//edt_stime = (EditText) this.findViewById(R.id.edt_stime);
		but_stime = (Button) this.findViewById(R.id.but_stime);
		checkrepeat = (CheckBox) this.findViewById(R.id.checkrepeat);
		spi_repeatinterval = (Spinner) this
				.findViewById(R.id.spi_repeatinterval);
		edt_more = (EditText) this.findViewById(R.id.edt_more);
		but_addschedule = (Button) this.findViewById(R.id.but_addschedule);
		// Ϊ�趨ʱ�䰴ť�趨ʱ��Ի�
		but_stime.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDialog(1);
			}
		});
		// �趨�Ƿ��ظ�����
		checkrepeat.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				repeat = arg1;
			}
		});
		// �趨ѡ����ظ�ʱ����
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
		// ��Edit���������ȡ����
		but_addschedule.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//ȡ����
				String stheme = edt_stheme.getText().toString();
				//ȡ�ص�
				String sarea = edt_sarea.getText().toString();
				//ȡ��ע
				String smore = edt_more.getText().toString();
				//���ӱ������ݣ�����༭����
				AddSchedule(stheme, sarea, sdate, setremindtime, repeat,
						timeinterval, smore);
				//����ѡ�������ڽ�����ת
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
	//������һ��ڼ���֮ǰ�ж����졣��������һ���µĵڼ�����һ���еĵڼ���
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
	//�����ճ̵ĺ���
	@SuppressLint("ShowToast")
	private void AddSchedule(String stheme, String sarea, String sdate,
			String setremindtime2, boolean repeat2, int timeinterval2,
			String smore) {
		// TODO Auto-generated method stub
		//����û����趨�ظ������ظ�ʱ����Ϊ0
		if (!repeat2) {
			timeinterval2 = 0;
		}
		//�����ݿ�
		mydbhelper mdh = new mydbhelper(ScheduleAddActivity.this);
		SQLiteDatabase sdb = mdh.getReadableDatabase();
		//���������ݷ���һ������
		ContentValues cv = new ContentValues();
		cv.put("stheme", stheme);
		cv.put("sarea", sarea);
		cv.put("sdate", sdate);
		cv.put("sremindtime", setremindtime2);
		cv.put("isrepeat", repeat2);
		cv.put("repeatinterval", timeinterval2);
		cv.put("smore", smore);
		//ִ�����ݿ��������
		sdb.insertOrThrow(mydbhelper.SCHEDULE_TABLE_NAME, null, cv);
		//��ʼ�����ӵ�id
		int id = 0;
		//ͨ���ճ������ҵ��ճ̵�id
		Cursor cur = sdb.query(mydbhelper.SCHEDULE_TABLE_NAME,
				new String[] { "_id" }, "stheme=?", new String[] { stheme },
				null, null, null);
		//���ճ̵�id��Ϊ���ӵ�id
		while (cur.moveToNext()) {
			id = cur.getInt(0);
		}
		//������ظ���ֻ�趨һ������
		if (!repeat2) {
			Bundle bu = new Bundle();
			Intent intent = new Intent(ScheduleAddActivity.this,
					AlarmshowActivity.class);
			//�趨��������
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			//�����ճ����������id��Ϊ������Ӧʱ�Ĳ���
			bu.putInt("amid", id);
			bu.putString("msg", stheme);
			intent.putExtras(bu);
			//�趨һ��������
			PendingIntent pi = PendingIntent.getActivity(ScheduleAddActivity.this, id, intent, 0);
			am.set(AlarmManager.RTC_WAKEUP, remindtime, pi);
		}
		//����ظ����趨�ظ�����
		else {
			Bundle bu = new Bundle();
			Intent intent = new Intent(ScheduleAddActivity.this,
					AlarmshowActivity.class);
			//�趨��������
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			//�����ճ����������id��Ϊ������Ӧʱ�Ĳ���
			bu.putString("msg", stheme);
			bu.putInt("amid", id);
			intent.putExtras(bu);
			//�趨�ظ�����
			PendingIntent pi = PendingIntent.getActivity(
					ScheduleAddActivity.this, id, intent, 0);
			am.setRepeating(AlarmManager.RTC_WAKEUP, remindtime,timeinterval2*60*1000, pi);
		}
		//��ʾ��ʾ����
		Toast.makeText(ScheduleAddActivity.this, "�ճ���ӳɹ���", 1).show();
		//�ر����ݿ�
		sdb.close();

	}

	@Override
	protected Dialog onCreateDialog(int id, Bundle args) {
		// TODO Auto-generated method stub
		Dialog dialog = null;
		// ����
		// ʱ��Ի���
		if (id == 1) {
			//����һ����������
			final Calendar c = Calendar.getInstance();
			//�õ�������һ��֮�еĵڼ���
			final int today = c.get(Calendar.DAY_OF_YEAR);
			dialog = new TimePickerDialog(this,
					new TimePickerDialog.OnTimeSetListener() {

						@Override
						public void onTimeSet(TimePicker arg0, int hor, int min) {
							// TODO Auto-generated method stub
							//�趨����ʱ����hh:mm����ʽ��������ݿ�
							setremindtime = hor + ":" + min;
							but_stime.setText(setremindtime);
							//ȡ�õ�ǰʱ��
							c.setTimeInMillis(System.currentTimeMillis());
							// ���Լ��趨��ʱ�䴫������
							c.set(Calendar.HOUR_OF_DAY, hor);
							c.set(Calendar.MINUTE, min);
							c.set(Calendar.SECOND, 0);
							c.set(Calendar.MILLISECOND, 0);
							//����������Ӧʱ�İ���ʱ��
							remindtime = c.getTimeInMillis() + (setday - today)
									* 24 * 60 * 60 * 1000;
						}
					}, c.get(Calendar.HOUR), c.get(Calendar.MINUTE), true);
			//��ʾ�Ի���
			dialog.show();
		}
		return super.onCreateDialog(id, args);
	}

}
