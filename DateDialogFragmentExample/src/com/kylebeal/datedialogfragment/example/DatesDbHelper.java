/**
Copyright (c) 2012 Kyle Beal

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and 
associated documentation files (the "Software"), to deal in the Software without restriction, 
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, 
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED 
TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL 
THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, 
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.kylebeal.datedialogfragment.example;


import java.text.SimpleDateFormat;
import java.util.Locale;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatesDbHelper extends SQLiteOpenHelper {
	
	private static final String TAG = "DateDbHelper";
	private static final int DB_VERSION = 3; //if this changes, onUpgrade() is called
	private static final String DB_NAME = "dates_db";
	
	public static final SimpleDateFormat LONG_DATE_FORMAT =
			new SimpleDateFormat("EEEEEEE, MMMMMMMMM dd, yyyy",Locale.US);
	
	public static final SimpleDateFormat SHORT_DATE_FORMAT =
			new SimpleDateFormat("MM-dd-yy",Locale.US);
	
	//SQL CREATE statement that creates the inspection types table
	private static final String CREATE_DATES_TABLE = "CREATE TABLE dates (_id integer primary key autoincrement, date text);";			
	
	Context mContext;

	public DatesDbHelper(Context context) {
		super(context, DB_NAME, null, DB_VERSION);
		mContext = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {			
		//create the dates table
		db.execSQL(CREATE_DATES_TABLE);
		populateDatesTable(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		//just drop and load
		db.execSQL("DROP TABLE dates");
		db.execSQL(CREATE_DATES_TABLE);
		populateDatesTable(db);
	}
	
	private void populateDatesTable(SQLiteDatabase db){
		String[] dates = mContext.getResources().getStringArray(R.array.dates);
		for (String date : dates){
			ContentValues cv = new ContentValues();
			cv.put("date", date);
			db.insert("dates", null, cv);
		}
	}
}
