package com.otherDB;

import java.util.ArrayList;
import java.util.Vector;

import list.fragment.MyFragmentActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.mainListDB.MySQLiteHelper;

public class SubListDataBase extends SQLiteOpenHelper {

	static SQLiteDatabase dataRef;

	final static String DB_NAME = "myCommments.db";

	final static String DB_TABLE2 = "secondList";
	final static String SQL_NAME2 = "rahuee";
	final static String SQL_ID2 = "ID";
	final static String SQL_FID = "FID";
	final static String parentTableName = "comments";

	public static String[] allColumns = { SQL_ID2, SQL_NAME2, SQL_FID };

	static Cursor cursor;

	public SubListDataBase(Context context) {
		super(context, DB_NAME, null, 1);
	}

	/***
	 * Creation of Table if not exists open db
	 */
	public void openDB() {
		dataRef = MyFragmentActivity.getCntx().openOrCreateDatabase(DB_NAME, 0, null);

		String cr_table = "create table if not exists " + DB_TABLE2 + " (" + SQL_ID2 + " integer primary key autoincrement ," + SQL_NAME2 + " TEXT,"
				+ SQL_FID + " integer" + ");";
		dataRef.execSQL(cr_table);

	}

	public static long insertDB(SubListModel data) {
		long iTemp = -1;
		String sql = "insert into " + DB_TABLE2 + " (" + SQL_NAME2 + "," + SQL_FID + ") values ('" + data.getName() + "'," + Constants.id + ");";
		SQLiteStatement stmt = dataRef.compileStatement(sql);
		iTemp = stmt.executeInsert();
		return iTemp;
	}

	public static ArrayList<SubListModel> getAllComments() {
		ArrayList<SubListModel> list = new ArrayList<SubListModel>();

		String strWhere = SQL_FID + "=" + Constants.id;

		Cursor cursor = dataRef.query(DB_TABLE2, allColumns, strWhere, null, null, null, null);

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			SubListModel data = cursorToComment(cursor);
			list.add(data);
			cursor.moveToNext();
		}
		// Make sure to close the cursor
		cursor.close();
		return list;
	}

	public static SubListModel cursorToComment(Cursor cursor) {
		SubListModel data = new SubListModel();
		data.setId(cursor.getInt(0));
		data.setName(cursor.getString(1));
		data.setFid(cursor.getInt(2));
		return data;
	}

	public static Vector<SubListModel> getData() {
		String stmt = "select * from " + DB_TABLE2;
		cursor = dataRef.rawQuery(stmt, null);
		return parseCursor(cursor);
	}

	static Vector<SubListModel> parseCursor(Cursor c) {
		int size = c.getCount();

		if (size < 0)
			return null;
		int iTemp = 0;
		Vector<SubListModel> v = new Vector<SubListModel>();

		int indexName = c.getColumnIndex(SQL_NAME2);
		c.moveToFirst();

		while (iTemp < size) {

			SubListModel data = new SubListModel();
			data.setName(c.getString(indexName));
			v.add(data);
			c.moveToNext();
			iTemp++;
		}
		return v;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("");
		// openDB();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void close() {
		if (cursor != null)
			cursor.close();
		if (dataRef != null)
			dataRef.close();
	}

	public void deleteDB(SubListModel data2) {
		long id = data2.getId();
		System.out.println("Comment deleted with id: " + id);
		dataRef.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);

		dataRef.delete(DB_TABLE2, SQL_ID2 + " = " + id, null);
	}

	// public static int selectDB(){
	//
	// String stmt = "select max(ID) from " + DB_TABLE ;
	// cursor = dataRef.rawQuery(stmt, null);
	// return parseCursor2(cursor);
	//
	//
	// }

	// static int parseCursor2(Cursor c) {
	// int size = c.getCount();
	//
	// if (size < 0)
	// return 0;
	// int iTemp = 0;
	//
	//
	// int indexName = c.getColumnIndex(SQL_ID);
	// c.moveToFirst();
	// int p =c.getInt(iTemp) ;
	//
	// return p;
	// }
}
