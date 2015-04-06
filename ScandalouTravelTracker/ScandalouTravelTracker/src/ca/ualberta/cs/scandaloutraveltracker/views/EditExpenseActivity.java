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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.DatePickerFragment;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.StateSpinner;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ExpenseController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ReceiptController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
import ca.ualberta.cs.scandaloutraveltracker.models.Receipt;

/**
 *  Activity takes an expense from the ExpenseListActivity and allows 
 *  the user to edit information about the expense.
 * @author Team3ScandalouStopwatch
 *
 */

public class EditExpenseActivity extends Activity implements ViewInterface {
	
	private Receipt receipt = new Receipt(null);
	private ClaimController claimController;
	private ExpenseController expenseController;
	private ReceiptController receiptController;
	private int claimId;
	private int expenseId;
	private Date newDate;
	private boolean canEdit;
	private Button cancel;
	private ImageButton receiptThumbnail;
	private ImageButton deleteReceiptButton;
	private Uri receiptPhotoUri;
	private String receiptPath; // shouldn't be a global; will figure out better way later
	private TextView addReceiptText;
	private int toastCount;
	private TextView locationTextView;
	private boolean flag;
	private Location location;
	private Button locationButton;
	private LocationManager lm;
	private Location GPSLocation;
	private Menu optionsMenu;
	
	/**
	 * 	Called when the activity is created. Sets up all the views and controllers for 
	 * 	editing the expense depending on the status of the claim it belongs to.
	 */
	@SuppressLint("ClickableViewAccessibility") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		//set up GPS
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		GPSLocation = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		//initialize fields
		EditText description = (EditText) findViewById(R.id.description);
		final EditText date = (EditText) findViewById(R.id.date_expense);
		EditText cost = (EditText) findViewById(R.id.amount);
		StateSpinner category = (StateSpinner) findViewById(R.id.catspinner);
		StateSpinner currencyType = (StateSpinner) findViewById(R.id.currencyspinner);
		receiptThumbnail = (ImageButton) findViewById(R.id.edit_expense_receipt_thumbnail);
		addReceiptText = (TextView) findViewById(R.id.edit_expense_add_receipt_text);
		deleteReceiptButton = (ImageButton) findViewById(R.id.edit_expense_delete_receipt);
		deleteReceiptButton.setVisibility(View.INVISIBLE);
		locationTextView = (TextView) findViewById(R.id.edit_location_edit_text);
		locationButton = (Button) findViewById(R.id.edit_expense_location_button);
		
		Button editButton = (Button) findViewById(R.id.edit_expense_button);
		Button cancel = (Button) findViewById(R.id.edit_expense_cancel);

		
		//makes sure that the position of the claim and corresponding 
		//expense to be edited are actually passed to this activity
		Bundle extras = getIntent().getExtras();
		Intent intent = getIntent();
		claimId = (int) intent.getIntExtra(Constants.claimIdLabel, -1);
		expenseId = (int) extras.getLong("expenseId", -1);
		if (claimId == -1) {
			Toast.makeText(this, "The Claim Position needs to be added to the " +
					"EditExpenseActivity intent before startActivity(intent) is called",
					Toast.LENGTH_LONG).show();
			finish();
		}
		else if (expenseId == -1) {
			Toast.makeText(this, "The Expense Position needs to be added to the " +
					"EditExpenseActivity intent before startActivity(intent) is called",
					Toast.LENGTH_LONG).show();
			finish();
		}
		
		//Set currentClaim to the claim that was selected via intent
		claimController = new ClaimController(new Claim(claimId));
		canEdit = claimController.getCanEdit();
		
		if (claimId != 0) {
			
			expenseController = new ExpenseController(claimController.getExpense(expenseId));
			
			flag = expenseController.getFlag();
			location = expenseController.getLocation();
			if (location == null) {
				locationButton.setText("Add Location");
			} else {
				locationButton.setText("View/Edit Location");
			}
			
			String categoryString = claimController.getExpense(expenseId).getCategory();
			String currencyString = claimController.getExpense(expenseId).getCurrencyType();
		
			//set fields to correct values
			description.setText(claimController.getExpense(expenseId)
					.getDescription());
			date.setText(claimController.getExpense(expenseId)
					.getDateString());
			newDate = claimController.getExpense(expenseId).getDate();
			cost.setText(""+claimController.getExpense(expenseId)
					.getCost());
			category.setSelection(getIndex(category, categoryString));
			currencyType.setSelection(getIndex(currencyType, currencyString));
			if (claimController.getExpense(expenseId).getLocation() == null) {
				locationTextView.setHint("Location not set");
			}
			else {
				locationTextView.setText("Lat: " + String.format("%.4f", 
						claimController.getExpense(expenseId).getLocation().getLatitude()) 
	        				+ "\nLong: " + String.format("%.4f", 
	        						claimController.getExpense(expenseId).getLocation().getLongitude()));
			}
			receipt = new Receipt(claimController.getExpense(expenseId).getReceiptPhoto());
			receiptController = new ReceiptController(receipt);
			setReceiptPhoto(receipt);
			
			receiptController.addView(this); // make this nicer later
			
		}
		
		cancel.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();	
			}
		});
		
		// Sets all the layout elements if the claim can't be edited
		if (!canEdit) {
			description.setFocusable(false);
			date.setFocusable(false);
			cost.setFocusable(false);
			category.setEnabled(false);
			currencyType.setEnabled(false);
			locationButton.setText("View Location");
			
			
			description.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
					toastCount++;
				}
			});
			
			date.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
					toastCount++;
				}
				
			});
			
			cost.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
					toastCount++;
				}
			});
			
			category.setOnTouchListener(new View.OnTouchListener() {
				
				@SuppressLint("ClickableViewAccessibility") @Override
				public boolean onTouch(View v, MotionEvent event) {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
					return false;
				}
			});
			
			currencyType.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
					return false;
				}
			});
			//hide edit button
			editButton.setVisibility(View.INVISIBLE);
		}


		//sets image button for receipt
		receiptThumbnail.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (receiptController.getReceiptPhoto() != null){			
				   Intent intent = new Intent(EditExpenseActivity.this, ReceiptActivity.class);
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
					
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
				}
				else {
					receiptController.saveReceiptPhoto(null);
				}
			}
		});
		
		//date dialog picker
		date.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				
				if (canEdit) {
					DialogFragment newFragment = new DatePickerFragment() {
						@Override
						public void onDateSet(DatePicker view, int year, int monthOfYear,
								int dayOfMonth) {
							String dateString = convertToString(year, monthOfYear, dayOfMonth);
							date.setHint(dateString);
							Calendar cal = Calendar.getInstance();
							cal.set(year, monthOfYear, dayOfMonth);
							Date tmpDate = cal.getTime();
							SimpleDateFormat sdf = new SimpleDateFormat(Constants.dateFormat, Locale.US);
							try {
								newDate = sdf.parse(dateString);
							} catch (ParseException e) {
								throw new RuntimeException();
							}
							newDate = tmpDate;
							date.setText(dateString);
						}
					};
					newFragment.show(getFragmentManager(), "datePicker");
				}
				
				else {
     			   Toast.makeText(getApplicationContext(), 
     					   		  claimController.getStatus() + " claims cannot be edited.", 
     					   		  Toast.LENGTH_SHORT).show();
     			   toastCount++;
     		   }
				
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
		getMenuInflater().inflate(R.menu.edit_expense, menu);
		MenuItem photoItem = menu.findItem(R.id.action_take_photo);
		if(canEdit) {
			photoItem.setVisible(true);
		} else {
			photoItem.setVisible(false);
		}
		optionsMenu = menu;
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
	 * 	Is called when the edit button is clicked. The method checks if everything in the 
	 * 	new version of the expense is permitted and accepts and executes the edit if 
	 * 	everything is and the claim status permits it to.
	 * @param v - the current view
	 */
	@SuppressLint("DefaultLocale")
	public void confirmEdit(View v) {
		
		if (canEdit) {
			
			//initialize fields again
			final EditText description = (EditText) findViewById(R.id.description);
			final EditText date = (EditText) findViewById(R.id.date_expense);
			final EditText cost = (EditText) findViewById(R.id.amount);
			final Spinner category = (Spinner) findViewById(R.id.catspinner);
			final Spinner currencyType = (Spinner) findViewById(R.id.currencyspinner);
			
			//parse the string input from user
			String descrString = description.getText().toString();
			String dateString = date.getText().toString();
			String costString = cost.getText().toString();
			String categoryString = category.getSelectedItem().toString();
			String currencyTypeString = currencyType.getSelectedItem().toString();
			
			//valid date is required
			Calendar cal = Calendar.getInstance();
		    cal.setTime(newDate);
		    cal.add(Calendar.DATE, -1);
		    Date compareDate = cal.getTime();
			if (dateString.length()==0) {
				Toast.makeText(getApplicationContext(), "Please include a date", Toast.LENGTH_SHORT).show();
		    }
		    else if (newDate.before(claimController.getStartDate())) {
				Toast.makeText(getApplicationContext(), "Please include a date after claim's start date", Toast.LENGTH_SHORT).show();
			}
		    else if (compareDate.after(claimController.getEndDate())) {
				Toast.makeText(getApplicationContext(), "Please include a date before claim's end date", Toast.LENGTH_SHORT).show();
			}
			//everything is good to be added
			else {
				
				expenseController = new ExpenseController(new Expense());
				
				expenseController.setFlag(flag);
				expenseController.setLocation(location);
				
				//checks if date is unchanged
				if (dateString.equals(claimController.getExpense(expenseId).getDateString())) {
					expenseController.setDate(claimController.getExpense(expenseId).getDate());
				}
				//change to new date
				else {
					expenseController.setDate(newDate);
				}
				expenseController.setDescription(descrString);
				expenseController.setCategory(categoryString);
				expenseController.setCurrency(currencyTypeString);
				if (costString.equals(".")) {
					costString = "0";
				}
				else if (costString.isEmpty()) {
					costString = "0";
				}
				costString = String.format("%.2f", Double.valueOf(costString));
				double amount = Double.valueOf(costString);
				expenseController.setCost(Double.valueOf(amount));
				
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
					claimController.updateExpense(expenseId, expenseController.getExpense());					
					
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
		else {
			   Toast.makeText(getApplicationContext(), 
					   		  claimController.getStatus() + " claims cannot be edited.", 
					   		  Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 	Get's the index that a given string occurs in the list of a given spinner.
	 * @param a spinner
	 * @param a string that's in the spinner
	 * @return the index in the spinner list that the given string occurs in the given spinner
	 */
	//http://stackoverflow.com/questions/2390102/how-to-set-selected-item-of-spinner-by-value-not-by-position 2015-03-14
	private int getIndex(Spinner spinner, String string) {
		int index = 0;
		for (int i=0;i<spinner.getCount();i++){
			if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(string)){
				index = i;
				break;
			}
		}
		return index;
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
		
		startActivityForResult(intent, EditExpenseActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
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
	 * 	Called when edit/view location is clicked. Gives the user multiple options to
	 * 	setting the location such as clearing the current location and setting the location
	 * 	using their device's gps or a map.
	 * @param v - the current view
	 */
	public void editLocation(View v) {
		claimController = new ClaimController(new Claim(claimId));
		canEdit = claimController.getCanEdit();
		if (!canEdit) {
			Intent intent = new Intent(getApplicationContext(), ViewLocationActivity.class);
			if (location == null) {
				intent.putExtra("latitude",999);
		    	intent.putExtra("longitude",999);
			}
			else {
				intent.putExtra("latitude",location.getLatitude());
				intent.putExtra("longitude",location.getLongitude());
			}
	    	startActivity(intent);
	    	return;
		}
		else {
			AlertDialog.Builder builder = new AlertDialog.Builder(EditExpenseActivity.this);
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
							locationButton.setText("Edit/View Location");
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
			        	locationButton.setText("Add/View Location");
					}
				}
				
			});
			AlertDialog alert = builder.create();
			alert.show();
		}
	}
	
	// Testing methods
	/**
	 * 	Used for testing
	 * @return the number of toasts corresponding with how many views
	 *  were clicked that aren't allowed to be edited
	 */
	public int getToastCount() {
		return toastCount;
	}
	
	/**
	 * Used for testing that the camera icon is shown when canEdit is true
	 * @return Menu containing the options menu for this activity
	 */
	public Menu getOptionsMenu() {
		return optionsMenu;
	}
	
}