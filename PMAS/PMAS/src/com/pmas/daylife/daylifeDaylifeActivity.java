package com.pmas.daylife;

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

import android.widget.ListView;

import android.widget.Button;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class daylifeDaylifeActivity extends Activity implements
		SearchView.OnQueryTextListener {

	ListView lv;
	SearchView sv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daylife_daylife);

		Button but1 = (Button) this.findViewById(R.id.button1);
		sv = (SearchView) daylifeDaylifeActivity.this
				.findViewById(R.id.searchView1);
		but1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(daylifeDaylifeActivity.this,
						daylifeDaylifeAddActivity.class);
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
			new AlertDialog.Builder(daylifeDaylifeActivity.this.getParent())
					.setItems(new String[] { "修改","取消" },
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
										b.putString("da_id", sql_id);
										Intent in = new Intent();
										in.setClass(
												daylifeDaylifeActivity.this,
												daylifeDaylifeUpdateActivity.class);
										in.putExtras(b);
										startActivity(in);
									} /*else if (which == 1) {

										new AlertDialog.Builder(
												daylifeDaylifeActivity.this
														.getParent())
												.setTitle("警告")
												.setMessage("是否删除")
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
																TextView t_id = (TextView) arg1
																		.findViewById(R.id.tv1);
																String sql_id = t_id
																		.getText()
																		.toString();
																delGroup(sql_id);

																Toast.makeText(
																		daylifeDaylifeActivity.this,
																		"删除成功",
																		1)
																		.show();
															}
														})
												.setNeutralButton("否", null)
												.create().show();

									}*/

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
			b.putString("da_id", sql_id);
			Intent in = new Intent();
			in.setClass(daylifeDaylifeActivity.this,
					daylifeDaylifeContentActivity.class);
			in.putExtras(b);
			startActivity(in);

			// contactGroupActivity.this.finish();
		}
	};

	public void delGroup(String sid) {
		mydbhelper mdb = new mydbhelper(daylifeDaylifeActivity.this);
		SQLiteDatabase sdb = mdb.getReadableDatabase();
		sdb.delete(mydbhelper.TABLE_NAME_DAYLIFE_DAYLIFE, "_id=?",
				new String[] { sid });
	}

	public void findquery(String str) {
		mydbhelper dbh = new mydbhelper(daylifeDaylifeActivity.this);
		SQLiteDatabase sdb = dbh.getReadableDatabase();
		String[] projecs = new String[] { "_id", "da_title", "da_content",
				"da_time" };
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_DAYLIFE_DAYLIFE, projecs,
				"da_title=?", new String[] { str }, null, null, null);
		SimpleCursorAdapter sca = new SimpleCursorAdapter(
				daylifeDaylifeActivity.this, R.layout.group_list3, cur,
				new String[] { "_id", "da_title", "da_content", "da_time" },
				new int[] { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4 }, 0);
		lv.setAdapter(sca);

	}

	// 查询列表。
	private void findall() {
		mydbhelper dbh = new mydbhelper(daylifeDaylifeActivity.this);
		SQLiteDatabase sdb = dbh.getReadableDatabase();
		String[] projecs = new String[] { "_id", "da_title", "da_content",
				"da_time" };
		Cursor cur = sdb.query(mydbhelper.TABLE_NAME_DAYLIFE_DAYLIFE, projecs,
				null, null, null, null, null);
		SimpleCursorAdapter sca = new SimpleCursorAdapter(
				daylifeDaylifeActivity.this, R.layout.group_list3, cur,
				new String[] { "_id", "da_title", "da_content", "da_time" },
				new int[] { R.id.tv1, R.id.tv2, R.id.tv3, R.id.tv4 }, 0);
		lv.setAdapter(sca);
	}// findall.

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