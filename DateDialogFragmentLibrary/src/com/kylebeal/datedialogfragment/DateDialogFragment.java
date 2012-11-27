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
package com.kylebeal.datedialogfragment;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.DatePicker;

public class DateDialogFragment extends DialogFragment {
	public static String TAG = "DateDialogFragment";

	static Context sContext;
	static Calendar sDate;
	static DateDialogFragmentListener sListener;	
	
	public static DateDialogFragment newInstance(Context context, int titleResource, Calendar date){
		DateDialogFragment dialog  = new DateDialogFragment();
		
		sContext = context;
		sDate = Calendar.getInstance();
		sDate = date;
		
		Bundle args = new Bundle();
		args.putInt("title", titleResource);
		dialog.setArguments(args);
		
		return dialog;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new DatePickerDialog(sContext, dateSetListener, sDate.get(Calendar.YEAR), sDate.get(Calendar.MONTH), sDate.get(Calendar.DAY_OF_MONTH));
	}
	
	public void setDateDialogFragmentListener(DateDialogFragmentListener listener){
		sListener = listener;
	}
	
	private DatePickerDialog.OnDateSetListener dateSetListener = 
			new DatePickerDialog.OnDateSetListener() {
		
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			
			Calendar newDate = Calendar.getInstance();
			newDate.set(year, monthOfYear, dayOfMonth);
			
			//call back to the DateDialogFragment listener
			sListener.dateDialogFragmentDateSet(newDate);			
		}
	};
	
	//---------------------------------------------------------
	//DateDialogFragment listener interface
	//---------------------------------------------------------
	public interface DateDialogFragmentListener{
		public void dateDialogFragmentDateSet(Calendar date);
	}

}
