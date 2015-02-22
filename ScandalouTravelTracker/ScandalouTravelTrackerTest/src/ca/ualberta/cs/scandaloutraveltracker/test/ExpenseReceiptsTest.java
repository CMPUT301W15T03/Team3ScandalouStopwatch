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

package ca.ualberta.cs.scandaloutraveltracker.test;

import java.io.File;

import junit.framework.TestCase;

public class ExpenseReceiptsTest extends TestCase {

	// Test UC 06.01.01
	public void testAttachPhoto() {
		File photo = new File("TestPhoto1.jpg");	//a test photo that complies with the size limit
		Expense expense = new Expense();
		expense.addPhoto(photo);
		assertTrue("Attach failed", (expense.getPhoto() == photo));
	}

	// Test UC 06.02.01
	public void testViewPhoto() {
		File photo = new File("TestPhoto1.jpg");	//a test photo that complies with the size limit
		Expense expense = new Expense();
		expense.addPhoto(photo);
		assertTrue("View failed", (expense.getPhoto() != null));
	}

	// Test UC 06.03.01
	public void testDeletePhoto() {
		File photo = new File("TestPhoto1.jpg");	//a test photo that complies with the size limit
		Expense expense = new Expense();
		expense.addPhoto(photo);
		expense.deletePhoto();
		assertTrue("Delete failed", (expense.getPhoto() == null));
		assertFalse("Photo still exists", photo.exists());
	}

	// Test UC 06.01.01
	public void testPhotoSizeLimit() {
		File badPhoto = new File("TestPhoto2.jpg");	//a test photo that doesn't comply with the size limit
		Expense expense = new Expense();
		expense.addPhoto(badPhoto);
		assertTrue("Size guard failed", (expense.getPhoto() == null));
	}

}
