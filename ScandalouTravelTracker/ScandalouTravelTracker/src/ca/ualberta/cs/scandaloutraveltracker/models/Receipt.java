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

package ca.ualberta.cs.scandaloutraveltracker.models;


import android.net.Uri;
import java.io.File;

import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import android.os.Environment;
import android.content.Intent;
import android.provider.MediaStore;

public class Receipt extends SModel {
	
	private String receiptPath;
	private Uri imageFileUri;
	
	public static final long MAX_RECEIPT_SIZE = 5*1024*1024; // 5 MB	

	public Receipt(String receiptPath){
		this.receiptPath = receiptPath;
	}
	
	/* Tried this, doesn't work. Instead we'll just save receipt paths in the expenses, rather than whole receipt objects
	 * 
	 * Apparently a default constructor is necessary for GSON to serialize
	 * classes in shared prefs in a way that isn't prone to cause stack
	 * overflows, which is what was happening to me
	 * CITATION: http://stackoverflow.com/questions/28743933/android-gson-tojson-throws-stackoverflowerror-on-arraylistoverlayitem, 2015-03-31
	 * 
	 * 
	 */
	public Receipt(){
		receiptPath = null;
	}
	
	// Getters and setters
	
	public String getReceiptPath() {
		return receiptPath;
	}

	public void setReceiptPath(String receiptPath) {
		this.receiptPath = receiptPath;
	}
	
	public Uri getImageFileUri() {
		return imageFileUri;
	}

	public void setImageFileUri(Uri imageFileUri) {
		this.imageFileUri = imageFileUri;
	}
	
	// Other methods
	
	public long getPhotoSize(){
		File receiptPhoto = new File(receiptPath);
		return receiptPhoto.length();
	}	
	
	public void saveReceiptPhoto(String newReceiptPath){

		receiptPath = newReceiptPath;
		
		notifyViews();	
	}
	
	public void saveReceiptPhotoForGood() throws UserInputException {

		if (receiptPath != null){
			if (getPhotoSize() > MAX_RECEIPT_SIZE)
				throw new UserInputException("The receipt image cannot exceed " +
						Long.toString(MAX_RECEIPT_SIZE / (1024*1024)) + " MB");
			
			// Create the receipts folder if it doesn't exist yet
			String folderPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/receipts";
			File folderFile = new File(folderPath);
			if (!folderFile.exists()) {
				folderFile.mkdir();
			}		

			// Get the temp receipt path; get the file
			String fromReceiptPath = receiptPath;
			File fromReceiptPhoto = new File(fromReceiptPath);
			
			// Make a permanent receipt path; make a file
			String toReceiptPath = folderPath + "/"+ String.valueOf(System.currentTimeMillis()) + ".jpg";
			File toReceiptPhoto = new File(toReceiptPath);			
			
			// Save to the permanent location
			fromReceiptPhoto.renameTo(toReceiptPhoto);
			
			// Delete the temp receipt file
			fromReceiptPhoto.delete();
			
			receiptPath = toReceiptPath;			
		}
	}
	
}