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
	// ����ؼ�
	EditText edt_sthemeold;//����
	EditText edt_sareaold;//�ص�
	EditText edt_stimeold;//����ʱ��
	Button but_stimeold;//�趨ʱ�䰴ť
	Button but_addscheduleold;//���水ť
	CheckBox checkrepeatold;//�Ƿ��ظ�checkbox
	Spinner spi_repeatintervalold;//�趨�ظ�ʱ�������б�
	EditText edt_moreold;//��ע
	String setremindtimeold;//����ʱ��
	long remindtime;//������Ӧʱ��
	int setday;//��ǰѡ�����ڵ���
	int timeinterval = 0;//�����ظ���Ӧʱ��
	int[] intervalsel = new int[] { 5, 15, 30, 60 };//�ظ���Ӧʱ���������飺5,15.30.60����
	boolean repeat = false;//�ж��û��趨�Ƿ���Ҫ�ظ�

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_schedule);
		// ȡ��date
		Intent in = this.getIntent();
		final Bundle bu = in.getExtras();
		final String sdate = bu.getInt("syear") + "-" + bu.getInt("smonth")
				+ "-" + bu.getInt("sday");
		//��������һ���µĵڼ��죬ת��Ϊ��һ��ĵڼ���
		setday = bu.getInt("sday")
				+ addday(bu.getInt("smonth"), bu.getInt("syear"));
		// �󶨿ؼ�
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
		// Ϊ�趨ʱ�䰴ť�趨ʱ��Ի�
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
		// �趨�Ƿ��ظ�����
		checkrepeatold
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton arg0,
							boolean arg1) {
						// TODO Auto-generated method stub
						repeat = arg1;
					}
				});
		// �趨ѡ����ظ�ʱ����
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
		// ��Edit���������ȡ����
		but_addscheduleold.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//ȡ����
				String stheme = edt_sthemeold.getText().toString();
				//ȡ�ص�
				String sarea = edt_sareaold.getText().toString();
				//ȡ��ע
				String smore = edt_moreold.getText().toString();
				//���±�����༭����
				setremindtimeold=but_stimeold.getText().toString();
				Update(stheme, sarea, sdate, setremindtimeold, repeat,
						timeinterval, smore);
				//����ѡ�������ڽ�����ת
				Intent in = new Intent(EditScheduleActivity.this,
						ScheduleActivity.class);
				in.putExtras(bu);
				in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in);
				EditScheduleActivity.this.finish();
			}
		});
	
	}
	//��ʾҪ�༭���ճ̵���ǰ����ϸ��Ϣ����Ҫ�����⣬�ص㣬��ע
	private void ShowDetail() {
		// TODO Auto-generated method stub
		Intent in = this.getIntent();
		Bundle bu = in.getExtras();
		//ȡ�������ֵ�����⣬��Ϊ�ؼ��ֲ���
		String stheme = bu.getString("stheme");
		//�����ĸ��ؼ�
		edt_sthemeold = (EditText) this.findViewById(R.id.edt_sthemeold);
		edt_sareaold = (EditText) this.findViewById(R.id.edt_sareaold);
		//edt_stimeold = (EditText) this.findViewById(R.id.edt_stimeold);
		but_stimeold=(Button) this.findViewById(R.id.but_stimeold);
		edt_moreold = (EditText) this.findViewById(R.id.edt_moreold);
		//�����ݿ�
		mydbhelper mdh = new mydbhelper(EditScheduleActivity.this);
		SQLiteDatabase sdb = mdh.getReadableDatabase();
		//��������Ϊ�ؼ��ֲ���
		Cursor cur = sdb.query(mydbhelper.SCHEDULE_TABLE_NAME, new String[] {
				"stheme", "sarea", "sremindtime", "smore" }, "stheme=?",
				new String[] { stheme }, null, null, null);
		//ѭ�������
		while (cur.moveToNext()) {
			// ��ʾ������Ϣ
			String theme = cur.getString(0);
			edt_sthemeold.setText(theme);
			String area = cur.getString(1);
			edt_sareaold.setText(area);
			String retime = cur.getString(2);
			but_stimeold.setText(retime);
			String more = cur.getString(3);
			edt_moreold.setText(more);
		}// while.
		//�ر����ݿ�
		sdb.close();
	}
	//�������ݿ⺯��
	protected void Update(String stheme, String sarea, String sdate,
			String setremindtimeold2, boolean repeat2, int timeinterval2,
			String smore) {
		// TODO Auto-generated method stub
		//����û����趨�ظ������ظ�ʱ����Ϊ0
		if( !repeat2)
		{
			timeinterval2 = 0;
		}
		Intent in = this.getIntent();
		Bundle bu = in.getExtras();
		//ȡ����������⣬��Ϊ���µĹؼ���
		String getstheme = bu.getString("stheme");
		//�����ݿ�
		mydbhelper mdh = new mydbhelper(EditScheduleActivity.this);
		SQLiteDatabase sdb = mdh.getReadableDatabase();
		//���������ݷ���һ������
		ContentValues cv = new ContentValues();
		cv.put("stheme", stheme);
		cv.put("sarea", sarea);
		cv.put("sdate", sdate);
		cv.put("sremindtime", setremindtimeold2);
		cv.put("isrepeat", repeat2);
		cv.put("repeatinterval", timeinterval2);
		cv.put("smore", smore);
		//ִ�����ݿ�������
		sdb.update(mydbhelper.SCHEDULE_TABLE_NAME, cv, "stheme=?",new String[] { getstheme });
		//��ʼ�����ӵ�id
		int id = 0;
		//ͨ���ճ������ҵ��ճ̵�id
		Cursor cur = sdb.query(mydbhelper.SCHEDULE_TABLE_NAME, new String[]{"_id"}, "stheme=?", new String[]{stheme}, null, null, null);
		//���ճ̵�id��Ϊ���ӵ�id
		while(cur.moveToNext()){
			id = cur.getInt(0);
		}
		//������ظ���ֻ�趨һ������
		if( !repeat2)
		{
			Bundle buset = new Bundle();
			Intent intent = new Intent(EditScheduleActivity.this,AlarmshowActivity.class);
			//�趨��������
			AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
			//�����ճ�������Ϊ������Ӧʱ�Ĳ���
			buset.putInt("amid", id);
			buset.putString("msg",stheme);
			intent.putExtras(buset);
			//�趨һ��������
			PendingIntent pi = PendingIntent.getActivity(EditScheduleActivity.this, id, intent, 0);
			am.set(AlarmManager.RTC_WAKEUP,remindtime, pi);
		}
		//����ظ����趨�ظ�����
		else{
			Bundle buset = new Bundle();
			Intent intent = new Intent(EditScheduleActivity.this,AlarmshowActivity.class);
			//�趨��������
			AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
			//�����ճ�������Ϊ������Ӧʱ�Ĳ���
			buset.putString("msg",stheme);
			buset.putInt("amid", id);
			intent.putExtras(buset);
			//�趨�ظ�����
			PendingIntent pi = PendingIntent.getActivity(EditScheduleActivity.this, id, intent, 0);
			am.setRepeating(AlarmManager.RTC_WAKEUP, remindtime,timeinterval2*60*1000, pi);
		}
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
							setremindtimeold = hor + ":" + min;
							but_stimeold.setText(setremindtimeold);
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
}
