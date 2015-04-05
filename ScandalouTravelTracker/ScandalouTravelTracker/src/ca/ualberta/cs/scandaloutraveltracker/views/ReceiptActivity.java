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
