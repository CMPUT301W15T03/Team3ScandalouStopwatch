package ca.ualberta.cs.scandaloutraveltracker;

import java.util.Calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
		
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
	
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		// TODO Set values 
		
	}
	
	public String convertToString(int year, int monthOfYear, int dayOfMonth) {
		
		String convertedDate;
		
		convertedDate = "" + (monthOfYear+1) + "/" + dayOfMonth +"/" + year;
		
		return convertedDate;
	}
	
}
