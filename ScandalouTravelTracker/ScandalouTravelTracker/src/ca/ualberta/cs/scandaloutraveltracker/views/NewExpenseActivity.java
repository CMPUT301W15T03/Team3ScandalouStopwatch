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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.DatePickerFragment;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ExpenseController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ReceiptController;
import ca.ualberta.cs.scandaloutraveltracker.mappers.ClaimMapper;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
import ca.ualberta.cs.scandaloutraveltracker.models.Receipt;

/**
 *  This is the activity that provides functionality for adding a new
 *  expense to the current claim.
 * @author Team3ScandalouStopwatch
 *
 */

public class NewExpenseActivity extends Activity implements ViewInterface {

	private int claimId;
	private Receipt receipt = new Receipt(null);
	private ReceiptController receiptController;	
	private Button addExpenseButton;
	private Date date;
	private boolean canEdit;
	private ClaimController claimController;
	private ExpenseController expenseController;
	private ClaimListController claimListController;
	private Button cancel;
	private EditText dateEditText;
	private Spinner categorySpinner;
	private EditText amountEditText;
	private Spinner currencySpinner;
	private EditText descriptionEditText;
	private TextView locationTextView;
	private ClaimMapper mapper;
	private Location location = null;
	private LocationManager lm;
	private Location GPSLocation;
	private Button locationButton;
	private ImageButton receiptThumbnail;
	private ImageButton deleteReceiptButton;
	private Uri receiptPhotoUri;
	private String receiptPath; // shouldn't be a global; will figure out better way later
	private TextView addReceiptText;	
	
	/**
	 * 	Called when the activity is created. Sets up the views, controllers and 
	 * 	listeners of the activity.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_expense);
		
		//set up GPS
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		GPSLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		//create ClaimMapper for saving data
		mapper = new ClaimMapper(this.getApplicationContext());
		
		//create date picker
		LinearLayout dateCurrencyLL = (LinearLayout) findViewById(R.id.addExpenseDateAndCurrencyLayout);
		dateEditText = (EditText) dateCurrencyLL.findViewById(R.id.date_expense2);
	
		//get widgets
		categorySpinner = (Spinner)findViewById(R.id.category);
		amountEditText = (EditText)findViewById(R.id.amount2);
		currencySpinner = (Spinner)findViewById(R.id.currency);
		descriptionEditText = (EditText)findViewById(R.id.description2);
		locationTextView = (TextView) findViewById(R.id.new_location_edit_text);
		locationButton = (Button) findViewById(R.id.add_expense_location_button);
		receiptThumbnail = (ImageButton) findViewById(R.id.add_expense_receipt_thumbnail);
		addReceiptText = (TextView) findViewById(R.id.add_expense_add_receipt_text);
		cancel = (Button) findViewById(R.id.new_expense_cancel);
		deleteReceiptButton = (ImageButton) findViewById(R.id.add_expense_delete_receipt);
		deleteReceiptButton.setVisibility(View.INVISIBLE);		

		//makes sure that the position of the claim and corresponding 
		//expense to be edited are actually passed to this activity
		Bundle extras = getIntent().getExtras();
		Intent intent = getIntent();
		claimId = (int) intent.getIntExtra(Constants.claimIdLabel, -1);
		if (claimId == -1) {
			Toast.makeText(this, "The Claim Position needs to be added to the " +
					"EditExpenseActivity intent before startActivity(intent) is called",
					Toast.LENGTH_LONG).show();
			finish();
		}		
		
		claimController = new ClaimController(new Claim(claimId));
		
		canEdit = claimController.getCanEdit();
		
		receipt = new Receipt(null);
		receiptController = new ReceiptController(receipt);
		setReceiptPhoto(receipt);	
		
		receiptController.addView(this);
		
		setUpListeners();
		
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
	}
	
	/**
	 * 	Sets the action bar to include the options to change user and take a receipt photo in the 
	 * 	dropdown menu.
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.view_expense, menu);
		MenuItem photoItem = menu.findItem(R.id.action_take_photo);
		if(canEdit) {
			photoItem.setVisible(true);
		} else {
			photoItem.setVisible(false);
		}
		return true;
	}
	
	/**
	 * 	Deals with the user pressing on options in the dropdown menu of the action bar.
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses
		switch(item.getItemId()) {
		// Go to user select menu
		case R.id.action_take_photo:
			if (canEdit) {
				takeReceiptPhoto();
			}
            return true;
		// Default do nothing
		default: 
			return false;
		}
	}
	
	/**
	 *  Sets up the on click listeners for editing the date of the
	 * 	expense and pressing add to add the expense to the current claim
	 */
	private void setUpListeners() {
		// Set date onClickListener
		dateEditText.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DialogFragment newFragment = new DatePickerFragment() {
					@Override
					public void onDateSet(DatePicker view, int year, int monthOfYear,
							int dayOfMonth) {
						String dateString = convertToString(year, monthOfYear, dayOfMonth);
						Calendar cal = Calendar.getInstance();
						cal.clear(Calendar.MILLISECOND);
						cal.set(year, monthOfYear, dayOfMonth);
						date = cal.getTime();
						dateEditText.setText(dateString);
					}
				};
				newFragment.show(getFragmentManager(), "datePicker");
			}
		});
			
		//sets image button for receipt
		receiptThumbnail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (receiptController.getReceiptPhoto() != null){			
				   Intent intent = new Intent(NewExpenseActivity.this, ReceiptActivity.class);
				   intent.putExtra(Constants.receiptPhotoLabel, receiptController.getReceiptPhoto());
				   startActivity(intent);
				} 
			}
		});
		
		//sets delete button for receipt
		deleteReceiptButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
				if (!canEdit) {
					
					//Toast.makeText(getApplicationContext(),
							//claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
				}
				else {
					receiptController.saveReceiptPhoto(null);
				}
			}
		});		
		
		//create listener for Add button
		addExpenseButton = (Button)findViewById(R.id.add_expense_button);
		addExpenseButton.setOnClickListener(new View.OnClickListener() {
			
			@SuppressLint("DefaultLocale")
			@Override
			public void onClick(View v) {
				//make controller for current claim
				Intent intent = getIntent();
			    int claimId = intent.getIntExtra(Constants.claimIdLabel, 0);
			    claimController = new ClaimController(new Claim(claimId));
			    
			    //ensure a valid date has been entered
			    if (date == null) {
					Toast.makeText(getApplicationContext(), "Please include a date", Toast.LENGTH_SHORT).show();
			    }
			    else if (date.before(claimController.getStartDate())) {
					Toast.makeText(getApplicationContext(), "Please include a date after claim's start date", Toast.LENGTH_SHORT).show();
				}
			    else {
					Calendar cal = Calendar.getInstance();
				    cal.setTime(date);
				    cal.add(Calendar.DATE, -1);
				    Date compareDate = cal.getTime();
					if (compareDate.after(claimController.getEndDate())) {
						Toast.makeText(getApplicationContext(), "Please include a date before claim's end date", Toast.LENGTH_SHORT).show();
					}
					else {
						//create new Expense, fill in values, attach to claim, close activity
									    			    
					    //make controller for new expense
						expenseController = new ExpenseController(new Expense());
						
						//fill in category
						String category = (String)categorySpinner.getSelectedItem();
						expenseController.setCategory(category);
						
						//fill in date
						expenseController.setDate(date);
						
						//fill in amount
						String costString = amountEditText.getText().toString();
						if (costString.equals(".")) {
							costString = "0";
						}
						else if (costString.isEmpty()) {
							costString = "0";
						}
						costString = String.format("%.2f", Double.valueOf(costString));
						double amount = Double.valueOf(costString);
						expenseController.setCost(amount);
						
						//fill in currency
						String currency = (String)currencySpinner.getSelectedItem();
						expenseController.setCurrency(currency);
						
						//fill in description
						String description = descriptionEditText.getText().toString();
						expenseController.setDescription(description);
						
						//set location
						expenseController.setLocation(location);
						
						try {
							
							 // If delete button visible, receipt attached 
							if (deleteReceiptButton.getVisibility() == View.VISIBLE) {
								expenseController.setReceiptStatus(true);
							}
							else if (deleteReceiptButton.getVisibility() == View.INVISIBLE) {
								expenseController.setReceiptStatus(false);
							}
							
							receiptController.clearReceiptFiles();
							
							expenseController.setReceiptPhoto(receiptController.getReceiptPhoto());
							
							// Throws exception
							claimController.addExpense(expenseController.getExpense());					
							
							// Refresh the claim list
							ClaimListController claimListController = new ClaimListController();
							claimListController.removeClaim(claimId);
							claimListController.addClaim(new Claim(claimId));
							
							setResult(RESULT_OK);
							
							// Go back to ExpenseList
							finish();					
							
						} catch (UserInputException e) {
							System.out.println(e.getMessage());
							Toast.makeText(getApplicationContext(),
									e.getMessage(), Toast.LENGTH_SHORT).show();
						}						

					}
				}
			}
		});
	}
	
	
	
	
	/**
	 * 	gets a picture from the user to attach as the receipt for the expense.
	 */
	public void takeReceiptPhoto() {
		
		// Create the receipts folder if it doesn't exist yet
		String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/receipts_working";
		File folderFile = new File(folderPath);
		if (!folderFile.exists()) {
			folderFile.mkdir();
		}
		
		// Make a new receipt photo file
		receiptPath = folderPath + "/"+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		File receiptPhoto = new File(receiptPath);
		
		// Get a URI for the file
		receiptPhotoUri = Uri.fromFile(receiptPhoto);
		
		// Store the picture
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, receiptPhotoUri);
		
		startActivityForResult(intent, NewExpenseActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}	
	
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;	
	/**
	 * 	Whenever an activity returns with a result this method is called. This is used for
	 *  saving a receipt and editing the expense location.
	 */
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
			
			if (resultCode == RESULT_OK){
				
				File receiptPhotoFile = new File(receiptPath);
				if (receiptPhotoFile.length() > Constants.MAX_PHOTO_SIZE){
					Toast.makeText(this, 
							"The receipt image cannot exceed " + Long.toString(Constants.MAX_PHOTO_SIZE / 1024) + " KB", 
							Toast.LENGTH_SHORT).show();	
				} else {			
					receiptController.saveReceiptPhoto(receiptPath);
				}	
				
			}
			
		}
		// for editing location
		// //http://stackoverflow.com/questions/10407159/how-to-manage-start-activity-for-result-on-android/10407371#10407371 2015-03-31
		if (requestCode == 1) {
	        if(resultCode == RESULT_OK){
	        	location = new Location("Expense Location");
	        	location.setLatitude(data.getDoubleExtra("latitude", 999));
	        	location.setLongitude(data.getDoubleExtra("longitude", 999));
	        	locationTextView.setText("Lat: " + String.format("%.4f", location.getLatitude()) 
	        			+ "\nLong: " + String.format("%.4f", location.getLongitude()));
	        	locationButton.setText("Edit/View Location");
	        	
	        }
	        if (resultCode == RESULT_CANCELED) {
	        }
	    }
		
	}
	
	/**
	 * 	Sets the receipt view to the given photo.
	 * @param the photo receipt the expense is getting set to.
	 */
	protected void setReceiptPhoto(Receipt receipt){
		
		if (receiptController.getReceiptPhoto() != null) {
			
			// Get the receipt photo
			byte[] decodedString = Base64.decode(receiptController.getReceiptPhoto(), Base64.DEFAULT);
			Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
			
			// Update the receipt area
			receiptThumbnail.setImageBitmap(decodedByte);
			addReceiptText.setText("View Attached Receipt");
			deleteReceiptButton.setVisibility(View.VISIBLE);
			receiptThumbnail.setClickable(true);
			
		} else {
			
			// Reset the receipt area 
			// http://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview, 2015-03-28
			receiptThumbnail.setImageDrawable(null);
			addReceiptText.setText("No Receipt Attached");
			deleteReceiptButton.setVisibility(View.INVISIBLE);
			receiptThumbnail.setClickable(false);
		}
		
	}
	
	/**
	 * 	Updates the receipt view when called.
	 */
	public void update() {
		setReceiptPhoto(receipt);
	}
	
	/**
	 * 	Called whenever the add location button is pressed and allows 
	 * 	the user to add or edit a location to the current expense.
	 * @param v - the view when the add location button is pressed
	 */
	public void addLocation(View v) {
		AlertDialog.Builder builder = new AlertDialog.Builder(NewExpenseActivity.this);
		builder.setTitle("Attaching a location")
		.setCancelable(true)
		.setItems(R.array.location_menu, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if (which == 0) {
					GPSLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					if (GPSLocation == null) {
			        	Toast.makeText(getApplicationContext(), "GPS currently unavailable", Toast.LENGTH_SHORT).show();
					}
					else {
						location = GPSLocation;
						locationTextView.setText("Lat: " + String.format("%.4f", location.getLatitude()) 
			        			+ "\nLong: " + String.format("%.4f", location.getLongitude()));
						locationButton.setText("Edit Location");
						
					}
				}
				if (which == 1) {
					Intent intent = new Intent(getApplicationContext(), SetExpenseLocationActivity.class);
					if (location == null) {
						intent.putExtra("latitude",999);
				    	intent.putExtra("longitude",999);
					}
					else {
						intent.putExtra("latitude",location.getLatitude());
						intent.putExtra("longitude",location.getLongitude());
					}
					startActivityForResult(intent, 1);
				}
				if (which == 2) {
					location = null;
		        	locationTextView.setText(null);
		        	locationTextView.setHint("Location not set");
		        	locationButton.setText("Add Location");
				}
			}
			
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	
	// TESTING METHODS
	/**
	 * Used for testing
	 * @param date the new expense was incurred
	 */
	public void setDate(Date date) {
		this.date = date;
	}

}