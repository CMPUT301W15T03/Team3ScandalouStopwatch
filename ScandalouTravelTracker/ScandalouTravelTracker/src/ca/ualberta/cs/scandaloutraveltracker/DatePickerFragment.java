/*

Copyright 2015 Team3ScandalouStopwatch

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package ca.ualberta.cs.scandaloutraveltracker;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

/**
 * Creates a DatePicker dialog. 
 * @author Team3ScandalouStopwatch
 *
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		
	/**
	 * The onCreateDialog uses the current date as the default value in the picker
	 */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use current date as a default value in the picker
		final Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);
		
		// Create new instance of date picker dialog and return it
		return new DatePickerDialog(getActivity(), this,  year, month, day);
	}
	
	/**
	 * When DatePicker is created in an activity, this method should
	 * be overwritten to suit whatever the activity needs. 
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		
	}
	
	/**
	 * Takes the date and converts it to a string: mm/dd/yyyy
	 * @param year
	 * @param monthOfYear
	 * @param dayOfMonth
	 * @return Date in string format.
	 */
	public String convertToString(int year, int monthOfYear, int dayOfMonth) {
		
		String convertedDate;
		
		convertedDate = "" + (monthOfYear+1) + "/" + dayOfMonth +"/" + year;
		
		return convertedDate;
	}
	
}
