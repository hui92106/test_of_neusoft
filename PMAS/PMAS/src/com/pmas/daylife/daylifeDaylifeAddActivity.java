package com.pmas.daylife;

import java.util.ArrayList;

import com.dh.pmas.R;
import com.dh.dbutils.mydbhelper;

import android.os.Bundle;
import android.app.Activity;
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

public class daylifeDaylifeAddActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daylife_daylife_add);

		Button buttonAdd = (Button) this.findViewById(R.id.buttonAdd);
		final mydbhelper dbh = new mydbhelper(daylifeDaylifeAddActivity.this);
		final SQLiteDatabase sdb = dbh.getReadableDatabase();
		final Spinner sp = (Spinner) daylifeDaylifeAddActivity.this
				.findViewById(R.id.spinner1);
		ArrayList<String> list = new ArrayList<String>();

		String[] projecs = new String[] { "_id", "ty_name", "ty_desc" };
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_DAYLIFE_TYPE, projecs,
				null, null, null, null, null);
		list.add("");

		while (cur.moveToNext()) {
			String lv = cur.getString(1);
			list.add(lv);
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				daylifeDaylifeAddActivity.this,
				android.R.layout.simple_spinner_item, list);

		sp.setAdapter(adapter);

		buttonAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				EditText da_title_et = (EditText) daylifeDaylifeAddActivity.this
						.findViewById(R.id.editText1);
				EditText da_content_et = (EditText) daylifeDaylifeAddActivity.this
						.findViewById(R.id.editText2);
				EditText da_time_et = (EditText) daylifeDaylifeAddActivity.this
						.findViewById(R.id.editText3);

				String da_title = da_title_et.getText().toString();
				String da_content = da_content_et.getText().toString();
				String da_time = da_time_et.getText().toString();

				String sp1 = sp.getSelectedItem().toString();
				ContentValues cv = new ContentValues();
				cv.put("da_title", da_title);
				cv.put("da_content", da_content);
				cv.put("da_time", da_time);
				cv.put("da_type", sp1);
				sdb.insert(mydbhelper.TABLE_NAME_DAYLIFE_DAYLIFE, null, cv);
				Toast.makeText(daylifeDaylifeAddActivity.this, "Ìí¼Ó³É¹¦", 1).show();
				daylifeDaylifeAddActivity.this.finish();
			}
		});
	}

}