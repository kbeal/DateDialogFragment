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

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ListView;

public class DateListFragment extends ListFragment 
	implements LoaderManager.LoaderCallbacks<Cursor>{

	private static final int DATE_LIST_LOADER = 0x01;
	
	private ListItemSelectedListener mSelectedListener;
	private SimpleCursorAdapter mAdapter;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
        	//cast the parent activity to a list item selection listener
            mSelectedListener = (ListItemSelectedListener) activity;
        } catch (ClassCastException e) {
        	//if the cast fails, yell
            throw new ClassCastException(activity.toString()
                    + " must implement ListItemSelectedListener in Activity");
        }
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		String[] bindFrom = {"date"};
		int[] bindTo = {R.id.short_date_text};
		
		//set up the list to pull its data from the db
		getLoaderManager().initLoader(DATE_LIST_LOADER, null, this);
		
        mAdapter = new SimpleCursorAdapter(
                getActivity().getApplicationContext(), R.layout.date_list_item,
                null, bindFrom, bindTo,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

        setListAdapter(mAdapter);
        setHasOptionsMenu(true);
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		//tell the parent activity a list item was clicked
		mSelectedListener.onListItemSelected(id);
	}

	//---------------------------------------------------------
	//ListItemSelectedListener listener interface
	//---------------------------------------------------------
	public interface ListItemSelectedListener{
		public void onListItemSelected(long id);
	}

	//---------------------------------------------------------
	//LoaderCallbacks interface
	//---------------------------------------------------------	
	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
        String[] projection = { "_id","date" };

        CursorLoader cursorLoader = new CursorLoader(getActivity(),
        		Uri.parse("content://" + DatesProvider.AUTHORITY + "/dates"), projection, null, null, null);
        return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		mAdapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);
	}
}
