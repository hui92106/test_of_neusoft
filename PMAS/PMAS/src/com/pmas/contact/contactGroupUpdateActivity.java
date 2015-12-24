package com.pmas.contact;

import com.dh.pmas.R;
import com.dh.dbutils.mydbhelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class contactGroupUpdateActivity extends Activity {

	EditText et_gr_name;
	EditText et_gr_desc;
	Button but1;
	Button but2;
	String sid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_group_update);
		Intent in = this.getIntent();
		Bundle bundle = in.getExtras();
		final String sql_id = (String) bundle.get("gr_id");
		et_gr_name = (EditText) this.findViewById(R.id.editText1);
		et_gr_desc = (EditText) this.findViewById(R.id.editText2);
		but1 = (Button) this.findViewById(R.id.buttonUpdate);
		but2 = (Button) this.findViewById(R.id.buttonDel);
		findById(sql_id);

		but1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String uet_gr_name = et_gr_name.getText().toString();
				String uet_gr_desc = et_gr_desc.getText().toString();
				String sid = sql_id;
				updateGroup(uet_gr_name, uet_gr_desc, sid);

				Toast.makeText(contactGroupUpdateActivity.this, "修改成功", 1)
						.show();
				contactGroupUpdateActivity.this.finish();
			}

		});
		but2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(contactGroupUpdateActivity.this)
						.setTitle("警告")
						.setMessage("是否删除")
						.setNegativeButton("是",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										sid = sql_id;
										delGroup(sql_id);
										Toast.makeText(
												contactGroupUpdateActivity.this,
												"删除成功", 1).show();
									}
								}).setNeutralButton("否", null).create().show();

			}
		});

	}// onCreate.

	// 修改功能分成两步来进行，第一步，按id查询数据库中固定的一条记录。
	// 第二步，调用修改方法对更新后结果修改。
	public void findById(String id) {
		mydbhelper mdb = new mydbhelper(contactGroupUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_CONTACT_GROUP,
				new String[] { "_id", "gr_name", "gr_desc" }, "_id=?",
				new String[] { id }, null, null, null);
		while (cur.moveToNext()) {
			String gname = cur.getString(1);
			et_gr_name.setText(gname);
			String gdesc = cur.getString(2);
			et_gr_desc.setText(gdesc);
		}// while.
	}// findById.

	public void updateGroup(String uet_ud_gname, String uet_ud_gdesc, String sid) {
		mydbhelper mdb = new mydbhelper(contactGroupUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("gr_name", uet_ud_gname);
		cv.put("gr_desc", uet_ud_gdesc);
		sdb.update(mydbhelper.TABLE_NAME_CONTACT_GROUP, cv, "_id=?",
				new String[] { sid });
	}

	public void delGroup(String sid) {
		mydbhelper mdb = new mydbhelper(contactGroupUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		sdb.delete(mydbhelper.TABLE_NAME_CONTACT_GROUP, "_id=?",
				new String[] { sid });
		finish();
	}

}
