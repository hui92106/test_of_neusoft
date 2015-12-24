package com.example.weather;

import java.util.ArrayList;
import java.util.HashMap;


import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class Addcity extends Activity {

	private GridView gv = null;
	Button btn_back;
	SimpleAdapter myadapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_addcity);
		
		gv = (GridView) findViewById(R.id.gv_addcity);
		btn_back=(Button) this.findViewById(R.id.addcity_back);
		final ArrayList<HashMap<String,Object>> mylist=new ArrayList<HashMap<String,Object>>();
		
		mydbhelper dbh=new mydbhelper(Addcity.this);		
		SQLiteDatabase sdb=dbh.getReadableDatabase();
		String[] project=new String[]{"city_name","city_todaytem","city_weather","city_code"};
		Cursor cur= sdb.query("city_weather", project, null, null, null, null, null);
		String str1;
		String str2;
		String str3;
		int id;
		if(cur !=null)
		{
			if(cur.getCount()==0)
			{
				Toast.makeText(Addcity.this, "请添加城市", 1).show();
			}
		}
		
		while(cur.moveToNext())
		{
			HashMap<String,Object> map1=new HashMap<String,Object>();
			str1=cur.getString(0);
			str2=cur.getString(1);
			str3=cur.getString(2);
			id=weathertobiao(str3);
			map1.put("resid", id);
			map1.put("cityname", str1);
			map1.put("temp",str2);
			map1.put("weather", str3);
			mylist.add(map1);			
		}
		sdb.close();
		HashMap<String,Object> map1=new HashMap<String,Object>();
		map1.put("resid", R.drawable.timepicker_up_normal);
		mylist.add(map1);
		
		myadapter=new SimpleAdapter(this,mylist,R.layout.addcitylist,
				new String[]{"resid","cityname","temp","weather","moren"},new int[]{R.id.img_weather,R.id.tv_cityname,R.id.tv_temp,R.id.tv_weather});
		
		gv.setAdapter(myadapter);
		
		gv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long id) {
				// TODO Auto-generated method stub
				HashMap map=mylist.get(pos);

				String str=(String)map.get("cityname");
				
				if(str==null){
					Intent in=new Intent(Addcity.this,Addcityinfo.class);
					in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(in);
					Addcity.this.finish();
				}
				else{
					Intent in=new Intent(Addcity.this,MainActivity.class);
					startActivity(in);
					Addcity.this.finish();
				}
					
				
				
			}});
		gv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int pos, long arg3) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(Addcity.this)
				.setItems(new String[]{"设为默认城市","删除","取消"}, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(which==0)
						{
							HashMap map=mylist.get(pos);
							String str=(String)map.get("cityname"); 
							
							mydbhelper dbh=new mydbhelper(Addcity.this);
							SQLiteDatabase sdb=dbh.getReadableDatabase();
							String[] project=new String[]{"_city_code"};
							Cursor cur= sdb.query("city_info", project, "_city1=?", new String[]{str}, null, null, null);
							while(cur.moveToNext())
							{
							    mycode.id=cur.getString(0);
							}
							ContentValues cv=new ContentValues();
							cv.put("maincitycode",mycode.id);
							sdb.update("maincity", cv, "id=?",new String[]{"1"});
							
						}
						//设置默认城市.
						else if(which==1)
						{
							HashMap map=mylist.get(pos);
							String str=(String)map.get("cityname"); 
							mydbhelper dbh=new mydbhelper(Addcity.this);
							SQLiteDatabase sdb=dbh.getReadableDatabase();
							sdb.delete("city_weather", "city_name=?", new String[]{str});							
							
							//.重新绑定数据源
							mylist.clear();
							SimpleAdapter myadapter1;
							
							String[] project=new String[]{"city_name","city_todaytem","city_weather"};
							Cursor cur= sdb.query("city_weather", project, null, null, null, null, null);
							String str1;
							String str2;
							String str3;
							int id;
							if(cur !=null)
							{
								if(cur.getCount()==0)
								{
									Toast.makeText(Addcity.this, "请添加城市", 1).show();
								}
							}
							while(cur.moveToNext())
							{
								HashMap<String,Object> map1=new HashMap<String,Object>();
								str1=cur.getString(0);
								str2=cur.getString(1);
								str3=cur.getString(2);
								id=weathertobiao(str3);
								map1.put("resid", id);
						        map1.put("cityname", str1);
						        map1.put("temp",str2);
						        map1.put("weather", str3);
						        mylist.add(map1);
							}
							HashMap<String,Object> map1=new HashMap<String,Object>();
							map1.put("resid", R.drawable.timepicker_up_normal);
							mylist.add(map1);
							
						
							gv.setAdapter(myadapter);

							dbh.close();

							
						}		//删除城市.			
					}
				}).show();
				return false;
			}
		});
		
		btn_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in1 = new Intent(Addcity.this,MainActivity.class);
				in1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(in1);
				Addcity.this.finish();
			}});

		
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		onCreate(null);
	}

	int weathertobiao(String weather)
    {
   	 if(weather.contains("雨")&&weather.contains("雷"))
     	{
     		  return R.drawable.w4;
     	 }
   	 if(weather.contains("雨")&&weather.contains("小"))
      	{
      		  return R.drawable.w7;
      	 }
   	 if(weather.contains("雨")&&weather.contains("暴"))
       	{
       		  return R.drawable.w10;
       	 }
   	 if(weather.contains("晴")&&weather.contains("云"))
       	{
       		  return R.drawable.w13;
       	 }
   	 
   	 if(weather.contains("晴"))
    	{
    		  return R.drawable.w0;
    	 }
    	
    	 if(weather.contains("云"))
     	{
     		  return R.drawable.w2;
     	 }
    
    	 if(weather.contains("雨")&&weather.contains("雪"))
     	{
     		  return R.drawable.w6;
     	 }
    	 if(weather.contains("雨"))
       	{
       		  return R.drawable.w7;
       	 }
    	 if(weather.contains("雪"))
      	{
      		  return R.drawable.w17;
      	 }
   	 	if(weather.contains("阴"))
   	 	{
   	 		return R.drawable.w18;
   	 	}
    	 else
    		 return R.drawable.wna;    	 
     }



}
