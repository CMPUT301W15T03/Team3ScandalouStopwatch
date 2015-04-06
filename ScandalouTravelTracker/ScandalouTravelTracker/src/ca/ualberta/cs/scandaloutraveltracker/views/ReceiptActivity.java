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

import java.io.File;

import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.models.Receipt;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * 	Shows the image that had it's file path passed as an intent to the activity
 * @author Team3ScandalouStopwatch
 *
 */
public class ReceiptActivity extends MenuActivity {

	private Receipt receipt;
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
	    String receiptPath = intent.getStringExtra(EditExpenseActivity.receiptPathLabel);
	    
	    if (receiptPath != null){
			receipt = new Receipt(receiptPath);
			File receiptFile = receipt.getPhotoFile();
			
			// Create the drawable
			Uri receiptFileUri = Uri.fromFile(receiptFile);
			Drawable receiptPhoto = Drawable.createFromPath(receiptFileUri.getPath());		
	
			// Draw the receipt
			receiptView = (ImageView) findViewById(R.id.receipt_receipt_view);
			receiptView.setImageDrawable(receiptPhoto);
	    }
		
	}

}
