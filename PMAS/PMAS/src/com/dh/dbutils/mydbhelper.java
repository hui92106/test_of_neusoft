package com.dh.dbutils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mydbhelper extends SQLiteOpenHelper {
	// ����û��Ĭ�ϵĹ��췽��������д���صĹ���
	// ��1:��ǰ�������Ļ���
	// ��2�����ݿ������
	// ��3���α깤����Ŀǰ��ϵͳ����ʹ�ã�����Ѿ��ṩ�����α�����ˣ��������д����е��α깤����
	// ֵ��null
	// ��4����ʾ��ǰ���ݿ�İ汾�š������ؽ����ݿ⣬�������ݿ⣬�ָ����ݿ�
	public static final int VERSION = 1;// ���ݿ�İ汾��
	public static final String TABLE_NAME_CONTACT_GROUP = "pmas_contact_group";// ��������ݿ��б������
	public static final String TABLE_NAME_CONTACT_PEOPLE = "pmas_contact_people";// ��������ݿ��б������
	public static final String TABLE_NAME_NOTE_TYPE = "pmas_note_type";// ��������ݿ��б������
	public static final String TABLE_NAME_DAYLIFE_TYPE = "pmas_daylife_type";// ��������ݿ��б������
	public static final String TABLE_NAME_NOTE_MESSAGE = "pmas_note_message";// ��������ݿ��б������
	public static final String TABLE_NAME_DAYLIFE_DAYLIFE = "pmas_daylife_daylife";// ��������ݿ��б������

	public static final String DATABASE_NAME = "neusoft.db";// ��ʾ�������ݿ�

	public mydbhelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	// �˷��������ݿ��ڴ�����ʱ�򱻵���
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// create table �������Ҫ��һ���ո�
		String str_sql = "create table "
				+ TABLE_NAME_CONTACT_GROUP
				+ "(_id integer primary key autoincrement,gr_name varchar,gr_desc varchar)";
		db.execSQL(str_sql);
		String str_sql2 = "create table "
				+ TABLE_NAME_CONTACT_PEOPLE
				+ "(_id integer primary key autoincrement,pe_name varchar,pe_birthday varchar,pe_phone varchar,pe_email varchar,pe_address varchar,pe_group varhcar)";
		db.execSQL(str_sql2);
		String str_sql3 = "create table "
				+ TABLE_NAME_NOTE_TYPE
				+ "(_id integer primary key autoincrement,ty_name varchar,ty_desc varchar)";
		db.execSQL(str_sql3);
		String str_sql4 = "create table "
				+ TABLE_NAME_DAYLIFE_TYPE
				+ "(_id integer primary key autoincrement,ty_name varchar,ty_desc varchar)";
		db.execSQL(str_sql4);
		String str_sql5 = "create table "
				+ TABLE_NAME_NOTE_MESSAGE
				+ "(_id integer primary key autoincrement,me_title varchar,me_content varchar,me_time varchar,me_type varchar)";
		db.execSQL(str_sql5);
		String str_sql6 = "create table "
				+ TABLE_NAME_DAYLIFE_DAYLIFE
				+ "(_id integer primary key autoincrement,da_title varchar,da_content varchar,da_time varchar,da_type varchar)";
		db.execSQL(str_sql6);// �������ݿ�ű���������ʱ�������ݿ⵱�в�û��������صı�
	}

	// �ؽ����ݿ⣬�������ݿ��ʱ��ʹ��
	// ��1����ʾ���ǵ�ǰ�����ݿ����
	// ��2����ʾ����ǰһ���汾��
	// ��3����ʾ��ǰ�汾��
	// �˷�����ʱ���ã�ֻ�е���3>��2��ʱ�򣬴˷�����ص�
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVerson, int newVerson) {
		// TODO Auto-generated method stub
		System.out.println("onUpgrade����ִ���ˡ�����������");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTACT_GROUP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTACT_PEOPLE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTE_TYPE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DAYLIFE_TYPE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTE_MESSAGE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DAYLIFE_DAYLIFE);

		onCreate(db);
	}

}
