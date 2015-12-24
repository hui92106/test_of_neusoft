package com.example.weather;

import com.calendar.demo.CalendarActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends TabActivity {
	private RadioGroup group;
	private TabHost tabHost;
	public static final String TAB_HOME = "tabHome";
	public static final String TAB_MES = "tabMes";
	public static final String TAB_TOUCH = "tab_touch";
	public static final String TAB_MORE = "tab_more";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.maintabs);
		group = (RadioGroup) findViewById(R.id.main_radio);
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_HOME).setIndicator(TAB_HOME)
				.setContent(new Intent(this, weather.class)));//go
		tabHost.addTab(tabHost.newTabSpec(TAB_MES).setIndicator(TAB_MES)
				.setContent(new Intent(this, CalendarActivity.class)));//go
		tabHost.addTab(tabHost.newTabSpec(TAB_TOUCH).setIndicator(TAB_TOUCH)
				.setContent(new Intent(this, ReminderActivity.class)));//go
		tabHost.addTab(tabHost.newTabSpec(TAB_MORE).setIndicator(TAB_MORE)
				.setContent(new Intent(this, MoerActivity.class)));//go
		
		
		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_weather:
					tabHost.setCurrentTabByTag(TAB_HOME);
					break;
				case R.id.radio_calendar:
					tabHost.setCurrentTabByTag(TAB_MES);
					break;
				case R.id.radio_reminderlife:
					tabHost.setCurrentTabByTag(TAB_TOUCH);
					break;
				case R.id.radio_more:
					tabHost.setCurrentTabByTag(TAB_MORE);
					break;

				default:
					break;
				}
			}
		});
	}//oncreate.
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		//用于解决activity中有ViewFlipper不响应
		getCurrentActivity().onTouchEvent(event);
		return super.onTouchEvent(event);
		}
}
