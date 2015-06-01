package com.zhangdong.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteMethod {
	SQLiteDatabase db;

	public SqliteMethod(Context context) {
		Sqlite sdbHelper = new Sqlite(context, "searchHistory", null, 1);
		if(sdbHelper!=null){
		db = sdbHelper.getWritableDatabase();
		}else{
			Log.v("tag", "www");
		}
	}

	// 添加数据
	public void insert(String keywords) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("keyword", keywords);
		db.insert("searchHistory", null, contentValues);

	}

	// 根据关键字查询数据
	public Cursor getBykeywords(String keywords) {
		String str = "select * from searchHistory where keyword=?";
		Cursor cursor = db.rawQuery(str, new String[] { keywords });
		return cursor;
	}
	
	// 查询全部数据
		public Cursor getAll() {
			String str = "select * from searchHistory ";
			Cursor cursor = db.rawQuery(str,null);
			return cursor;
		}
		
		//删除全部数据	
				public void deleteAll(){
					
					db.delete("searchHistory", null, null);
				}
		
	
}
