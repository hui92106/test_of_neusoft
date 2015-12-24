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
		//接受传进来的年月日
		Intent inget= this.getIntent();
		lv_todayschedule = (ListView) this.findViewById(R.id.lv_todayschedule);
		final Bundle bu = inget.getExtras();
		buget = bu;
		int syear = bu.getInt("syear");
		int smonth = bu.getInt("smonth");
		int sday = bu.getInt("sday");
		//将年月日设置成string
        String today =syear+"-"+smonth+"-"+sday;
        //将当天所有日程列表显示
        ListTodaySchedule(today);
        //增加按钮
        Button but_addschedule = (Button) this.findViewById(R.id.btn_add);
        //按钮监听器
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
    //显示今日所有日程
	private void ListTodaySchedule(String today) {
		// TODO Auto-generated method stub
		//初始化数据库
		mydbhelper mdh = new mydbhelper(ScheduleActivity.this);
		SQLiteDatabase sdb = mdh.getReadableDatabase();
		//要显示的列
		String[] find =new String[]{"_id","sremindtime","stheme"};
		//查询数据库
		Cursor cur = sdb.query(mydbhelper.SCHEDULE_TABLE_NAME, find, "sdate = ?",new String[]{today}, null, null, null);
		//增加适配器
		SimpleCursorAdapter sca = new SimpleCursorAdapter(ScheduleActivity.this, R.layout.todayscheduleitem, cur, find,new int[]{R.id.tv_todayscheduleitemid,R.id.tv_todayscheduleitemremindtime,R.id.tv_todayscheduleitemremindtheme},0);
		//绑定适配器到列表
		lv_todayschedule.setAdapter(sca);
		AdapterView.OnItemClickListener lvitemselect =new  AdapterView.OnItemClickListener(){
			//特定的元素被选中之后触发的事件
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//将日程主题作为参数传入下一个activity，便于显示日程详细信息
				TextView tv_stheme= (TextView) view.findViewById(R.id.tv_todayscheduleitemremindtheme);
				String stheme =tv_stheme.getText().toString();
				buget.putString("stheme", stheme);
				Intent in = new Intent(ScheduleActivity.this,ScheduleinfoActivity.class);
				in.putExtras(buget);
				startActivity(in);
				//finish();//go
			}

		};
	
		//监听器与列表绑定
		lv_todayschedule.setOnItemClickListener(lvitemselect);
		//关闭数据库
		sdb.close();
	}
	
}
