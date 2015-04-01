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
