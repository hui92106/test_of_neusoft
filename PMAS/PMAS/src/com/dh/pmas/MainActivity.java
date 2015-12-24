package com.dh.pmas;

import com.pmas.contact.contactActivity;
import com.pmas.daylife.daylifeActivity;
import com.pmas.note.noteActivity;
import com.pmas.setting.settingActivity;

import android.os.Bundle;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class MainActivity extends TabActivity {
	private RadioGroup group;
	private TabHost tabHost;
	public static final String TAB_MAIN = "main";
	public static final String TAB_CONTACT = "contact";
	public static final String TAB_NOTE = "note";
	public static final String TAB_DAYLIFE = "daylife";
	public static final String TAB_SETTING = "setting";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		group = (RadioGroup) findViewById(R.id.main_radio);
		tabHost = getTabHost();
		tabHost.addTab(tabHost.newTabSpec(TAB_MAIN).setIndicator(TAB_MAIN)
				.setContent(new Intent(this, welActivity.class)));// go
		tabHost.addTab(tabHost.newTabSpec(TAB_CONTACT)
				.setIndicator(TAB_CONTACT)
				.setContent(new Intent(this, contactActivity.class)));// go
		tabHost.addTab(tabHost.newTabSpec(TAB_NOTE).setIndicator(TAB_NOTE)
				.setContent(new Intent(this, noteActivity.class)));// go
		tabHost.addTab(tabHost.newTabSpec(TAB_DAYLIFE)
				.setIndicator(TAB_DAYLIFE)
				.setContent(new Intent(this, daylifeActivity.class)));// go
		tabHost.addTab(tabHost.newTabSpec(TAB_SETTING)
				.setIndicator(TAB_SETTING)
				.setContent(new Intent(this, settingActivity.class)));// go

		group.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.radio_button0:
					tabHost.setCurrentTabByTag(TAB_CONTACT);
					break;
				case R.id.radio_button1:
					tabHost.setCurrentTabByTag(TAB_NOTE);
					break;
				case R.id.radio_button2:
					tabHost.setCurrentTabByTag(TAB_DAYLIFE);
					break;
				case R.id.radio_button3:
					tabHost.setCurrentTabByTag(TAB_SETTING);
					break;

				default:
					break;
				}
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		MenuItem mi = menu.findItem(R.id.action_settings);
		// actionbar最常用的事件之一。menu的item点击事件。
		mi.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				// add();
				finish();
				return false;
			}
		});

		return true;
	}

}
