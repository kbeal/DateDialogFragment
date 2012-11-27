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

import java.text.ParseException;
import java.util.Calendar;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DateDetailFragment extends Fragment {
	
	public static final String TAG = "DateDetailFragment";
	
	RelativeLayout mRootView;
	TextView mShortDateText;
	TextView mLongDateText;
	Button mSetDateButton;
	DateDetailFragmentButtonListener mButtonListener;
	Calendar mDate;
	long mDateId;
		

    @Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
            mButtonListener = (DateDetailFragmentButtonListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DateDetailFragmentButtonListener in Activity");
        }
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {    
		
		
    	mRootView = (RelativeLayout)inflater.inflate(R.layout.date_detail_fragment, container);
    	
    	mShortDateText = (TextView)mRootView.findViewById(R.id.short_date_text);
    	mLongDateText = (TextView)mRootView.findViewById(R.id.long_date_text);
    	
    	mSetDateButton = (Button)mRootView.findViewById(R.id.set_date_button);
    	mSetDateButton.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				showDateDialog();
			}
		});    
    	
    	return mRootView;
    }
	
	/**
	 * Update the Ui and the db record the detail fragment is currently on
	 *  with this date
	 * @param date - the new date
	 */
	public void updateDate(Calendar date){			
		//set this detail fragment's date property
		setDate(date);
		//update the display
		updateUi(date);
		//update the row in the db
		setDbDate(date);
	}
	
	/**
	 * Switch to the date from the db from specified row
	 * @param dateId - id of row containing date to display
	 */
	public void updateDateId(long dateId){		
		mDateId = dateId;
		String[] projection = {"_id","date"};
		Uri queryUri = Uri.withAppendedPath(Uri.parse("content://"+DatesProvider.AUTHORITY+"/dates"), String.valueOf(dateId));			
		//get the specified date from the db
		Cursor dateCursor = getActivity().getContentResolver().query(queryUri, 
				projection, null, null,null);
		
		Calendar date = getDate(dateCursor);		
		setDate(date);
		updateUi(date);		
	}
	
	private void setDbDate(Calendar newDate){
		//get activity's content resolver and update
		Uri updateUri = Uri.withAppendedPath(Uri.parse("content://"+DatesProvider.AUTHORITY+"/dates"),String.valueOf(mDateId));
		ContentValues newDateValues = new ContentValues();
		newDateValues.put("date", DatesDbHelper.SHORT_DATE_FORMAT.format(newDate.getTime()));
		getActivity().getContentResolver().update(updateUri, newDateValues, "_id=?", new String[]{String.valueOf(mDateId)});
	}
	
	private Calendar getDate(Cursor dateCursor){
		String shortDateStr = dateCursor.getString(dateCursor.getColumnIndex("date"));
		
		Calendar date = Calendar.getInstance();
		try {
			date.setTime(DatesDbHelper.SHORT_DATE_FORMAT.parse(shortDateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	private void updateUi(Calendar date){		
		String shortDateStr = DatesDbHelper.SHORT_DATE_FORMAT.format(date.getTime());
		mShortDateText.setText(shortDateStr);					
		
		String longDateStr = DatesDbHelper.LONG_DATE_FORMAT.format(date.getTime());
		mLongDateText.setText(longDateStr);
	}
	
	public void setDate(Calendar newDate){
		mDate = newDate;
	}
    
    private void showDateDialog(){
    	mButtonListener.onSetDateButtonClicked(mDate);
    }
    
    //------------------------------------------
    //Fragment's interface
    //-----------------------------------------
    public interface DateDetailFragmentButtonListener{
    	public void onSetDateButtonClicked(Calendar date);
    }
}
