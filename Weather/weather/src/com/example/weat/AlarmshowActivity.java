package com.example.weat;

import com.example.weather.R;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;
//�趨ʱ�䵽��ʱ��������Ӧ��ʽ���Ի������ʽ
public class AlarmshowActivity extends Activity {
	MediaPlayer mMediaPlayer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarmshow);
		//���������趨
		mMediaPlayer = MediaPlayer.create(this, R.raw.schedule);
		//���ű�������
		mMediaPlayer.start();
		//��ʾ�Ի���
		showDialog();
		
	}
	public void showDialog(){
		Intent in = this.getIntent();
		Bundle bu = in.getExtras();
		//ȡ��������Ӧʱ��ʾ���ճ���
		final String msg = bu.getString("msg");
		//ȡ����Ӧ���ӵ�id���������û��߸�������
		final int amid = bu.getInt("amid");
		new AlertDialog.Builder(AlarmshowActivity.this)
		//�Ի���ı���
		.setTitle("�ճ����ѣ�"+msg)
		//��һ����ť����ͣ���죬���Ϊ5����
		.setPositiveButton("��ͣ����",new DialogInterface.OnClickListener(){

			@SuppressLint("ShowToast")
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(AlarmshowActivity.this, "�����֮���������", 1);
				//�趨��Ӧ��ʱ�䣬�����ڿ�ʼ��5����֮��
				long remindtime =System.currentTimeMillis()+5*60*1000;
				//������ת�Ĳ������ճ�����
				Intent intent = new Intent(AlarmshowActivity.this,AlarmshowActivity.class);
				Bundle buset = new Bundle();
				buset.putString("msg", msg);
				buset.putInt("amid", amid);
				PendingIntent pi = PendingIntent.getActivity(AlarmshowActivity.this, amid, intent, 0);
				//�趨���ӣ�����֮ǰ
				AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
				am.set(AlarmManager.RTC_WAKEUP,remindtime, pi);
				//ֹͣ����
				mMediaPlayer.stop();
				//�رյ�ǰҳ��
				finish();
				
			//ȡ����ť���趨���ظ����ѣ����ǻ������Ӧ����ֻ����һ�Σ������Ӳ�����Ӧ	
			}}).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					//ֹͣ����
					mMediaPlayer.stop();
					//�ر�ҳ��
					finish();
				}
			//ֹͣ���ӣ����ӱ����ǣ�������ʾ����ʹ�趨���ظ�����
			}).setNeutralButton("ֹͣ", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					//�趨һ������ʱ�̵�����
					long remindtime =System.currentTimeMillis()+5*60*1000;
					Intent intent = new Intent(AlarmshowActivity.this,AlarmshowActivity.class);
					//����һ��������Ӧʱ��
					PendingIntent pi = PendingIntent.getActivity(AlarmshowActivity.this, amid, intent, 0);
					//�趨һ������
					AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
					//�趨������Ӧʱ��
					am.set(AlarmManager.RTC_WAKEUP, remindtime, pi);
					//ȡ��������ӣ���ԭ�����ӱ����ǣ���ǰ���ӱ�ȡ���������򲻴��ڣ�ֹͣ���ӵ�Ч���ﵽ
					am.cancel(pi);
					//ֹͣ����
					mMediaPlayer.stop();
					//�رյ�ǰҳ��
					finish();
				}
			}).create().show();
		
	}

}
