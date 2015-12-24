package com.pmas.contact;

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

public class contactPeopleAddActivity extends Activity {

	Spinner sp;
	ArrayList<String> list;
	ArrayAdapter<String> adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_people_add);

		Button buttonAdd = (Button) this.findViewById(R.id.buttonAdd);
		final mydbhelper dbh = new mydbhelper(contactPeopleAddActivity.this);
		final SQLiteDatabase sdb = dbh.getReadableDatabase();
		sp = (Spinner) contactPeopleAddActivity.this
				.findViewById(R.id.spinner1);
		list = new ArrayList<String>();

		String[] projecs = new String[] { "_id", "gr_name", "gr_desc" };
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_CONTACT_GROUP, projecs,
				null, null, null, null, null);
		list.add("");

		while (cur.moveToNext()) {
			String lv = cur.getString(1);
			list.add(lv);
		}
		adapter = new ArrayAdapter<String>(contactPeopleAddActivity.this,
				android.R.layout.simple_spinner_item, list);

		sp.setAdapter(adapter);

		buttonAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				EditText pe_name_et = (EditText) contactPeopleAddActivity.this
						.findViewById(R.id.editText1);
				EditText pe_birthday_et = (EditText) contactPeopleAddActivity.this
						.findViewById(R.id.editText2);
				EditText pe_phone_et = (EditText) contactPeopleAddActivity.this
						.findViewById(R.id.editText3);
				EditText pe_email_et = (EditText) contactPeopleAddActivity.this
						.findViewById(R.id.editText4);
				EditText pe_address_et = (EditText) contactPeopleAddActivity.this
						.findViewById(R.id.editText5);

				String pe_name = pe_name_et.getText().toString();
				String pe_birthday = pe_birthday_et.getText().toString();
				String pe_phone = pe_phone_et.getText().toString();
				String pe_email = pe_email_et.getText().toString();
				String pe_address = pe_address_et.getText().toString();

				String sp1 = sp.getSelectedItem().toString();

				ContentValues cv = new ContentValues();
				cv.put("pe_name", pe_name);
				cv.put("pe_birthday", pe_birthday);
				cv.put("pe_phone", pe_phone);
				cv.put("pe_email", pe_email);
				cv.put("pe_address", pe_address);
				cv.put("pe_group", sp1);
				sdb.insert(mydbhelper.TABLE_NAME_CONTACT_PEOPLE, null, cv);
				Toast.makeText(contactPeopleAddActivity.this, "Ìí¼Ó³É¹¦", 1).show();
				contactPeopleAddActivity.this.finish();
			}
		});
	}

}