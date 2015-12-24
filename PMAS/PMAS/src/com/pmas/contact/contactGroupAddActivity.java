package com.pmas.contact;

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

public class contactGroupAddActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_group_add);

		Button buttonAdd = (Button) this.findViewById(R.id.buttonAdd);
		final mydbhelper dbh = new mydbhelper(contactGroupAddActivity.this);

		buttonAdd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				EditText grname_et = (EditText) contactGroupAddActivity.this
						.findViewById(R.id.editText1);
				EditText grdesc_et = (EditText) contactGroupAddActivity.this
						.findViewById(R.id.editText2);

				String grname = grname_et.getText().toString();
				String grdesc = grdesc_et.getText().toString();

				SQLiteDatabase sdb = dbh.getReadableDatabase();
				ContentValues cv = new ContentValues();
				cv.put("gr_name", grname);
				cv.put("gr_desc", grdesc);
				sdb.insert(mydbhelper.TABLE_NAME_CONTACT_GROUP, null, cv);
				Toast.makeText(contactGroupAddActivity.this, "Ìí¼Ó³É¹¦", 1).show();

				// com.pmas.contact.contactGroupActivity.this.finish();
				contactGroupAddActivity.this.finish();
			}
		});
	}

}
