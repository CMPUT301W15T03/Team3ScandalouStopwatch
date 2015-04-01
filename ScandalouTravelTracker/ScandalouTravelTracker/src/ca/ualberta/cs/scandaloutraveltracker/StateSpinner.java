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

public class StateSpinner extends Spinner {

	// http://stackoverflow.com/questions/18447063/spinner-get-state-or-get-notified-when-opens
	// 03/29/2015
	private boolean spinnerOpened;

    public StateSpinner(Context context) 
    {
         super(context);
         this.spinnerOpened = false;
    }

    public StateSpinner(Context context, AttributeSet attrs) 
    {
        super(context, attrs);
        this.spinnerOpened = false;
    }

    public StateSpinner(Context context, int mode) 
    {
        super(context, mode);
        this.spinnerOpened = false;
    }

    public StateSpinner(Context context, AttributeSet attrs, int defStyle, int mode) 
    {
        super(context, attrs, defStyle, mode);
        this.spinnerOpened = false;
    }

    public StateSpinner(Context context, AttributeSet attrs, int defStyle) 
    {
        super(context, attrs, defStyle);
        this.spinnerOpened = false;
    }
	
	@Override
	public boolean performClick() {
		
		spinnerOpened = true;

		return super.performClick();	
	}
	
	public boolean hasBeenOpened() {
		return spinnerOpened;
	}

}
