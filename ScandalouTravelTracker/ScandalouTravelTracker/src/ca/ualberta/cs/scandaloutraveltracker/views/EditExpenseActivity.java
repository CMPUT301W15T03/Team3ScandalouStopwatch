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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.DatePickerFragment;
import ca.ualberta.cs.scandaloutraveltracker.R;
import ca.ualberta.cs.scandaloutraveltracker.StateSpinner;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.R.id;
import ca.ualberta.cs.scandaloutraveltracker.R.layout;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ExpenseController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ReceiptController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
import ca.ualberta.cs.scandaloutraveltracker.models.Receipt;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 *  Activity takes an expense from the ExpenseListActivity and allows 
 *  the user to edit information about the expense.
 * @author Team3ScandalouStopwatch
 *
 */
public class EditExpenseActivity extends MenuActivity implements ViewInterface {
	
	private static final String receiptPathLabel = "ca.ualberta.cs.scandaloutraveltracker.receiptPath";	
	
	private Receipt receipt = new Receipt();
	private ClaimController claimController;
	private ExpenseController expenseController;
	private ReceiptController receiptController;
	private int claimId;
	private int expenseId;
	private Date newDate;
	private boolean canEdit;
	private ImageButton imageButton;
	private ImageButton deleteReceiptButton;
	private Uri receiptPhotoUri;
	private String newReceiptPath; // shouldn't be a global; will figure out better way later
	private TextView addReceiptText;
	private int toastCount;
	private TextView locationTextView;
	
	@SuppressLint("ClickableViewAccessibility") @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_expense);
		
		//initialize fields
		EditText description = (EditText) findViewById(R.id.description);
		final EditText date = (EditText) findViewById(R.id.date_expense);
		EditText cost = (EditText) findViewById(R.id.amount);
		StateSpinner category = (StateSpinner) findViewById(R.id.catspinner);
		StateSpinner currencyType = (StateSpinner) findViewById(R.id.currencyspinner);
		imageButton = (ImageButton) findViewById(R.id.edit_expense_add_receipt);
		addReceiptText = (TextView) findViewById(R.id.edit_expense_add_receipt_text);
		deleteReceiptButton = (ImageButton) findViewById(R.id.edit_expense_delete_receipt);
		deleteReceiptButton.setVisibility(View.INVISIBLE);
		locationTextView = (TextView) findViewById(R.id.edit_location_edit_text);
		
		Button editButton = (Button) findViewById(R.id.edit_expense_button);

		
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
			
			String categoryString = claimController.getExpense(expenseId).getCategory();
			String currencyString = claimController.getExpense(expenseId).getCurrencyType();
		
			//set fields to correct values
			description.setText(claimController.getExpense(expenseId)
					.getDescription());
			date.setText(claimController.getExpense(expenseId)
					.getDateString());
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
			receipt = new Receipt(claimController.getExpense(expenseId).getReceiptPath());
			receiptController = new ReceiptController(receipt);
			setReceiptPhoto(receipt);
			
			receiptController.addView(this); // make this nicer later
			
		}
		
		// Sets all the layout elements if the claim can't be edited
		if (!canEdit) {
			description.setFocusable(false);
			date.setFocusable(false);
			cost.setFocusable(false);
			category.setEnabled(false);
			currencyType.setEnabled(false);
			
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
		imageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			
				if (!canEdit) {
					
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
				}
				else {
					takeReceiptPhoto();
				}
			}
		});
		
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
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	//is called when edit button is clicked
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
			
			//check multiple user input errors and get them to correct accordingly
			//category is required
			if (categoryString.equals("--Choose Category--")) {
				Toast.makeText(this, "Please include a category", Toast.LENGTH_SHORT).show();
				return;
			}
			//cost is required
			else if (costString.equals("")) {
				cost.setError("Please include an amount");
				cost.requestFocus();
				return;
			}
			//date is required
			else if (dateString.equals("")) {
				Toast.makeText(getApplicationContext(), "Please include a date", Toast.LENGTH_SHORT).show();
				return;
			}
			//currency is required
			else if (currencyTypeString.equals("--Choose Currency--")) {
				Toast.makeText(this, "Please include a currency", Toast.LENGTH_SHORT).show();
				return;
			}
			//description is required
			else if (descrString.equals("")) {
				description.setError("Please include a description");
				description.requestFocus();
				return;
			}
			//everything is good to be added
			else {			
			
				expenseController = new ExpenseController(new Expense());
				
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
				costString = String.format("%.2f", Double.valueOf(costString));
				double amount = Double.valueOf(costString);
				expenseController.setCost(Double.valueOf(amount));
				
				
				try {
					
					// Throws exception
					receiptController.saveReceiptPhotoForGood();
					
					receiptController.clearOldReceipts();
					
					expenseController.setReceiptPath(receiptController.getReceiptPath());
					
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

	public void takeReceiptPhoto() {
		
		// Create the receipts folder if it doesn't exist yet
		String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/receipts_working";
		File folderFile = new File(folderPath);
		if (!folderFile.exists()) {
			folderFile.mkdir();
		}
		
		// Make a new receipt photo file
		newReceiptPath = folderPath + "/"+ String.valueOf(System.currentTimeMillis()) + ".jpg";
		File receiptPhoto = new File(newReceiptPath);
		
		// Get a URI for the file
		receiptPhotoUri = Uri.fromFile(receiptPhoto);
		
		// Store the picture
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, receiptPhotoUri);
		
		startActivityForResult(intent, EditExpenseActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}	
	
	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
			
			if (resultCode == RESULT_OK){
				receiptController.saveReceiptPhoto(newReceiptPath);		
			}
			
		}
		
	}
	
	protected void setReceiptPhoto(Receipt receipt){
		
		if (receiptController.getReceiptPath() != null){
			
			// Get the receipt photo
			File receiptFile = new File(receiptController.getReceiptPath());
			Uri receiptFileUri = Uri.fromFile(receiptFile);
			Drawable receiptPhoto = Drawable.createFromPath(receiptFileUri.getPath());
			
			// Update the receipt area
			imageButton.setImageDrawable(receiptPhoto);
			deleteReceiptButton.setVisibility(View.VISIBLE);
			addReceiptText.setVisibility(View.INVISIBLE);
			
		} else {
			
			// Reset the receipt area 
			// http://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview, 2015-03-28
			imageButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_camera));
			deleteReceiptButton.setVisibility(View.INVISIBLE);
			addReceiptText.setVisibility(View.VISIBLE);
		}
		
	}
	
	public void update(){
		setReceiptPhoto(receipt);
	}
	
	// Testing methods
	public int getToastCount() {
		return toastCount;
	}
	
	public void editLocation(View v) {
		
	}
}
