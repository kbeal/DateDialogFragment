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

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;


public class DatesProvider extends ContentProvider {

	private static final String TAG = "DatesContentProvider";
	public static final String AUTHORITY = "com.kylebeal.datedialogfragment.example.DatesProvider";
	private static final UriMatcher mUriMatcher;
	private DatesDbHelper mDbHelper;
	
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		//not supporting deletion
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		switch (mUriMatcher.match(uri)){
			case 100:
				return ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.kylebeal.dates";
			case 101:
				return ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.kylebeal.dates";
			default:
				throw new IllegalArgumentException("Unknown Uri " + uri);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#insert(android.net.Uri, android.content.ContentValues)
	 * Inserts a new item
	 */
	@Override
	public Uri insert(Uri uri, ContentValues initialValues) {		
		//not supporting insertion
		//we did not successfully insert the new date
		throw new SQLException("Currently not supporting insertion, implement DatesProvider.insert" + uri);
	}

	@Override
	public boolean onCreate() {
		//create a new db helper
		mDbHelper = new DatesDbHelper(getContext());
		return false;
	}

	@Override
	/*
	 * (non-Javadoc)
	 * @see android.content.ContentProvider#query(android.net.Uri, java.lang.String[], java.lang.String, java.lang.String[], java.lang.String)
	 * queries an item from the db
	 */
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
			String sortOrder) {
		
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();	
		queryBuilder.setTables("dates");
		
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		switch (mUriMatcher.match(uri)){
			//all dates
			case 100:
				//not putting any where on the query
				break;
			//single date
			case 101:
				//append the id given at the end of the uri in the where clause
				queryBuilder.appendWhere("_id="+uri.getLastPathSegment());
				break;
			default:
				//exit, yelling at the caller
				throw new IllegalArgumentException("Unknown Uri " + uri);				
		}
		
		Cursor result = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
		result.setNotificationUri(getContext().getContentResolver(), uri);
		if (result != null){
			result.moveToFirst();
		}		
		
		return result;
	}

	@Override
	public int update(Uri uri, ContentValues newValues, String where, String[] whereArgs) {
		ContentValues values;
		if (newValues != null){
			values = newValues;
		}else{
			values = new ContentValues();
		}
		
		int result = 0;
		
		if (mUriMatcher.match(uri)==101){
			SQLiteDatabase db = mDbHelper.getWritableDatabase();
			result = db.update("dates", values, "_id=?", new String[]{uri.getLastPathSegment()});			
			getContext().getContentResolver().notifyChange(uri, null);
		}else{
			throw new IllegalArgumentException("Unknown Uri "+uri); 
		}
		
		return result;
	}
	
	static{
		mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		//for getting all dates
		mUriMatcher.addURI(AUTHORITY, "dates", 100);
		//for getting single date
		mUriMatcher.addURI(AUTHORITY, "dates/#", 101);
	}
}
