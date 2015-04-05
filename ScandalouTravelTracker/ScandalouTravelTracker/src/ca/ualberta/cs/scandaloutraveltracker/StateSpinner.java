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

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Spinner;

/**
 * The StateSpinner extends the Spinner class and just adds the functionality
 * of being able to check if the spinner has been opened or if it has not been
 * opened.
 * @author Team3ScandalouStopwatch
 *
 */
public class StateSpinner extends Spinner {

	// http://stackoverflow.com/questions/18447063/spinner-get-state-or-get-notified-when-opens
	// 03/29/2015
	private boolean spinnerOpened;

	/**
	 * Constructor that has the additional functionality of setting the spinnerOpened
	 * status to false when created as it has not been opened yet
	 * @param context Activity Context that spinner is to be displayed
	 */
    public StateSpinner(Context context) 
    {
         super(context);
         this.spinnerOpened = false;
    }

	/**
	 * Constructor that has the additional functionality of setting the spinnerOpened
	 * status to false when created as it has not been opened yet
	 * @param context Activity Context that spinner is to be displayed
	 */
    public StateSpinner(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
        this.spinnerOpened = false;
    }

	/**
	 * Constructor that has the additional functionality of setting the spinnerOpened
	 * status to false when created as it has not been opened yet
	 * @param context Activity Context that spinner is to be displayed
	 */
    public StateSpinner(Context context, int mode) 
    {
        super(context, mode);
        this.spinnerOpened = false;
    }

	/**
	 * Constructor that has the additional functionality of setting the spinnerOpened
	 * status to false when created as it has not been opened yet
	 * @param context Activity Context that spinner is to be displayed
	 */
    public StateSpinner(Context context, AttributeSet attrs, int defStyle, int mode) 
    {
        super(context, attrs, defStyle, mode);
        this.spinnerOpened = false;
    }

	/**
	 * Constructor that has the additional functionality of setting the spinnerOpened
	 * status to false when created as it has not been opened yet
	 * @param context Activity Context that spinner is to be displayed
	 */
    public StateSpinner(Context context, AttributeSet attrs, int defStyle) 
    {
        super(context, attrs, defStyle);
        this.spinnerOpened = false;
    }
	
	@Override
	/**
	 * The performClick is overridden so that it can also update the state of the spinner
	 * to being opened. 
	 */
	public boolean performClick() {
		
		spinnerOpened = true;

		return super.performClick();	
	}
	
	/**
	 * Can be called to check if the StateSpinner has ever been opened
	 * @return Boolean based on if the spinner has been opened
	 */
	public boolean hasBeenOpened() {
		return spinnerOpened;
	}

}
