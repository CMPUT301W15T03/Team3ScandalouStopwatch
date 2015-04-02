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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.UserInputException;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.UserListController;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;
import ca.ualberta.cs.scandaloutraveltracker.models.Destination;
import ca.ualberta.cs.scandaloutraveltracker.models.Expense;
import ca.ualberta.cs.scandaloutraveltracker.models.User;

public class ClaimGenerator {
	UserListController ulc;
	ClaimListController clc;
	
	public ClaimGenerator() {
		
		try {
			ulc = new UserListController();
		} catch (NullPointerException e) {

		}
		
		try {
			clc = new ClaimListController();
		} catch (NullPointerException e) {
			
		}
	}
	
	
	// User to start the tests with an empty claim list
	public void clearCL() {
		ArrayList<Claim> claims = clc.getClaimList().getClaims();
		Iterator<Claim> iterator = claims.iterator();
		
		while (iterator.hasNext()) {
			Claim currentClaim = iterator.next();
			int id = currentClaim.getId();
			clc.deleteClaim(id);
			iterator.remove();
		}
	}
	
	// Used to start the tests with an empty user list
	public void clearUL() {
		// Initialize UserListController
		try {
			ArrayList<User> users = ulc.getUserList().getUsers();
			Iterator<User> iterator = users.iterator();
			
			while (iterator.hasNext()) {
				User user = iterator.next();
				int id = user.getId();
				ulc.deleteUser(id);
				iterator.remove();
			}
		} catch (NullPointerException e) {
			
		}
	}
	
	// Used to create two users and have one claim associated with each
	public void makeTwoUsersWithClaims() throws UserInputException {
		// Create two users and add them to the list
		int userId = ulc.createUser("User1");
		ulc.addUser(new User(userId));
		int userId2 = ulc.createUser("User2");
		ulc.addUser(new User(userId2));
		
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		ClaimListController clc = new ClaimListController();
		String status = Constants.statusInProgress;
		ArrayList<String> tagsList = new ArrayList<String>();
		boolean canEdit = true;
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		
		// Create the claim
		int newClaimId = clc.createClaim(new Date(), new Date(), "d1", destinations, 
				tagsList, status, canEdit, expenses, new User(userId));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
		
		// Create another ClaimList associated with user2
		newClaimId = clc.createClaim(new Date(), new Date(), "d2", destinations, 
				tagsList, status, canEdit, expenses, new User(userId2));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
	}
	
	public int createMockClaim(boolean editable, boolean expensesIncluded, boolean tagsIncluded, 
			boolean destinationsIncluded) throws UserInputException {
		// Create user and add to the list
		UserListController ulc = new UserListController();
		int userId = ulc.createUser("User1");
		ulc.addUser(new User(userId));
		
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		ClaimListController clc = new ClaimListController();
		String status = Constants.statusInProgress;
		ArrayList<String> tagsList = new ArrayList<String>();
		boolean canEdit = editable;
		ArrayList<Expense> expenses = new ArrayList<Expense>();	
		
		if (expensesIncluded) {
			Expense newExpense = new Expense();
			newExpense.setDate(createDate(2,2,2014));
			newExpense.setCurrencyType("CAD");
			newExpense.setCost(2.25);
			newExpense.setDescription("Test Expense");
			newExpense.setCategory("Parking");
			newExpense.setFlag(true);
			newExpense.setReceiptAttached(true);
			newExpense.setLocationAttached(true);
			expenses.add(newExpense);
		}
		
		if (tagsIncluded) {
			tagsList.add("#harlem");
			tagsList.add("#bedstuy");
		}
		
		if (destinationsIncluded) {
			Destination d1 = new Destination("Harlem", "WORK vid shoot");
			destinations.add(d1);
			d1 = new Destination("Brooklyn", "Meet with Jay");
			destinations.add(d1);
		}
		
		// Month - Day - Year
		Date startDate = createDate(2, 1, 2014);
		Date endDate = createDate(2, 3, 2014);
		
		// Create the claim
		int newClaimId = clc.createClaim(startDate, endDate, "d1", destinations, 
				tagsList, status, canEdit, expenses, new User(userId));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
		
		return newClaimId;
	}

	public void createClaimWithTags(int userId, ArrayList<String> tags, Date startDate, Date endDate) throws UserInputException {
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		Destination destination = new Destination("Brooklyn", "Meet with Jay");
		destinations.add(destination);
		ClaimListController clc = new ClaimListController();
		ArrayList<String> tagsList = tags;
		boolean canEdit = true;
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		
		// Create the claim
		int newClaimId = clc.createClaim(startDate, endDate, "d1", destinations, 
				tagsList, Constants.statusInProgress, canEdit, expenses, new User(userId));	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
	}
	
	public void createSubmittedClaim(int userId, Date startDate, Date endDate) throws UserInputException {
		// Create one ClaimList associated with user1
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		Destination destination = new Destination("Brooklyn", "Meet with Jay");
		destinations.add(destination);
		ClaimListController clc = new ClaimListController();
		ArrayList<String> tagsList = new ArrayList<String>();
		ArrayList<Expense> expenses = new ArrayList<Expense>();
		int newClaimId = 0;
		tagsList.add("");
		tagsList.add("#NY");
		Expense newExpense = new Expense();
		newExpense.setCurrencyType("USD");
		newExpense.setCost(5.00);
		expenses.add(newExpense);
		
		// Create the claim
		try {
			newClaimId = clc.createClaim(startDate, endDate, "d1", destinations, 
					tagsList, Constants.statusSubmitted, false, expenses, new User(userId));
		} catch (UserInputException e) {
			throw e;
		}	
		
		// Add the claim to list
		clc.addClaim(new Claim(newClaimId));
	}
	
	public void createClaims_Tagged(int newUserId) throws UserInputException {
		Date startDate;
		Date endDate;
		
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("#tag1");
		tags.add("#tag2");
		startDate = createDate(0, 1, 2014);
		endDate= createDate(0, 2, 2014);
		createClaimWithTags(newUserId, tags, startDate, endDate);
		
		tags = new ArrayList<String>();
		tags.add("#tag1");
		tags.add("#tag3");
		tags.add("#tag4");
		startDate = createDate(0, 1, 2013);
		endDate= createDate(0, 2, 2013);
		createClaimWithTags(newUserId, tags, startDate, endDate);
		
		tags = new ArrayList<String>();
		tags.add("#tag5");
		startDate = createDate(0, 1, 2012);
		endDate= createDate(0, 2, 2012);
		createClaimWithTags(newUserId, tags, startDate, endDate);
	}
	
	public Date createDate(int month, int day, int year) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		Date date = cal.getTime();
		
		return date;
	}
	
	public void resetState(Context context) {
		clearUL();
		clearCL();
		
		SharedPreferences counterFile;
		Editor editor;
		
		counterFile = context.getSharedPreferences("userCounter", 0);
		editor = counterFile.edit();
		editor.putInt("userCount", 0);
		editor.commit();
		
		counterFile = context.getSharedPreferences("claimCounter", 0);
		editor = counterFile.edit();
		editor.putInt("claimCount", 0);
		editor.commit();
	}
}
