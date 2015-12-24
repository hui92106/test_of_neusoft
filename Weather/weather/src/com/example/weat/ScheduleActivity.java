package com.example.weat;

import com.calendar.demo.CalendarActivity;
import com.example.weather.R;
import com.example.weather.mydbhelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class ScheduleActivity extends Activity {
	ListView lv_todayschedule;
	Bundle buget;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_schedule);
		//���ܴ�������������
		Intent inget= this.getIntent();
		lv_todayschedule = (ListView) this.findViewById(R.id.lv_todayschedule);
		final Bundle bu = inget.getExtras();
		buget = bu;
		int syear = bu.getInt("syear");
		int smonth = bu.getInt("smonth");
		int sday = bu.getInt("sday");
		//�����������ó�string
        String today =syear+"-"+smonth+"-"+sday;
        //�����������ճ��б���ʾ
        ListTodaySchedule(today);
        //���Ӱ�ť
        Button but_addschedule = (Button) this.findViewById(R.id.btn_add);
        //��ť������
        but_addschedule.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent in = new Intent(ScheduleActivity.this,ScheduleAddActivity.class);
				in.putExtras(bu);
				startActivity(in);
				finish();
			}
		});
    	Button btn_back=(Button) this.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			finish();
			}
		});
		
	}
    //��ʾ���������ճ�
	private void ListTodaySchedule(String today) {
		// TODO Auto-generated method stub
		//��ʼ�����ݿ�
		mydbhelper mdh = new mydbhelper(ScheduleActivity.this);
		SQLiteDatabase sdb = mdh.getReadableDatabase();
		//Ҫ��ʾ����
		String[] find =new String[]{"_id","sremindtime","stheme"};
		//��ѯ���ݿ�
		Cursor cur = sdb.query(mydbhelper.SCHEDULE_TABLE_NAME, find, "sdate = ?",new String[]{today}, null, null, null);
		//����������
		SimpleCursorAdapter sca = new SimpleCursorAdapter(ScheduleActivity.this, R.layout.todayscheduleitem, cur, find,new int[]{R.id.tv_todayscheduleitemid,R.id.tv_todayscheduleitemremindtime,R.id.tv_todayscheduleitemremindtheme},0);
		//�����������б�
		lv_todayschedule.setAdapter(sca);
		AdapterView.OnItemClickListener lvitemselect =new  AdapterView.OnItemClickListener(){
			//�ض���Ԫ�ر�ѡ��֮�󴥷����¼�
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//���ճ�������Ϊ����������һ��activity��������ʾ�ճ���ϸ��Ϣ
				TextView tv_stheme= (TextView) view.findViewById(R.id.tv_todayscheduleitemremindtheme);
				String stheme =tv_stheme.getText().toString();
				buget.putString("stheme", stheme);
				Intent in = new Intent(ScheduleActivity.this,ScheduleinfoActivity.class);
				in.putExtras(buget);
				startActivity(in);
				//finish();//go
			}

		};
	
		//���������б��
		lv_todayschedule.setOnItemClickListener(lvitemselect);
		//�ر����ݿ�
		sdb.close();
	}
	
}
