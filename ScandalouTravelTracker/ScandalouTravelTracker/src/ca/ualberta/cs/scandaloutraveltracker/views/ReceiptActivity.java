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

package ca.ualberta.cs.scandaloutraveltracker.views;

import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.R;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

/**
 * 	Shows the image that had it's file path passed as an intent to the activity
 * @author Team3ScandalouStopwatch
 *
 */
public class ReceiptActivity extends MenuActivity {

	private ImageView receiptView;
	
	/**
	 *	Called when the activity is created. 
	 *	Sets the image view to the passed file path image
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receipt);
		
	    // Get the message from the intent
	    Intent intent = getIntent();
	    String receiptPhoto = intent.getStringExtra(Constants.receiptPhotoLabel);
	    
	    if (receiptPhoto != null){
	    	
	    	// CITATION http://stackoverflow.com/questions/15683032/android-convert-base64-encoded-string-into-image-view, 2015-04-07
			byte[] decodedString = Base64.decode(receiptPhoto, Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);			
			
			// Draw the receipt
			receiptView = (ImageView) findViewById(R.id.receipt_receipt_view);
			receiptView.setImageBitmap(decodedByte);
	    }
		
	}

}
