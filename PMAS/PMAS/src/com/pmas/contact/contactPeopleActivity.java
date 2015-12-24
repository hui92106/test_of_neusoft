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
import android.view.View;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class contactPeopleActivity extends Activity implements
		SearchView.OnQueryTextListener {

	ListView lv;
	SearchView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_people);

		Button but1 = (Button) this.findViewById(R.id.button1);
		sv = (SearchView) contactPeopleActivity.this
				.findViewById(R.id.searchView1);
		but1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(contactPeopleActivity.this,
						contactPeopleAddActivity.class);
				startActivity(in);
			}
		});
		lv = (ListView) this.findViewById(R.id.listView1);
		findall();
		lv.setOnItemClickListener(loi);
		lv.setOnItemLongClickListener(lol);
		lv.setTextFilterEnabled(true);
		sv.setOnQueryTextListener(this);
		sv.setSubmitButtonEnabled(true);
	}

	AdapterView.OnItemLongClickListener lol = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			new AlertDialog.Builder(contactPeopleActivity.this.getParent())
					.setItems(new String[] { "修改", "取消" },
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									if (which == 0) {
										TextView t_id = (TextView) arg1
												.findViewById(R.id.tv1);
										String sql_id = t_id.getText()
												.toString();
										Bundle b = new Bundle();
										b.putString("pe_id", sql_id);
										Intent in = new Intent();
										in.setClass(
												contactPeopleActivity.this,
												contactPeopleUpdateActivity.class);
										in.putExtras(b);
										startActivity(in);

									}
								}
							}).show();
			return false;
		}
	};

	AdapterView.OnItemClickListener loi = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int pos, long id) {
			// TODO Auto-generated method stub
			TextView gr_id = (TextView) view.findViewById(R.id.tv1);
			String sql_id = gr_id.getText().toString();

			Bundle b = new Bundle();
			b.putString("gr_id", sql_id);
			Intent in = new Intent();
			in.setClass(contactPeopleActivity.this,
					contactPeopleContentActivity.class);
			in.putExtras(b);
			startActivity(in);

			// contactGroupActivity.this.finish();
		}
	};

	public void delGroup(String sid) {
		mydbhelper mdb = new mydbhelper(contactPeopleActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		sdb.delete(mydbhelper.TABLE_NAME_CONTACT_PEOPLE, "_id=?",
				new String[] { sid });

	}

	public void findquery(String str) {
		mydbhelper dbh = new mydbhelper(contactPeopleActivity.this);
		SQLiteDatabase sdb = dbh.getReadableDatabase();
		String[] projecs = new String[] { "_id", "pe_name", "pe_birthday",
				"pe_phone", "pe_email", "pe_address" };
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_CONTACT_PEOPLE, projecs,
				"pe_name=?", new String[] { str }, null, null, null);
		SimpleCursorAdapter sca = new SimpleCursorAdapter(
				contactPeopleActivity.this, R.layout.group_list, cur,
				new String[] { "_id", "pe_name", "pe_phone" }, new int[] {
						R.id.tv1, R.id.tv2, R.id.tv3 }, 0);
		lv.setAdapter(sca);

	}

	// 查询列表。
	private void findall() {
		mydbhelper dbh = new mydbhelper(contactPeopleActivity.this);
		SQLiteDatabase sdb = dbh.getReadableDatabase();
		String[] projecs = new String[] { "_id", "pe_name", "pe_birthday",
				"pe_phone", "pe_email", "pe_address" };
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_CONTACT_PEOPLE, projecs,
				null, null, null, null, null);
		SimpleCursorAdapter sca = new SimpleCursorAdapter(
				contactPeopleActivity.this, R.layout.group_list, cur,
				new String[] { "_id", "pe_name", "pe_phone" }, new int[] {
						R.id.tv1, R.id.tv2, R.id.tv3 }, 0);
		lv.setAdapter(sca);

	}// findall.

	// 表示哪一个菜单被选中了。
	// 参数item表示的是选中项，

	@Override
	public boolean onQueryTextChange(String newText) {

		/*
		 * if (TextUtils.isEmpty(newText)) { // Clear the text filter.
		 * lv.clearTextFilter(); } else { // Sets the initial value for the text
		 * filter. lv.setFilterText(newText.toString()); }
		 */

		return false;

	}

	@Override
	public boolean onQueryTextSubmit(String arg0) {
		// TODO Auto-generated method stub
		findquery(arg0);
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.group_main, menu);

		MenuItem mi = menu.findItem(R.id.menu_fol);
		MenuItem mi1 = menu.findItem(R.id.action_settings);

		// actionbar最常用的事件之一。menu的item点击事件。
		mi.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				// add();
				findall();
				return false;
			}
		});
		mi1.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem arg0) {
				// TODO Auto-generated method stub
				// add();
				finish();
				return false;
			}
		});

		return true;
	}// onCreateOptionsMenu.

}