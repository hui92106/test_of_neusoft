package com.pmas.note;

import com.dh.pmas.R;
import com.dh.dbutils.mydbhelper;

import android.os.Bundle;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class noteTypeAddActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_type_add);

		Button buttonAdd = (Button) this.findViewById(R.id.buttonAdd);
		final mydbhelper dbh = new mydbhelper(noteTypeAddActivity.this);

		buttonAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				EditText ty_name_et = (EditText) noteTypeAddActivity.this
						.findViewById(R.id.editText1);
				EditText ty_desc_et = (EditText) noteTypeAddActivity.this
						.findViewById(R.id.editText2);

				String ty_name = ty_name_et.getText().toString();
				String ty_desc = ty_desc_et.getText().toString();

				SQLiteDatabase sdb = dbh.getReadableDatabase();
				ContentValues cv = new ContentValues();
				cv.put("ty_name", ty_name);
				cv.put("ty_desc", ty_desc);
				sdb.insert(mydbhelper.TABLE_NAME_NOTE_TYPE, null, cv);
				Toast.makeText(noteTypeAddActivity.this, "Ìí¼Ó³É¹¦", 1).show();
				noteTypeAddActivity.this.finish();
			}
		});
	}

}
