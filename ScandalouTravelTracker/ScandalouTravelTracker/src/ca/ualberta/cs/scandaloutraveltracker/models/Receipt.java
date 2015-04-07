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


import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

/**
 *  The class Receipt that gets an image of a receipt and changes it to a string.
 * @author Team3ScandalouStopwatch
 *
 */
public class Receipt extends SModel {
	
	private String receiptPhoto;
    
	/**
	 * Constructor
	 * @param receiptPhoto path to the photo
	 */
	public Receipt(String receiptPhoto){
		this.receiptPhoto = receiptPhoto;
	}
	
	// Getters and setters
	/**
	 * @return receiptPhoto
	 */
	public String getReceiptPhoto() {
		return receiptPhoto;
	}
	/**
	 * 
	 * @param receiptphoto for the current expense
	 */
	public void setReceiptPhoto(String receiptPhoto) {
		this.receiptPhoto = receiptPhoto;
	}
	
	// Other methods	
	/**
	 * saves a receipt photo, allow changes to it
	 */
	public void saveReceiptPhoto(String newReceiptPath){// throws UserInputException {

		/*if (getPhotoSize(receiptPath) > MAX_RECEIPT_SIZE){
			throw new UserInputException("The receipt image cannot exceed " +
					Long.toString(MAX_RECEIPT_SIZE / (1024*1024)) + " MB");
		}*/
		
		// CITATION http://stackoverflow.com/questions/4830711/how-to-convert-a-image-into-base64-string, 2015-04-07
		if (newReceiptPath == null){
			receiptPhoto = null;
		} else {
			Bitmap bm = BitmapFactory.decodeFile(newReceiptPath);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();  
			bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object   
			byte[] b = baos.toByteArray();
			receiptPhoto = Base64.encodeToString(b, Base64.DEFAULT);
		}	
		
		notifyViews();	
	}
	
}