package com.example.weather;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class mydbhelper{

	private static final String DATABASE_PATH = "/data/data/com.example.weather/databases";
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_NAME = "weather.db";
	public static String SCHEDULE_TABLE_NAME = "schedule";
	private static String outFileName = DATABASE_PATH + "/" + DATABASE_NAME;

	private Context context;
	private SQLiteDatabase database;
	 
	public mydbhelper(Context context) {
		this.context = context;
		outFileName = outFileName.toString();
		File file = new File(outFileName);
		
		if (!file.exists()) {
			
			
		}
		try {
			buildDatabase();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void buildDatabase() throws Exception{
		InputStream myInput;
		System.out.println("build");
		outFileName = outFileName.toString();
		myInput = context.getResources().openRawResource(R.raw.weather);
		File file = new File(outFileName);
		
		File dir = new File(DATABASE_PATH);
		if (!dir.exists()) {
			System.out.println("!dir exists");
			if (!dir.mkdir()) {
				throw new Exception("´´½¨Ê§°Ü");
			}
		}
		
		if (!file.exists()) {			
			try {
				System.out.println("!file exists");
				OutputStream myOutput = new FileOutputStream(outFileName);
				byte[] buffer = new byte[1024];
		    	int length;
		    	while ((length = myInput.read(buffer))>0){
		    		myOutput.write(buffer, 0, length);
		    	}
		    	myOutput.close();
		    	myInput.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		database = SQLiteDatabase.openOrCreateDatabase(outFileName, null);
	}
	public SQLiteDatabase getReadableDatabase(){
		database = SQLiteDatabase.openOrCreateDatabase(outFileName, null);
		return database;
	}
	public void close(){
		database.close();
	}



}