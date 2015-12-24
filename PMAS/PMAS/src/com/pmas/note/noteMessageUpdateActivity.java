package com.pmas.note;

import java.sql.Date;
import java.text.SimpleDateFormat;
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

public class noteMessageUpdateActivity extends Activity {

	EditText et1;
	EditText et2;
	Spinner sp;
	ArrayList<String> list;
	mydbhelper mdb;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_message_update);
		Intent in = this.getIntent();
		Bundle bundle = in.getExtras();
		final String sql_id = (String) bundle.get("me_id");
		et1 = (EditText) this.findViewById(R.id.editText1);
		et2 = (EditText) this.findViewById(R.id.editText2);

		// sp= (Spinner)this.findViewById(R.id.spinner1);

		sp = (Spinner) noteMessageUpdateActivity.this
				.findViewById(R.id.spinner1);
		list = new ArrayList<String>();
		mdb = new mydbhelper(noteMessageUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		String[] projecs = new String[] { "_id", "ty_name", "ty_desc" };
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_NOTE_TYPE, projecs, null,
				null, null, null, null);

		findById(sql_id);
		while (cur.moveToNext()) {
			String lv = cur.getString(1);
			if (!list.contains(lv)) {
				list.add(lv);
			}
		}
		adapter = new ArrayAdapter<String>(noteMessageUpdateActivity.this,
				android.R.layout.simple_spinner_item, list);
		sp.setAdapter(adapter);
		Button but1 = (Button) this.findViewById(R.id.buttonAdd);
		but1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String u_et1 = et1.getText().toString();
				String u_et2 = et2.getText().toString();
				String u_sp = sp.getSelectedItem().toString();
				String sid = sql_id;

				SimpleDateFormat formatter = new SimpleDateFormat(
						"yyyy年MM月dd日    HH:mm:ss ");
				Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
				String u_time = formatter.format(curDate);

				updateGroup(u_et1, u_et2, u_time, u_sp, sid);
				Toast.makeText(noteMessageUpdateActivity.this, "修改成功", 1)
						.show();
				noteMessageUpdateActivity.this.finish();
			}
		});

	}

	public void findById(String id) {

		SQLiteDatabase sdb = mdb.getReadableDatabase();
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_NOTE_MESSAGE,
				new String[] { "_id", "me_title", "me_content", "me_type", },
				"_id=?", new String[] { id }, null, null, null);
		while (cur.moveToNext()) {
			String me_title = cur.getString(1);
			et1.setText(me_title);
			String me_content = cur.getString(2);
			et2.setText(me_content);
			String me_type = cur.getString(3);
			list.add(me_type);
		}
	}

	public void updateGroup(String etu1, String etu2, String etu3, String etu4,
			String sid) {
		mydbhelper mdb = new mydbhelper(noteMessageUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("me_title", etu1);
		cv.put("me_content", etu2);
		cv.put("me_time", etu3);
		cv.put("me_type", etu4);
		sdb.update(mydbhelper.TABLE_NAME_NOTE_MESSAGE, cv, "_id=?",
				new String[] { sid });
	}

}
