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

import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.models.Receipt;
import ca.ualberta.cs.scandaloutraveltracker.views.ViewInterface;

import android.os.Environment;

public class ReceiptController {

	Receipt receipt;
	
	public ReceiptController (Receipt receipt){
		this.receipt = receipt;
	}
	
	public String getReceiptPath(){
		return receipt.getReceiptPath();
	}
	
	public void saveReceiptPhoto(String newReceiptPath){
		receipt.saveReceiptPhoto(newReceiptPath);
	}
	
	
	public void saveReceiptPhotoForGood() throws UserInputException {
		receipt.saveReceiptPhotoForGood();
	}
	
	public void clearOldReceipts (){
		File tempReceiptFolderFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/receipts_working");
		if (tempReceiptFolderFile.exists()) {
			File[] photoFiles = tempReceiptFolderFile.listFiles();
			for (File file : photoFiles){
				file.delete();
			}
		}
		
		File receiptFolderFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/receipts");
		if (receiptFolderFile.exists()) {
			File[] photoFiles = receiptFolderFile.listFiles();
			for (File file : photoFiles){
				if (receipt.getReceiptPath() != null  && !receipt.getReceiptPath().equals(file.getAbsolutePath())){
					file.delete();
				}
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