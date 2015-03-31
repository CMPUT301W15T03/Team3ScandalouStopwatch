package ca.ualberta.cs.scandaloutraveltracker;


import android.net.Uri;
import java.io.File;

import ca.ualberta.cs.scandaloutraveltracker.views.EditExpenseActivity;
import android.os.Environment;
import android.content.Intent;
import android.provider.MediaStore;

public class Photo {
	private Uri imageFileUri;

	public Uri getImageFileUri() {
		return imageFileUri;
	}

	public void setImageFileUri(Uri imageFileUri) {
		this.imageFileUri = imageFileUri;
	}

	public void takeAPhoto(EditExpenseActivity editExpenseActivity) {
		if (editExpenseActivity.getReceiptPath() != null) {
			File imageFile = new File(editExpenseActivity.getReceiptPath());
			imageFile.delete();
		}
		String folderPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/receipts";
		File folderFile = new File(folderPath);
		if (!folderFile.exists()) {
			folderFile.mkdir();
		}
		editExpenseActivity.setReceiptPath(folderPath + "/"
				+ String.valueOf(System.currentTimeMillis()) + ".jpg");
		File imageFile = new File(editExpenseActivity.getReceiptPath());
		imageFileUri = Uri.fromFile(imageFile);
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
		editExpenseActivity.startActivityForResult(intent,
				EditExpenseActivity.CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
	}
}