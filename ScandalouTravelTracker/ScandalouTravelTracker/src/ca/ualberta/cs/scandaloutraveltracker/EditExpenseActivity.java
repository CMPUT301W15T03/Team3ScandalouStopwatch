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

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
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
	
	private ClaimController claimController;
	private ExpenseController expenseController;
	private int claimId;
	private int expenseId;
	private Date newDate;
	private ClaimMapper mapper;
	private boolean canEdit;
	private Uri imageFileUri;
	private ImageButton imageButton;
	private ImageButton deleteReceiptButton;
	private String receiptPath;
	private TextView addReceiptText;
	
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
			receiptPath = claimController.getExpense(expenseId).getReceiptPath();
			setReceiptPhoto(claimController.getExpense(expenseId).getReceiptPath());
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
				}
			});
			
			date.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
				}
			});
			
			cost.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Toast.makeText(getApplicationContext(),
							claimController.getStatus() + " claims cannot be edited.", Toast.LENGTH_SHORT).show();
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
					takeAPhoto();
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
					deletePhoto();
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
     		   }
				
			}
		});
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	public void update() {
		// TODO Auto-generated method stub
		
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
				expenseController.setReceiptPath(receiptPath);
				
				try {
					
					// Throws exception on failure
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

	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;	
	
	public void takeAPhoto() {

		// If there was already a receipt for this expense, delete it
		if (receiptPath != null){
			File imageFile = new File(receiptPath);
			imageFile.delete();
		}
		
		// Create a folder to store the receipts if necessary
		String folderPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/receipts";
		File folderFile = new File(folderPath);
		if (!folderFile.exists()) {
			folderFile.mkdir();
		}

		// Create a URI for the picture file
		receiptPath = folderPath + "/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
		File imageFile = new File(receiptPath);
		imageFileUri = Uri.fromFile(imageFile);

		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		
		startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
		
	}	

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
			if (resultCode == RESULT_OK){
				setReceiptPhoto(receiptPath);
			} else if (resultCode == RESULT_CANCELED){
				deletePhoto();			
			} else {
				deletePhoto();			
			}
		}
		
	}
	
	protected void setReceiptPhoto(String receiptPath){
		if (receiptPath != null){
			File receiptFile = new File(receiptPath);
			Uri receiptFileUri = Uri.fromFile(receiptFile);
			Drawable receipt = Drawable.createFromPath(receiptFileUri.getPath());
			imageButton.setImageDrawable(receipt);
			deleteReceiptButton.setVisibility(View.VISIBLE);
			addReceiptText.setVisibility(View.INVISIBLE);
		}
	}
	
	public void deletePhoto(){
		File imageFile = new File(receiptPath);
		imageFile.delete();

		// http://stackoverflow.com/questions/8642823/using-setimagedrawable-dynamically-to-set-image-in-an-imageview, 2015-03-28
		imageButton.setImageDrawable(getResources().getDrawable(android.R.drawable.ic_menu_camera));
		deleteReceiptButton.setVisibility(View.INVISIBLE);
		addReceiptText.setVisibility(View.VISIBLE);
		
		receiptPath = null;
		
	}

}
