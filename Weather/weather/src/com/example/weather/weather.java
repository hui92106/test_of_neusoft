package com.example.weather;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.telephony.SmsManager;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;
public class weather extends Activity implements OnGestureListener {
	
	private long mExitTime;
	
	String msggmainfo;
	List<CWeather> clist;
	String citycode;
	int i=0;

	ViewFlipper flipper;
	GestureDetector detector;
	mydbhelper dbh;
	SQLiteDatabase sdb;
	Cursor cur;
	Cursor cursor;
	
	TextView tv_citynm;
	TextView tv_temp;
	TextView tv_weath;
	TextView tv_w_dir;
	TextView tv_w_pow;
	TextView tv_day;
	TextView tv_week;
	
	TextView tv_today;
	TextView tv_today_temp;
	TextView tv_today_weather;
	TextView tv_tomo;
	TextView tv_tomo_temp;
	TextView tv_tomo_weather;
	TextView tv_daftomo;
	TextView tv_daftomo_temp;
	TextView tv_daftomo_weather;
	
	String cw_citycode;
	String cw_cityname;
	String cw_temp;
	String cw_weather;
	String cw_wind_direction;
	String cw_wind_power;
	String cw_days;
	String cw_weeks;   //today.
	String cw_tomoweek;
	String cw_tomotemp;
	String cw_tomoweath;
	String cw_daftomoweek;
	String cw_daftomotemp;
	String cw_daftomoweath;
		
	Button btn_ref;
	Button btn_add;
	
	String usernumber;
	TextView tv;
	Button bt1;
	Button btn_share;
	EditText et;
	
	
	RelativeLayout lat;
	ImageView imgv1;
	ImageView imgv2;
	ImageView imgv3;
	

	int total = 0;
	int current = 1;
	
    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
          setContentView(R.layout.main);
  		
          tv_citynm=(TextView) this.findViewById(R.id.tv_cityname);
          tv_temp=(TextView) this.findViewById(R.id.tv_temperature);
          tv_weath=(TextView) this.findViewById(R.id.textView1);
          tv_w_dir=(TextView) this.findViewById(R.id.tv_wind_direction);
          tv_w_pow=(TextView) this.findViewById(R.id.tv_wind_power);
          tv_day=(TextView) this.findViewById(R.id.tv_days);
          tv_week=(TextView) this.findViewById(R.id.tv_week);
          
          tv_today=(TextView) this.findViewById(R.id.tv_today);
          tv_today_temp=(TextView) this.findViewById(R.id.tv_today_tem);
          tv_today_weather=(TextView) this.findViewById(R.id.tv_today_weather);
          tv_tomo=(TextView) this.findViewById(R.id.tv_tomo);
          tv_tomo_temp=(TextView) this.findViewById(R.id.tv_tomo_tem);
          tv_tomo_weather=(TextView) this.findViewById(R.id.tv_tomo_weather);
          tv_daftomo=(TextView) this.findViewById(R.id.tv_dayaftertomo);
          tv_daftomo_temp=(TextView) this.findViewById(R.id.tv_dayaftertomo_tem);
          tv_daftomo_weather=(TextView) this.findViewById(R.id.tv_dayaftertomo_weather);
          
          btn_ref=(Button) this.findViewById(R.id.btn_refresh);
          btn_add=(Button) this.findViewById(R.id.btn_cityadd);
          btn_share = (Button) this.findViewById(R.id.btn_share);
	      imgv1=(ImageView) this.findViewById(R.id.img_today);
	      imgv2=(ImageView) this.findViewById(R.id.img_tomo);
	      imgv3=(ImageView) this.findViewById(R.id.img_dayaftertomo);
	      lat=(RelativeLayout) this.findViewById(R.id.rltl);
	      
	      detector = new GestureDetector(this);
		  flipper = (ViewFlipper) this.findViewById(R.id.flipper);
		
		  
		  //.����
	      btn_share.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					LinearLayout share = (LinearLayout) getLayoutInflater()
							.inflate(R.layout.share, null);

					// new AlertDialog.Builder(Main.this)
					final AlertDialog.Builder dialog = new AlertDialog.Builder(
							weather.this);
					dialog.setTitle("����");
					dialog.setView(share);

					dialog.setPositiveButton("����",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									String message = tv.getText().toString().trim();
									System.out.println(message);
									usernumber = et.getText().toString();
									if (usernumber.equals("")) {
										Toast.makeText(weather.this.getParent(),
												"�������ֻ�����", Toast.LENGTH_LONG)
												.show();
									} else {
										SmsManager smsManager = SmsManager
												.getDefault();
										if (message.length() > 70) {
											List<String> contents = smsManager
													.divideMessage(message);
											for (String sms : contents) {
												smsManager.sendTextMessage(
														usernumber, null, sms,
														null, null);
											}
										} else {
											smsManager.sendTextMessage(usernumber,
													null, message, null, null);
										}
										// <string
										// name="str_remind_sms_send_finish">�������</string>
										Toast.makeText(weather.this, "���ͳɹ�",
												Toast.LENGTH_SHORT).show();
										System.out.println(message);
									}
								}
							});

					dialog.setNegativeButton("ȡ��", null);
					dialog.show();

					tv = (TextView) share.findViewById(R.id.tv_weather);
					bt1 = (Button) share.findViewById(R.id.btn_cho);
					et = (EditText) share.findViewById(R.id.et_contact);

					String infomsg = tv_day.getText().toString() + " "
							+ tv_week.getText().toString() + " "
							+ tv_citynm.getText().toString() + "������״����"
							+ tv_weath.getText().toString() + "���¶ȣ�"
							+ tv_temp.getText().toString() + "��"
							+ tv_w_dir.getText().toString() + " "
							+ tv_w_pow.getText().toString();
					System.out.println(infomsg);
					tv.setText(infomsg);

					bt1.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							startActivityForResult(new Intent(Intent.ACTION_PICK,
									ContactsContract.Contacts.CONTENT_URI), 0);
						}
					}); 
				}

			});

	      
	      
	      
				 mydbhelper dbh=new mydbhelper(weather.this);
		  		  SQLiteDatabase sdb=dbh.getReadableDatabase();
			  		cursor= sdb.query("city_weather", new String[]{"city_name"},null, null, null, null, null);
					total = cursor.getCount();
					System.out.println("���С�������������������"+total);
					while(cursor.moveToNext()){
						String name=cursor.getString(0);
						System.out.println("���С�������������������"+name);
					}
					if(total>0){
						cursor.moveToFirst();
						String cityname=cursor.getString(0);
						addTextView(cityname);
					}
					else {
						System.out.println("���޳��С�������������������");
						Toast.makeText(this, "������ӳ���",Toast.LENGTH_SHORT).show(); 
					}
				
					//��ӳ���
				btn_add.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Intent in1 = new Intent(weather.this,Addcity.class);
						startActivity(in1);
						
					}});
				
                   //��������
				btn_ref.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						welcome();							
					}});
				
		}  

    //������������ͼƬ
     int weatherimg(String weather){
    	 
    	 if(weather.contains("��"))
    	{
    		  return R.drawable.qingtian;
    	 }
    	 if(weather.contains("��"))
     	{
     		  return R.drawable.lightning_1;
     	 }
    	
    	 if(weather.contains("��"))
     	{
     		  return R.drawable.cloudy_day;
     	 }
    
    	 if(weather.contains("��"))
     	{
     		  return R.drawable.bg_rain;
     	 }
    	 if(weather.contains("ѩ"))
      	{
      		  return R.drawable.day_snow;
      	 }
    	 if(weather.contains("��"))
       	{
       		  return R.drawable.else_bg;
       	 }
    	 
    	 else
    		 return R.drawable.fog_day;    	 
     }
    
     //��������ͼ��
     int weathertobiao(String weather)
     {
    	 if(weather.contains("��")&&weather.contains("��"))
      	{
      		  return R.drawable.w4;
      	 }
    	 if(weather.contains("��")&&weather.contains("С"))
       	{
       		  return R.drawable.w7;
       	 }
    	 if(weather.contains("��")&&weather.contains("��"))
        	{
        		  return R.drawable.w10;
        	 }
    	 if(weather.contains("��")&&weather.contains("��"))
        	{
        		  return R.drawable.w13;
        	 }
    	 
    	 if(weather.contains("��"))
     	{
     		  return R.drawable.w0;
     	 }
     	
     	 if(weather.contains("��"))
      	{
      		  return R.drawable.w2;
      	 }
     
     	 if(weather.contains("��")&&weather.contains("ѩ"))
      	{
      		  return R.drawable.w6;
      	 }
     	 if(weather.contains("��"))
        	{
        		  return R.drawable.w7;
        	 }
     	 if(weather.contains("ѩ"))
       	{
       		  return R.drawable.w17;
       	 }
     	 if(weather.contains("��"))
        	{
        		  return R.drawable.w18;
        	 }
     	 else
     		 return R.drawable.wna;    	 
      }

     //����һ�����У����һ��View
     private void addTextView(String text) {
		  
 		String[] project=new String[]{"city_name","city_todaytem","city_weather","city_wdir",
 				"city_wpow","city_days","city_week","city_tomoweek","city_tomotem",
 				"city_yomoweather","city_daftomoweek","city_daftomotem","city_daftomoweather","city_code"};
 	
 		   mydbhelper dbh=new mydbhelper(weather.this);
 	  		SQLiteDatabase sdb=dbh.getReadableDatabase();
 	
 		cur= sdb.query("city_weather", project, "city_name=?", new String[]{text}, null, null, null);
 		 
 		if(cur !=null)
 		{
 			if(cur.getCount()==0)
 			{
 				Toast.makeText(weather.this, "û�г���������Ϣ", 1).show();
 			}
 		}
 		
 		String weath1 = null;
 		String weath2 = null;
 		String weath3 = null;
 		String citycode = null;
 		//��ȡĬ�ϳ���code
 		String[] project1=new String[]{"maincitycode"};
		Cursor cur1= sdb.query("maincity", project1, "id=?", new String[]{"1"}, null, null, null);
		if(cur1 !=null)
		{
			if(cur1.getCount()==0)
			{
				Toast.makeText(weather.this, "�����Ĭ�ϳ���", 1).show();
			}
		}
		while(cur1.moveToNext())
		{
			citycode=cur1.getString(0);
		}
 		
 		while(cur.moveToNext())
 		{		
 		  System.out.println("system+"+cur.getString(0));
 		 mycode._id=cur.getString(13);

 		  if(citycode.equals(cur.getString(13)))
 		  {
 			 tv_citynm.setText(cur.getString(0)+"(Ĭ��)");
 		  }
 		  else	
 		  {
 			  tv_citynm.setText(cur.getString(0));
 		  }
   		  tv_temp.setText(cur.getString(1));
   		  weath1=cur.getString(2);
   		  tv_weath.setText(weath1);
   		  tv_w_dir.setText(cur.getString(3));
   		  tv_w_pow.setText(cur.getString(4));
   		  tv_day.setText(cur.getString(5));
   		  tv_week.setText(cur.getString(6));
   		  tv_today.setText("����");
   		  tv_today_temp.setText(cur.getString(1));
   		  tv_today_weather.setText(cur.getString(2));
   		  
   		  tv_tomo.setText(cur.getString(7));
   		  tv_tomo_temp.setText(cur.getString(8));
   		  weath2=cur.getString(9);
   		  tv_tomo_weather.setText(weath2);
   		  
   		  tv_daftomo.setText(cur.getString(10));
   		  tv_daftomo_temp.setText(cur.getString(11));
   		  weath3=cur.getString(12);
   		  tv_daftomo_weather.setText(weath3);
   		  		 
 	}
 		int bid = weatherimg(weath1);
 		int tid1 = weathertobiao(weath1);
 		int tid2 = weathertobiao(weath2);
 		int tid3 = weathertobiao(weath3);
 		lat.setBackgroundResource(bid);
 		imgv1.setBackgroundResource(tid1);
 		imgv2.setBackgroundResource(tid2);
 		imgv3.setBackgroundResource(tid3);
 		
 	}
    
     @Override
 	public boolean onTouchEvent(MotionEvent event) {
 		return this.detector.onTouchEvent(event);
 	}

 	@Override
 	public boolean onDown(MotionEvent e) {
 		// TODO Auto-generated method stub
 		return false;
 	}

 	  public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

		total = cursor.getCount();
		System.out.println("total "+total);
		if (e1.getX() - e2.getX() > 120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
			if(!cursor.isLast())
			{
				cursor.moveToNext();
				current++;
				//cur.move(current);
				addTextView(cursor.getString(0));
				System.out.println("next "+cursor.getString(0));
				this.flipper.showNext();
				
			}
			else 
			{
				
			}
			return true;
		} else if (e1.getX() - e2.getX() < -120) {
			this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
			this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
			
			if(!cursor.isFirst())
			{
				cursor.moveToPrevious();
				current--;
				addTextView(cursor.getString(0));
				System.out.println("previous "+cursor.getString(0));
				this.flipper.showPrevious();
				
			}else {
				
			}
			return true;
		}
		return false;
	}
     @Override
 	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
 		super.onActivityResult(requestCode, resultCode, data);
 		if (resultCode == Activity.RESULT_OK) {
 			ContentResolver reContentResolverol = getContentResolver();
 			Uri contactData = data.getData();
 			@SuppressWarnings("deprecation")
 			Cursor cursor = managedQuery(contactData, null, null, null, null);
 			cursor.moveToFirst();
 			String contactId = cursor.getString(cursor
 					.getColumnIndex(ContactsContract.Contacts._ID));
 			Cursor phone = reContentResolverol.query(
 					ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
 					ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = "
 							+ contactId, null, null);
 			while (phone.moveToNext()) {
 				usernumber = phone
 						.getString(phone
 								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
 				et.setText(usernumber);
 			}

 		}
 	}


	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		onCreate(null);
	}
     
	public void welcome(){
		//�ص㣺�̻߳��ơ�
		//��һ���������̶߳���
		new Thread(
				new Runnable(){//�������߳�ʵ���ࡣ

					@Override
					public void run() {//�ڶ����̵߳����ݴ��롣
						// TODO Auto-generated method stub
						
						mydbhelper dbh=new mydbhelper(weather.this);		
						SQLiteDatabase sdb=dbh.getReadableDatabase();
						
						String[] project=new String[]{"city_code"};
						Cursor cur= sdb.query("city_weather", project, null, null, null, null, null);
						if(cur !=null)
						{
							if(cur.getCount()==0)
							{
								Toast.makeText(weather.this, "����ӳ���", 1).show();
							}
						}
						
						while(cur.moveToNext())
						{
							citycode=cur.getString(0);							
						
						
						//�����������й���������ȥȡ�����ݡ�
	  						//String cityCode="1";//demo.ȡ�������ʱ�š��˴��������ɱ���ȡǰ̨������ֵ��
	  						//�������������������ַ��С�
							System.out.println("���б���"+citycode);
	  						String weatherUrl="http://api.k780.com:88/?app=weather.future&weaid="+citycode+ 
	  								"&appkey=10003&sign=b59bc3ef6191eb9f747dd4e83c99f2a4&format=json";
	  						//����ĳ������ȥ���ӷ�������
	  						String wgsoninfo=connWeb(weatherUrl);
						
	  						try {
								Thread.sleep(0);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
	  						Message msg=new Message();//�����̷߳�����Ϣ��
	  						msg.obj=wgsoninfo;//�ѽ��������obj���Է��͸����̡߳�
	  						whandler.sendMessage(msg);//�����߳��е���Ϣ���󷢸������̡߳�
					//run.	  						
						}
						dbh.close();
						begin();					
					}	
				}//runnable.
				).start();
		
	}
	
				 //�ڶ�����
			  	Handler whandler=new Handler(){//handler������ui�̡߳�

			  		@Override
			  		public void handleMessage(Message msg) {//�õ���̨�̵߳���Ϣ��
			  			// TODO Auto-generated method stub
			  			super.handleMessage(msg);
			  			msggmainfo=(String)msg.obj;//���̻߳�ȡ���̴߳��ݹ��������ݡ�
			  			//System.out.println("main Thread:"+msggmainfo);
			  		
			  			//���Ĳ��������õ������ݡ�����Ƿ�װ����������
			  		clist=getGsonList(msggmainfo);
			  			
			  		//�õ����ķ�ʽ��ȡֵ��
			  		Iterator it=clist.iterator();
			  		if(it.hasNext()){
			  			CWeather cw=(CWeather)it.next();
			  			
			  			cw_citycode=cw.getCityid();
			  			cw_cityname=cw.getCity();
			  			cw_weather=cw.getWeather();
			  			cw_temp=cw.getTemperature();
			  			cw_wind_direction=cw.getWind_dir();
			  			cw_wind_power=cw.getWind_pow();
			  			cw_days=cw.getDays();
			  			cw_weeks=cw.getWeek();  //today.
			  			cw_tomoweek=cw.getTomo_week();
			  			cw_tomotemp=cw.getTomo_temp();
			  		    cw_tomoweath=cw.getTomo_weather();
			  			cw_daftomoweek=cw.getDaftomo_week();
			  			cw_daftomotemp=cw.getDaftomo_temp();
			  			cw_daftomoweath=cw.getDaftomo_weather();
			  			
			  			mydbhelper dbh=new mydbhelper(weather.this);
						SQLiteDatabase sdb=dbh.getReadableDatabase();
						
						ContentValues cv=new ContentValues();
						cv.put("city_name",cw_cityname);
						cv.put("city_code",cw_citycode);
						cv.put("city_todaytem",cw_temp);
						cv.put("city_weather",cw_weather);
						cv.put("city_wdir",cw_wind_direction);
						cv.put("city_wpow",cw_wind_power);
						cv.put("city_days",cw_days);
						cv.put("city_week",cw_weeks);
						cv.put("city_tomoweek",cw_tomoweek);
						cv.put("city_tomotem",cw_tomotemp);
						cv.put("city_yomoweather",cw_tomoweath);
						cv.put("city_daftomoweek",cw_daftomoweek);
						cv.put("city_daftomotem",cw_daftomotemp);
						cv.put("city_daftomoweather",cw_daftomoweath);
						
							String[] project=new String[]{"city_name"};
							Cursor cur= sdb.query("city_weather", project, "city_name=?", new String[]{cw_cityname}, null, null, null);
							
							if(cur !=null)
							{
								if(cur.getCount()==0)
								{
									System.out.println("��........................................");
									sdb.insert("city_weather", null,cv);
								}
								else
									sdb.update("city_weather", cv, "city_name=?",new String[]{cw_cityname});
							}
							
						
			  			System.out.println("........."+cw_cityname);
			  			System.out.println("........."+cw_weather);
			  			dbh.close();
			  		}//if.
			  		}//handlerMessage.
			  	};//whandler.
	
	public void begin(){

		Intent in=new Intent(weather.this,MainActivity.class);
		in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(in);
	}//begin.
	
	public List<CWeather> getGsonList(String msg){
		List<CWeather> mlist=new ArrayList<CWeather>();
		
		try {
			JSONObject job=new JSONObject(msg);//JSONObject�����ܵġ�
		//	JSONObject wjob=job.getJSONObject("result");//�õ�job���json�����еĵ�һ��key.
			 JSONArray wjob= job.getJSONArray("result");
			//���ܵõ����е�ÿһ��key��value.
			//���εİ����е�ÿһ��key��valueȡ��,��Ѫ��ʵ���С�
			CWeather cw=new CWeather();
			
			cw.setCityid(wjob.getJSONObject(0).getString("weaid"));//set��ֵ��ͨ��wjob��get��key���õ���value.
			cw.setCity(wjob.getJSONObject(0).getString("citynm"));
			cw.setTemperature(wjob.getJSONObject(0).getString("temperature"));
			cw.setWeather(wjob.getJSONObject(0).getString("weather"));
			cw.setWind_dir(wjob.getJSONObject(0).getString("wind_direction"));
			cw.setWind_pow(wjob.getJSONObject(0).getString("wind_power"));
			cw.setDays(wjob.getJSONObject(0).getString("days"));
			cw.setWeek(wjob.getJSONObject(0).getString("week"));
			cw.setTomo_week(wjob.getJSONObject(1).getString("week"));
			cw.setTomo_temp(wjob.getJSONObject(1).getString("temperature"));
			cw.setTomo_weather(wjob.getJSONObject(1).getString("weather"));
			cw.setDaftomo_week(wjob.getJSONObject(2).getString("week"));
			cw.setDaftomo_temp(wjob.getJSONObject(2).getString("temperature"));
			cw.setDaftomo_weather(wjob.getJSONObject(2).getString("weather"));
			//cw.setTemp2(wjob.getString("temp_low"));
        
			//cw.setDays(wjob.getString("days"));
			//cw.setWeek(wjob.getString("week"));
			
			//ʵ����뼯�ϡ�
			mlist.add(cw);
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}//���ַ���������json����
		
		return mlist;
	}//method
	
	
	//���ӷ������ķ�����
  	private String connWeb(String url){
  		String str="";
  		try {
  		HttpPost request=new HttpPost(url);//get/post.����get�����������������
  		//�������ӿͻ��˶���
  		HttpClient httpClient=new DefaultHttpClient();
  		//�����ӿͻ��˶���ȥ�����������󡣵õ��Ľ����һ�����������ص���Ӧ�������
  		HttpResponse response=httpClient.execute(request);
  		//�ж����ӽ���Ƿ��������������ӳɹ��ˡ���http��״̬�����жϡ�������״̬����200.
  		if(response.getStatusLine().getStatusCode() == 200){
  			//������ӳɹ��ˡ��ѽ�������е�ֵȡ������ת�����ַ�����
  			str=EntityUtils.toString(response.getEntity());
  			System.out.println("response str:"+str);
  		}//if.
  		} catch (ClientProtocolException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		} catch (IOException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}
  		return str;//�˷����������ӵ��Ľ����
  	}//connWeb.





	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onScroll(MotionEvent arg0, MotionEvent arg1, float arg2,
			float arg3) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onShowPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public boolean onSingleTapUp(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		  if (keyCode == KeyEvent.KEYCODE_BACK) {
		  if ((System.currentTimeMillis() - mExitTime) > 2000) {
		         Object mHelperUtils;
		          Toast.makeText(this, "�ٰ�һ���˳�����", Toast.LENGTH_SHORT).show();
		          mExitTime = System.currentTimeMillis();
		         } 
		  else {
			   android.os.Process.killProcess(android.os.Process.myPid());
		      
		  }
		   return true;
   }
	 return super.onKeyDown(keyCode, event);  }


}
