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

package ca.ualberta.cs.scandaloutraveltracker.controllers;

import java.io.File;

import android.os.Environment;
import ca.ualberta.cs.scandaloutraveltracker.models.Receipt;
import ca.ualberta.cs.scandaloutraveltracker.views.ViewInterface;

/**
 * Used for editing receipt files. Should be used instead of directly
 * editing receipt files.
 * @author Team3ScandalouStopwatch
 *
 */
public class ReceiptController {

	Receipt receipt;
	
	/**
	 * Constructor of the controller
	 * @param receipt The receipt you wish to edit
	 */
	public ReceiptController (Receipt receipt){
		this.receipt = receipt;
	}
	
	/**
	 * Get the receipt photo's path
	 * @return String that is the path to the receipt photo
	 */
	public String getReceiptPhoto(){
		return receipt.getReceiptPhoto();
	}	
	
	/**
	 * Saves the receipt file given the receiptPath
	 * @param receiptPath is the location where you want to save the photo
	 */
	public void saveReceiptPhoto(String receiptPath) {
		receipt.saveReceiptPhoto(receiptPath);
	}
	
	/**
	 * Clears the receipt file of the receipt that is passed to the controller
	 * upon construction. The receipt will be empty after this command.
	 */
	public void clearReceiptFiles(){
		File tempReceiptFolderFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/receipts_working");
		if (tempReceiptFolderFile.exists()) {
			File[] photoFiles = tempReceiptFolderFile.listFiles();
			for (File file : photoFiles){
				file.delete();
			}
		}
	}
	
	/**
	 * 
	 * @param view
	 */
	public void addView(ViewInterface view) {
		receipt.addView(view);
	}	
	
}
