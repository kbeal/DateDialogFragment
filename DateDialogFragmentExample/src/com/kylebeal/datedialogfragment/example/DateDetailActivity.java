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

import java.util.Calendar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.kylebeal.datedialogfragment.DateDialogFragment;
import com.kylebeal.datedialogfragment.DateDialogFragment.DateDialogFragmentListener;
import com.kylebeal.datedialogfragment.example.DateDetailFragment.DateDetailFragmentButtonListener;


public class DateDetailActivity extends FragmentActivity implements 
	DateDetailFragmentButtonListener{

	public static final String BUNDLE_KEY_DATE_ID = "date_id";	
	private static final String DATE_PICKER_TAG = "date_picker_dialog_fragment";
	
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.date_detail_activity);		
		
		//get the launching intent
		Intent launchingIntent = getIntent();
		
		//get the id for the date passed in the intent
		long dateId = launchingIntent.getLongExtra("date_id", 0);
               
        //get the Date Detail Fragment
		DateDetailFragment ddf = (DateDetailFragment)getSupportFragmentManager().findFragmentById(R.id.date_detail_fragment);
        //update the detail fragment with data from date with given id
        ddf.updateDateId(dateId);
	}

	//------------------------------------------------------
	//DateDetailFragment interface
	//-----------------------------------------------------
	@Override
	public void onSetDateButtonClicked(Calendar date) {
		//create new DateDialogFragment
		DateDialogFragment ddf = DateDialogFragment.newInstance(this, R.string.set_date, date);
		
		ddf.setDateDialogFragmentListener(new DateDialogFragmentListener() {
			
			@Override
			public void dateDialogFragmentDateSet(Calendar date) {
				DateDetailFragment ddf = (DateDetailFragment)getSupportFragmentManager().findFragmentById(R.id.date_detail_fragment);
				// update the fragment				
				ddf.updateDate(date);
			}
		});
		
		ddf.show(getSupportFragmentManager(), DATE_PICKER_TAG);
	}

	@Override
	protected void onPause() {
		DateDialogFragment ddf = (DateDialogFragment)getSupportFragmentManager().findFragmentByTag(DATE_PICKER_TAG);
		if (ddf!=null){
			ddf.dismiss();
		}
		super.onPause();
	}
}
