package ca.ualberta.cs.scandaloutraveltracker.views;

import java.io.File;

import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.R.layout;
import ca.ualberta.cs.scandaloutraveltracker.R.menu;
import ca.ualberta.cs.scandaloutraveltracker.models.Receipt;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.widget.ImageView;

public class ReceiptActivity extends Activity {

	private Receipt receipt;
	private ImageView receiptView;
	
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receipt, menu);
		return true;
	}

}
