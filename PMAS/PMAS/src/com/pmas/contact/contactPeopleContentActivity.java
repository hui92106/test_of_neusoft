package com.pmas.contact;

import com.dh.pmas.R;
import com.dh.dbutils.mydbhelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class contactPeopleContentActivity extends Activity {
	TextView tv1;
	TextView tv2;
	TextView tv3;
	TextView tv4;
	TextView tv5;
	TextView tv6;
	String sql_id;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_people_content);

		tv1 = (TextView) this.findViewById(R.id.tv1);
		tv2 = (TextView) this.findViewById(R.id.tv2);
		tv3 = (TextView) this.findViewById(R.id.tv3);
		tv4 = (TextView) this.findViewById(R.id.tv4);
		tv5 = (TextView) this.findViewById(R.id.tv5);
		tv6 = (TextView) this.findViewById(R.id.tv6);

		Intent in = this.getIntent();
		Bundle bundle = in.getExtras();
		sql_id = (String) bundle.get("gr_id");
		findById(sql_id);
	}

	public void findById(String id) {
		mydbhelper mdb = new mydbhelper(contactPeopleContentActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_CONTACT_PEOPLE,
				new String[] { "_id", "pe_name", "pe_birthday", "pe_phone",
						"pe_email", "pe_address", "pe_group" }, "_id=?",
				new String[] { id }, null, null, null);
		while (cur.moveToNext()) {
			String pe_name = cur.getString(1);
			tv1.setText(pe_name);
			String pe_birthday = cur.getString(2);
			tv2.setText(pe_birthday);
			String pe_phone = cur.getString(3);
			tv3.setText(pe_phone);
			String pe_email = cur.getString(4);
			tv4.setText(pe_email);
			String pe_address = cur.getString(5);
			tv5.setText(pe_address);
			String pe_group = cur.getString(6);
			tv6.setText(pe_group);

		}// while.
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.content, menu);

		MenuItem mi = menu.findItem(R.id.back);
		MenuItem mi1 = menu.findItem(R.id.del);

		// actionbar最常用的事件之一。menu的item点击事件。
		mi.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
			finish();
				return false;
			}
		});
		mi1.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(
						contactPeopleContentActivity.this
								)
						.setTitle("警告")
						.setMessage("是否删除?")
						.setNegativeButton(
								"是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(
											DialogInterface dialog,
											int which) {
										// TODO
										// Auto-generated
										// method stub
										delGroup(sql_id);

										Toast.makeText(
												contactPeopleContentActivity.this,
												"删除成功",
												1)
												.show();
										finish();
									}
								})
						.setNeutralButton("否", null)
						.create().show();

				return false;
			}
		});

		return true;
	}// onCreateOptionsMenu.
	public void delGroup(String sid) {
		mydbhelper mdb = new mydbhelper(contactPeopleContentActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		sdb.delete(mydbhelper.TABLE_NAME_CONTACT_PEOPLE, "_id=?",
				new String[] { sid });

	}
}
