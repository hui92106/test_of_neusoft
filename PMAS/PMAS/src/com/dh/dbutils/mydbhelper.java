package com.dh.dbutils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class mydbhelper extends SQLiteOpenHelper {
	// 此类没有默认的构造方法，必须写重载的构造
	// 参1:当前的上下文环境
	// 参2：数据库的名称
	// 参3：游标工厂，目前的系统不会使用，类库已经提供处理游标的类了，如果想重写类库中的游标工厂类
	// 值是null
	// 参4：表示当前数据库的版本号。用来重建数据库，升级数据库，恢复数据库
	public static final int VERSION = 1;// 数据库的版本号
	public static final String TABLE_NAME_CONTACT_GROUP = "pmas_contact_group";// 定义的数据库中表的名称
	public static final String TABLE_NAME_CONTACT_PEOPLE = "pmas_contact_people";// 定义的数据库中表的名称
	public static final String TABLE_NAME_NOTE_TYPE = "pmas_note_type";// 定义的数据库中表的名称
	public static final String TABLE_NAME_DAYLIFE_TYPE = "pmas_daylife_type";// 定义的数据库中表的名称
	public static final String TABLE_NAME_NOTE_MESSAGE = "pmas_note_message";// 定义的数据库中表的名称
	public static final String TABLE_NAME_DAYLIFE_DAYLIFE = "pmas_daylife_daylife";// 定义的数据库中表的名称

	public static final String DATABASE_NAME = "neusoft.db";// 表示的是数据库

	public mydbhelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	// 此方法是数据库在创建的时候被调用
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// create table 后面必须要有一个空格
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
		db.execSQL(str_sql6);// 运行数据库脚本，建表。此时，在数据库当中并没有生产相关的表
	}

	// 重建数据库，升级数据库的时候使用
	// 参1：表示的是当前的数据库对象
	// 参2：表示的是前一个版本号
	// 参3：表示当前版本号
	// 此方法何时调用？只有当参3>参2的时候，此方法会回调
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVerson, int newVerson) {
		// TODO Auto-generated method stub
		System.out.println("onUpgrade方法执行了。。。。。。");
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTACT_GROUP);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CONTACT_PEOPLE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTE_TYPE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DAYLIFE_TYPE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NOTE_MESSAGE);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_DAYLIFE_DAYLIFE);

		onCreate(db);
	}

}
