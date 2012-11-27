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
import com.kylebeal.datedialogfragment.example.DateListFragment.ListItemSelectedListener;

public class DateListActivity extends FragmentActivity 
	implements ListItemSelectedListener, DateDetailFragmentButtonListener{
	
	DateDetailFragment mDateDetailFragment;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.date_list_activity);
    }

	//---------------------------------------------------------
	//ListItemSelectedListener listener interface
	//---------------------------------------------------------
	@Override
	public void onListItemSelected(long id) {
		//ask the fragment manager for a date detail fragment
        mDateDetailFragment = (DateDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.date_detail_fragment);
        
        //if the fragment manager is unable to give one
        //it is not currently shown (portrait orientation, small screen device)
        if (mDateDetailFragment == null || !mDateDetailFragment.isInLayout()) {
        	//create a new intent to start up the date detail activity
            Intent showDate = new Intent(getApplicationContext(),
                    DateDetailActivity.class);
            
            showDate.putExtra("date_id", id);
            startActivity(showDate);
        } else {
        	//fragment manager gave us a date detail fragment (it is shown)
        	//update the date detail fragment
        	mDateDetailFragment.updateDateId(id);
        }
	}

	//---------------------------------------------------------
	//Date Detail Fragment Button listener interface
	//---------------------------------------------------------
	@Override
	public void onSetDateButtonClicked(Calendar date) {
		//create new DateDialogFragment
		DateDialogFragment ddf = DateDialogFragment.newInstance(this, R.string.set_date, date);
		
		ddf.setDateDialogFragmentListener(new DateDialogFragmentListener() {
			
			@Override
			public void dateDialogFragmentDateSet(Calendar date) {
				// update the fragment
				mDateDetailFragment.updateDate(date);
			}
		});
		
		ddf.show(getSupportFragmentManager(), "date picker dialog fragment");
	}
}