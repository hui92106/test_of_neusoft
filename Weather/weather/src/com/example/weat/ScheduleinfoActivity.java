package com.example.weat;

import com.example.weather.R;
import com.example.weather.mydbhelper;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ScheduleinfoActivity extends Activity {
	//����ؼ�
	TextView tv_gettheme;
	TextView tv_getarea;
	TextView tv_gettime;
	TextView tv_getisrepeat;
	TextView tv_gettimeinterval;
	TextView tv_getmore;
	Button but_del;
	Button but_edt;
	Bundle buget;
	String curstheme;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scheduleinfo);
		//�õ�������ճ�����
		Intent in = this.getIntent();
		Bundle bu = in.getExtras();
		buget = bu;
		String stheme = bu.getString("stheme");
		curstheme = stheme;
		//��ʾ��Ϣ
		ShowSchedule();
		//ɾ����ť
		but_del = (Button) this.findViewById(R.id.but_del);
		but_del.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(ScheduleinfoActivity.this)
				.setTitle("�Ƿ�ȷ��ɾ����")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						//�����ݿ�
						mydbhelper mdh = new mydbhelper(ScheduleinfoActivity.this);
						SQLiteDatabase sdb = mdh.getReadableDatabase();
						//�õ��ճ�id����Ϊ����id��������֮ǰ������
						Cursor cur = sdb.query(mydbhelper.SCHEDULE_TABLE_NAME, new String[]{"_id"}, "stheme = ?", new String[]{curstheme}, null, null, null);
						int id=0;
						while(cur.moveToNext()){
							id = cur.getInt(0);
						}
						//�ر����ݿ�
						sdb.close();
						//�½�һ�����ӣ�����֮ǰ���ӣ���ɾ����ǰ���ӣ��ﵽȡ�����ӵ�Ч��
						Intent intent = new Intent(ScheduleinfoActivity.this,AlarmshowActivity.class);
						AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
						PendingIntent pi = PendingIntent.getActivity(ScheduleinfoActivity.this, id, intent, 0);
						am.set(AlarmManager.RTC_WAKEUP,0, pi);
						am.cancel(pi);
						DelSchedule();
					}})
					//���ȡ������Ի���ȡ������ִ��ɾ������
					.setNegativeButton("ȡ��", new DialogInterface.OnClickListener(){

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.cancel();
						}}).create().show();
			}
		});
		//�༭��ť
		but_edt = (Button) this.findViewById(R.id.but_edt);
		but_edt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//�����������ת���༭ҳ��
				Intent inset = new Intent(ScheduleinfoActivity.this,EditScheduleActivity.class);
				inset.putExtras(buget);
				startActivity(inset);
				finish();
			}
		});
		Button btn_back=(Button) this.findViewById(R.id.btn_back2);
		btn_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Intent in=new Intent(ScheduleinfoActivity.this,ScheduleActivity.class);
				//startActivity(in);
				finish();	
			}
		});
		
	}
	//ɾ���ճ̺���
	private void DelSchedule() {
		// TODO Auto-generated method stub
		//�����ݿ�
		mydbhelper mdb=new mydbhelper(ScheduleinfoActivity.this);
		SQLiteDatabase sdb=mdb.getReadableDatabase();
		//ִ��ɾ������
		sdb.delete(mydbhelper.SCHEDULE_TABLE_NAME, "stheme=?", new String[]{curstheme});
		//�ر����ݿ�
		sdb.close();
		Intent inset = new Intent(ScheduleinfoActivity.this,ScheduleActivity.class);
		
		//����ѡ�����ڣ���ת�������ճ���ʾ����
		inset.putExtras(buget);
		inset.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(inset);
		ScheduleinfoActivity.this.finish();
	}
	//��ʾѡ���ճ̵ĺ���
	private void ShowSchedule() {
		// TODO Auto-generated method stub
		//������ʾ�ռ�
		tv_gettheme = (TextView) this.findViewById(R.id.tv_gettheme);
		tv_getarea = (TextView) this.findViewById(R.id.tv_getarea);
		tv_gettime = (TextView) this.findViewById(R.id.tv_gettime);
		tv_getisrepeat = (TextView) this.findViewById(R.id.tv_getisrepeat);
		tv_gettimeinterval = (TextView) this.findViewById(R.id.tv_gettimeinterval);
		tv_getmore = (TextView) this.findViewById(R.id.tv_getmore);
		//�����ݿ�
		mydbhelper mdb=new mydbhelper(ScheduleinfoActivity.this);
		final SQLiteDatabase sdb=mdb.getReadableDatabase();
		//���ճ�������Ϊ�ؼ��ֲ�ѯҪ��ʾ���ճ�
		Cursor cur=sdb.query(mydbhelper.SCHEDULE_TABLE_NAME, new String[]{"stheme","sarea","sremindtime","isrepeat","repeatinterval","smore"}, "stheme=?", new String[]{curstheme}, null, null, null);
		//������ѯ������õ�Ҫ��ʾ����Ϣ
		while(cur.moveToNext()){
			//��ʾ������Ϣ
			String theme = cur.getString(0);
			tv_gettheme.setText(theme);
			String area = cur.getString(1);
			tv_getarea.setText(area);
			String retime = cur.getString(2);
			tv_gettime.setText(retime);
			String isrepeat = cur.getString(3);
			//�ж��û��Ƿ��趨���ظ�����
			if(isrepeat.equals("1")){
				tv_getisrepeat.setText("��");
			}
			else {
				tv_getisrepeat.setText("��");
			}
			String timeinterval = cur.getString(4)+" ����";
			tv_gettimeinterval.setText(timeinterval);
			String more =  cur.getString(5);
			tv_getmore.setText(more);
		}//while.
		sdb.close();
	}
}
