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
/**
 *  The class Receipt that gets an image of a receipt and changes it to a string.
 * @author Team3ScandalouStopwatch
 *
 */
public class Receipt extends SModel {
	
	private String receiptPath;
	private Uri imageFileUri;
	
	public static final long MAX_RECEIPT_SIZE = 5*1024*1024; // 5 MB	
    
	public Receipt(String receiptPath){
		this.receiptPath = receiptPath;
	}
	
	/*
	 * 
	 * default constructor is necessary for GSON to serialize
	 * classes in shared prefs in a way that isn't prone to cause stack
	 * overflows
	 * CITATION: http://stackoverflow.com/questions/28743933/android-gson-tojson-throws-stackoverflowerror-on-arraylistoverlayitem, 2015-03-31
	 * 
	 * 
	 */
	public Receipt(){
		receiptPath = null;
	}
	
	// Getters and setters
	/**
	 * @return receiptPath
	 */
	public String getReceiptPath() {
		return receiptPath;
	}
	/**
	 * 
	 * @param receiptpath for the current expense
	 */
	public void setReceiptPath(String receiptPath) {
		this.receiptPath = receiptPath;
	}
	/**
	 * @return image file
	 */
	public Uri getImageFileUri() {
		return imageFileUri;
	}
	/**
	 * 
	 * @param image file for the current expense
	 */

	public void setImageFileUri(Uri imageFileUri) {
		this.imageFileUri = imageFileUri;
	}
	
	// Other methods
	/**
	 * @return the photo file
	 */
	public File getPhotoFile(){
		return new File(receiptPath);
	}
	/**
	 * @return size of the photo
	 */
	public long getPhotoSize(){
		File receiptPhoto = new File(receiptPath);
		return receiptPhoto.length();
	}	
	/**
	 * saves a receipt photo, allow changes to it
	 */
	public void saveReceiptPhoto(String newReceiptPath){

		receiptPath = newReceiptPath;
		
		notifyViews();	
	}
	
	/**
	 * saves a receipt photo but doesnt allow changes to it
	 */
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