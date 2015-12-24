package com.pmas.daylife;

import java.util.ArrayList;

import com.dh.pmas.R;
import com.dh.dbutils.mydbhelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class daylifeDaylifeUpdateActivity extends Activity {

	EditText et1;
	EditText et2;
	EditText et3;
	Spinner sp;
	ArrayList<String> list;
	mydbhelper mdb;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daylife_daylife_update);
		Intent in = this.getIntent();
		Bundle bundle = in.getExtras();
		final String sql_id = (String) bundle.get("da_id");
		et1 = (EditText) this.findViewById(R.id.editText1);
		et2 = (EditText) this.findViewById(R.id.editText2);
		et3 = (EditText) this.findViewById(R.id.editText3);
		// sp= (Spinner)this.findViewById(R.id.spinner1);

		sp = (Spinner) daylifeDaylifeUpdateActivity.this
				.findViewById(R.id.spinner1);
		list = new ArrayList<String>();
		mdb = new mydbhelper(daylifeDaylifeUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		String[] projecs = new String[] { "_id", "ty_name", "ty_desc" };
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_DAYLIFE_TYPE, projecs,
				null, null, null, null, null);

		findById(sql_id);
		while (cur.moveToNext()) {
			String lv = cur.getString(1);
			if (!list.contains(lv)) {
				list.add(lv);
			}
		}
		adapter = new ArrayAdapter<String>(daylifeDaylifeUpdateActivity.this,
				android.R.layout.simple_spinner_item, list);
		sp.setAdapter(adapter);
		Button but1 = (Button) this.findViewById(R.id.buttonAdd);
		but1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String u_et1 = et1.getText().toString();
				String u_et2 = et2.getText().toString();
				String u_et3 = et3.getText().toString();
				String u_sp = sp.getSelectedItem().toString();
				String sid = sql_id;
				updateGroup(u_et1, u_et2, u_et3, u_sp, sid);
				Toast.makeText(daylifeDaylifeUpdateActivity.this, "ÐÞ¸Ä³É¹¦", 1).show();
				daylifeDaylifeUpdateActivity.this.finish();
			}
		});

	}

	public void findById(String id) {

		SQLiteDatabase sdb = mdb.getReadableDatabase();
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_DAYLIFE_DAYLIFE,
				new String[] { "_id", "da_title", "da_content", "da_time",
						"da_type" }, "_id=?", new String[] { id }, null, null,
				null);
		while (cur.moveToNext()) {
			String da_title = cur.getString(1);
			et1.setText(da_title);
			String da_content = cur.getString(2);
			et2.setText(da_content);
			String da_time = cur.getString(3);
			et3.setText(da_time);
			String da_type = cur.getString(4);
			list.add(da_type);
		}
	}

	public void updateGroup(String etu1, String etu2, String etu3, String etu4,
			String sid) {
		mydbhelper mdb = new mydbhelper(daylifeDaylifeUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("da_title", etu1);
		cv.put("da_content", etu2);
		cv.put("da_time", etu3);
		cv.put("da_type", etu4);
		sdb.update(mydbhelper.TABLE_NAME_DAYLIFE_DAYLIFE, cv, "_id=?",
				new String[] { sid });
	}

}
