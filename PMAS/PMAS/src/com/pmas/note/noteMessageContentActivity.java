package com.pmas.note;

import com.dh.pmas.R;
import com.dh.dbutils.mydbhelper;
import com.pmas.contact.contactPeopleContentActivity;

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

public class noteMessageContentActivity extends Activity {
	TextView tv1;
	TextView tv2;
	TextView tv3;
	TextView tv4;
	String sql_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_message_content);

		tv1 = (TextView) this.findViewById(R.id.tv1);
		tv2 = (TextView) this.findViewById(R.id.tv2);
		tv3 = (TextView) this.findViewById(R.id.tv3);
		tv4 = (TextView) this.findViewById(R.id.tv4);

		Intent in = this.getIntent();
		Bundle bundle = in.getExtras();
		sql_id = (String) bundle.get("me_id");
		findById(sql_id);
	}

	public void findById(String id) {
		mydbhelper mdb = new mydbhelper(noteMessageContentActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_NOTE_MESSAGE,
				new String[] { "_id", "me_title", "me_content", "me_type",
						"me_time" }, "_id=?", new String[] { id }, null, null,
				null);
		while (cur.moveToNext()) {
			String me_title = cur.getString(1);
			tv1.setText(me_title);
			String me_content = cur.getString(2);
			tv2.setText(me_content);
			String me_type = cur.getString(3);
			tv3.setText(me_type);
			String me_time = cur.getString(4);
			tv4.setText(me_time);

		}// while.
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.content, menu);

		MenuItem mi = menu.findItem(R.id.back);
		MenuItem mi1 = menu.findItem(R.id.del);

		// actionbar��õ��¼�֮һ��menu��item����¼���
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
						noteMessageContentActivity.this
								)
						.setTitle("����")
						.setMessage("�Ƿ�ɾ��?")
						.setNegativeButton(
								"��",
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
												noteMessageContentActivity.this,
												"ɾ���ɹ�",
												1)
												.show();
										finish();
									}
								})
						.setNeutralButton("��", null)
						.create().show();

				return false;
			}
		});

		return true;
	}// onCreateOptionsMenu.
	public void delGroup(String sid) {
		mydbhelper mdb = new mydbhelper(noteMessageContentActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		sdb.delete(mydbhelper.TABLE_NAME_NOTE_MESSAGE, "_id=?",
				new String[] { sid });

	}
}
