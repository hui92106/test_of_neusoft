package com.pmas.daylife;

import com.dh.pmas.R;
import com.dh.dbutils.mydbhelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputFilter.LengthFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class daylifeTypeUpdateActivity extends Activity {

	EditText et_ty_name;
	EditText et_ty_desc;
	Button but1;
	Button but2;
	String sid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daylife_type_update);
		Intent in = this.getIntent();
		Bundle bundle = in.getExtras();
		final String sql_id = (String) bundle.get("ty_id");
		et_ty_name = (EditText) this.findViewById(R.id.editText1);
		et_ty_desc = (EditText) this.findViewById(R.id.editText2);
		but1 = (Button) this.findViewById(R.id.buttonUpdate);
		but2 = (Button) this.findViewById(R.id.buttonDel);
		findById(sql_id);

		but1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String uet_gr_name = et_ty_name.getText().toString();
				String uet_gr_desc = et_ty_desc.getText().toString();
				String sid = sql_id;
				updateGroup(uet_gr_name, uet_gr_desc, sid);
				Toast.makeText(daylifeTypeUpdateActivity.this, "修改成功",1)
						.show();
				daylifeTypeUpdateActivity.this.finish();
			}

		});
		but2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sid = sql_id;
				delGroup(sql_id);
				Toast.makeText(daylifeTypeUpdateActivity.this, "删除成功", 1)
						.show();
				daylifeTypeUpdateActivity.this.finish();
			}
		});

	}// onCreate.

	// 修改功能分成两步来进行，第一步，按id查询数据库中固定的一条记录。
	// 第二步，调用修改方法对更新后结果修改。
	public void findById(String id) {
		mydbhelper mdb = new mydbhelper(daylifeTypeUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_DAYLIFE_TYPE,
				new String[] { "_id", "ty_name", "ty_desc" }, "_id=?",
				new String[] { id }, null, null, null);
		while (cur.moveToNext()) {
			String ty_name = cur.getString(1);
			et_ty_name.setText(ty_name);
			String ty_gdesc = cur.getString(2);
			et_ty_desc.setText(ty_gdesc);
		}// while.
	}// findById.

	public void updateGroup(String uup_tyname, String uup_tydesc, String sid) {
		mydbhelper mdb = new mydbhelper(daylifeTypeUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("ty_name", uup_tyname);
		cv.put("ty_desc", uup_tydesc);
		sdb.update(mydbhelper.TABLE_NAME_DAYLIFE_TYPE, cv, "_id=?",
				new String[] { sid });
	}

	public void delGroup(String sid) {
		mydbhelper mdb = new mydbhelper(daylifeTypeUpdateActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		sdb.delete(mydbhelper.TABLE_NAME_DAYLIFE_TYPE, "_id=?",
				new String[] { sid });
		finish();
	}

}
