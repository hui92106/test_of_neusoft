package com.pmas.contact;

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

public class contactPeopleUpdateActivity extends Activity {

	EditText et1;
	EditText et2;
	EditText et3;
	EditText et4;
	EditText et5;
	Spinner sp;
	ArrayList<String> list;
	mydbhelper mdb;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_people_update);
		Intent in = this.getIntent();
		Bundle bundle = in.getExtras();
		final String sql_id = (String) bundle.get("pe_id");
		et1 = (EditText) this.findViewById(R.id.editText1);
		et2 = (EditText) this.findViewById(R.id.editText2);
		et3 = (EditText) this.findViewById(R.id.editText3);
		et4 = (EditText) this.findViewById(R.id.editText4);
		et5 = (EditText) this.findViewById(R.id.editText5);
		// sp= (Spinner)this.findViewById(R.id.spinner1);

		sp = (Spinner) contactPeopleUpdateActivity.this
				.findViewById(R.id.spinner1);
		list = new ArrayList<String>();
		mdb = new mydbhelper(contactPeopleUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		String[] projecs = new String[] { "_id", "gr_name", "gr_desc" };
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_CONTACT_GROUP, projecs,
				null, null, null, null, null);

		findById(sql_id);
		while (cur.moveToNext()) {
			String lv = cur.getString(1);
			if (!list.contains(lv)) {
				list.add(lv);
			}
		}
		adapter = new ArrayAdapter<String>(contactPeopleUpdateActivity.this,
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
				String u_et4 = et4.getText().toString();
				String u_et5 = et5.getText().toString();
				String u_sp = sp.getSelectedItem().toString();
				String sid = sql_id;
				updateGroup(u_et1, u_et2, u_et3, u_et4, u_et5, u_sp, sid);
				Toast.makeText(contactPeopleUpdateActivity.this, "ÐÞ¸Ä³É¹¦", 1)
						.show();
				contactPeopleUpdateActivity.this.finish();
			}
		});

	}

	public void findById(String id) {

		SQLiteDatabase sdb = mdb.getReadableDatabase();
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_CONTACT_PEOPLE,
				new String[] { "_id", "pe_name", "pe_birthday", "pe_phone",
						"pe_email", "pe_address", "pe_group" }, "_id=?",
				new String[] { id }, null, null, null);
		while (cur.moveToNext()) {
			String pe_name = cur.getString(1);
			et1.setText(pe_name);
			String pe_birthday = cur.getString(2);
			et2.setText(pe_birthday);
			String pe_phone = cur.getString(3);
			et3.setText(pe_phone);
			String pe_email = cur.getString(4);
			et4.setText(pe_email);
			String pe_address = cur.getString(5);
			et5.setText(pe_address);
			String pe_group = cur.getString(6);
			list.add(pe_group);
		}
	}

	public void updateGroup(String etu1, String etu2, String etu3, String etu4,
			String etu5, String etu6, String sid) {
		mydbhelper mdb = new mydbhelper(contactPeopleUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("pe_name", etu1);
		cv.put("pe_birthday", etu2);
		cv.put("pe_phone", etu3);
		cv.put("pe_email", etu4);
		cv.put("pe_address", etu5);
		cv.put("pe_group", etu6);
		sdb.update(mydbhelper.TABLE_NAME_CONTACT_PEOPLE, cv, "_id=?",
				new String[] { sid });
	}

}
