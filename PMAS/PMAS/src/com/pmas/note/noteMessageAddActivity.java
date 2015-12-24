package com.pmas.note;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.dh.pmas.R;
import com.dh.dbutils.mydbhelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class noteMessageAddActivity extends Activity {
	Dialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_message_add);

		Button buttonAdd = (Button) this.findViewById(R.id.buttonAdd);
		final mydbhelper dbh = new mydbhelper(noteMessageAddActivity.this);
		final SQLiteDatabase sdb = dbh.getReadableDatabase();
		final Spinner sp = (Spinner) noteMessageAddActivity.this
				.findViewById(R.id.spinner1);
		ArrayList<String> list = new ArrayList<String>();
		list.add("");
		String[] projecs = new String[] { "_id", "ty_name", "ty_desc" };
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_NOTE_TYPE, projecs, null,
				null, null, null, null);

		while (cur.moveToNext()) {
			String lv = cur.getString(1);
			list.add(lv);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				noteMessageAddActivity.this,
				android.R.layout.simple_spinner_item, list);

		sp.setAdapter(adapter);

		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy年MM月dd日    HH:mm:ss ");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		final String date = formatter.format(curDate);

		buttonAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				EditText me_title_et = (EditText) noteMessageAddActivity.this
						.findViewById(R.id.editText1);
				EditText me_content_et = (EditText) noteMessageAddActivity.this
						.findViewById(R.id.editText2);

				String me_title = me_title_et.getText().toString();
				String me_content = me_content_et.getText().toString();

				String sp1 = sp.getSelectedItem().toString();
				ContentValues cv = new ContentValues();

				cv.put("me_title", me_title);
				cv.put("me_content", me_content);
				cv.put("me_type", sp1);
				cv.put("me_time", date);
				sdb.insert(mydbhelper.TABLE_NAME_NOTE_MESSAGE, null, cv);
				Toast.makeText(noteMessageAddActivity.this, "添加成功", 1).show();
				noteMessageAddActivity.this.finish();
			}
		});
	}

}