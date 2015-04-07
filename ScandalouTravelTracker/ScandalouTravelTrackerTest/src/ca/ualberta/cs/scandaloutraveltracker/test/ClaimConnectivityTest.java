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

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import junit.framework.TestCase;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.Constants;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.mappers.ClaimMapper;
import ca.ualberta.cs.scandaloutraveltracker.mappers.OnlineMapper;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ClaimConnectivityTest extends TestCase {
	
	private Context context;
	
	private Gson gson = new Gson();
	private OnlineMapper onlineMapper;
	private ClaimMapper claimMapper;
	
	ClaimGenerator cg = new ClaimGenerator();
	ClaimListController claimListController;
	
	int waitTime = 6000; // 6 seconds
	
	QueryObject searchResult;
	
	// Tests that a claim gets pushed online when created (assuming there's an internet connection)
	// US 09.01.01
	public void testPushOnClaimCreation() throws Throwable {

		// CITATION http://stackoverflow.com/questions/6516441/why-does-androidtestcase-getcontext-getapplicationcontext-return-null, 2015-04-06
		context = ClaimApplication.getContext();
		while (context == null){
			context = ClaimApplication.getContext();
		}		
		
		onlineMapper = new OnlineMapper(ClaimApplication.getContext());	
		claimMapper = new ClaimMapper(ClaimApplication.getContext());
		
		// Generate a mock claim
		int newClaimId = cg.createMockClaim(true, true, true, true);
		Claim mockClaim = new Claim(newClaimId);
		
		newClaimId++; // ID of claim that will be created

		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);		
		
		// Search the server at the claim id we just made
		searchResult = searchServer(newClaimId);
			
		// There should be 0 claims
		assertEquals(0, searchResult.getTotalHits());
			
		// Save the claim
		claimListController = new ClaimListController();
		claimListController.createClaim(mockClaim.getStartDate(), mockClaim.getEndDate(), 
				mockClaim.getDescription(), mockClaim.getDestinations(), mockClaim.getTags(),
				mockClaim.getStatus(), mockClaim.getCanEdit(), mockClaim.getExpenses(), 
				mockClaim.getUser());
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);
		
		// Search the server at the claim id we just made again
		searchResult = searchServer(newClaimId);
		assertEquals(1, searchResult.getTotalHits());
		assertEquals(mockClaim.getDescription(), searchResult.getRetrievedDescription());
			
	}

	// Tests that a claim gets pushed online when updated (assuming there's an internet connection)
	// US 09.01.01
	public void testPushOnClaimUpdate() throws Throwable {

		// CITATION http://stackoverflow.com/questions/6516441/why-does-androidtestcase-getcontext-getapplicationcontext-return-null, 2015-04-06
		context = ClaimApplication.getContext();
		while (context == null){
			context = ClaimApplication.getContext();
		}		
		
		onlineMapper = new OnlineMapper(ClaimApplication.getContext());	
		claimMapper = new ClaimMapper(ClaimApplication.getContext());
		
		// Generate a mock claim
		int newClaimId = cg.createMockClaim(true, true, true, true);
		Claim mockClaim = new Claim(newClaimId);
		
		newClaimId++; // ID of claim that will be created
		
		// Save the claim
		claimListController = new ClaimListController();
		claimListController.createClaim(mockClaim.getStartDate(), mockClaim.getEndDate(), 
				"Test 1", mockClaim.getDestinations(), mockClaim.getTags(),
				mockClaim.getStatus(), mockClaim.getCanEdit(), mockClaim.getExpenses(), 
				mockClaim.getUser());
		
		mockClaim = new Claim(newClaimId);
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);		
		
		// Search the server at the claim id we just made
		searchResult = searchServer(newClaimId);
		assertEquals(1, searchResult.getTotalHits());
		assertEquals(mockClaim.getDescription(), searchResult.getRetrievedDescription());
			
		// Change the claim
		ClaimController claimController = new ClaimController(mockClaim);
		claimController.updateClaim(mockClaim.getStartDate(), mockClaim.getEndDate(), 
				"Test 2", mockClaim.getDestinations(), mockClaim.getCanEdit());
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);	
		
		// Search the server at the claim id we just made again
		searchResult = searchServer(newClaimId);
		assertEquals(1, searchResult.getTotalHits());
		assertEquals(mockClaim.getDescription(), searchResult.getRetrievedDescription());
			
	}
	
	// Tests that a claim gets deleted online when deleted locally (assuming there's an internet connection)
	// US 09.01.01
	public void testPushOnClaimDelete() throws Throwable {

		// CITATION http://stackoverflow.com/questions/6516441/why-does-androidtestcase-getcontext-getapplicationcontext-return-null, 2015-04-06
		context = ClaimApplication.getContext();
		while (context == null){
			context = ClaimApplication.getContext();
		}		
		
		onlineMapper = new OnlineMapper(ClaimApplication.getContext());	
		claimMapper = new ClaimMapper(ClaimApplication.getContext());
		
		// Generate a mock claim
		int newClaimId = cg.createMockClaim(true, true, true, true);
		Claim mockClaim = new Claim(newClaimId);
		
		newClaimId++; // ID of claim that will be created
		
		// Save the claim
		claimListController = new ClaimListController();
		claimListController.createClaim(mockClaim.getStartDate(), mockClaim.getEndDate(), 
				mockClaim.getDescription(), mockClaim.getDestinations(), mockClaim.getTags(),
				mockClaim.getStatus(), mockClaim.getCanEdit(), mockClaim.getExpenses(), 
				mockClaim.getUser());
		
		mockClaim = new Claim(newClaimId);
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);		
		
		// Search the server at the claim id we just made
		searchResult = searchServer(newClaimId);
		assertEquals(1, searchResult.getTotalHits());
		assertEquals(mockClaim.getDescription(), searchResult.getRetrievedDescription());
			
		// Delete the claim
		claimListController.deleteClaim(newClaimId);
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);	
		
		// Search again
		searchResult = searchServer(newClaimId);
		assertEquals(0, searchResult.getTotalHits());
			
	}
	
	// Tests that changes to multiple claims while offline get synced online when an internet connection
	// returns
	// US 09.01.01
	public void testSyncClaims() throws Throwable {

		// CITATION http://stackoverflow.com/questions/6516441/why-does-androidtestcase-getcontext-getapplicationcontext-return-null, 2015-04-06
		context = ClaimApplication.getContext();
		while (context == null){
			context = ClaimApplication.getContext();
		}		
		
		onlineMapper = new OnlineMapper(ClaimApplication.getContext());	
		claimMapper = new ClaimMapper(ClaimApplication.getContext());

		// Mock disable internet connection
		Constants.CONNECTIVITY_STATUS = false;
		
		
		/* Create mock claim 1 */
		
		// Generate a mock claim
		int newClaimId1 = cg.createMockClaim(true, true, true, true);
		Claim mockClaim1 = new Claim(newClaimId1);
		
		newClaimId1++; // ID of claim that will be created
		
		// Save the claim
		claimListController = new ClaimListController();
		claimListController.createClaim(mockClaim1.getStartDate(), mockClaim1.getEndDate(), 
				"Test 1", mockClaim1.getDestinations(), mockClaim1.getTags(),
				mockClaim1.getStatus(), mockClaim1.getCanEdit(), mockClaim1.getExpenses(), 
				mockClaim1.getUser());
		
		mockClaim1 = new Claim(newClaimId1);
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);		
		
		// Search the server at the claim id we just made
		searchResult = searchServer(newClaimId1);
		assertEquals(0, searchResult.getTotalHits());

		
		/* Create and update mock claim 2 */
		
		// Generate a mock claim
		int newClaimId2 = cg.createMockClaim(true, true, true, true);
		Claim mockClaim2 = new Claim(newClaimId2);
		
		newClaimId2++; // ID of claim that will be created
		
		// Save the claim
		claimListController = new ClaimListController();
		claimListController.createClaim(mockClaim2.getStartDate(), mockClaim2.getEndDate(), 
				"Test 2", mockClaim2.getDestinations(), mockClaim2.getTags(),
				mockClaim2.getStatus(), mockClaim2.getCanEdit(), mockClaim2.getExpenses(), 
				mockClaim2.getUser());
		
		mockClaim2 = new Claim(newClaimId2);
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);		
		
		// Search the server at the claim id we just made
		searchResult = searchServer(newClaimId2);
		assertEquals(0, searchResult.getTotalHits());
		
		
		// Mock enable internet connection
		Constants.CONNECTIVITY_STATUS = true;	
		onlineMapper.sync();
		
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);		
		
		// Search the server at the claim ids we just made
		searchResult = searchServer(newClaimId1);
		assertEquals(1, searchResult.getTotalHits());
		assertEquals(mockClaim1.getDescription(), searchResult.getRetrievedDescription());
		searchResult = searchServer(newClaimId2);
		assertEquals(1, searchResult.getTotalHits());
		assertEquals(mockClaim2.getDescription(), searchResult.getRetrievedDescription());	
	}
	
	
	// Tests that multiple changes to a claim while offline get synced online when an internet connection
	// returns
	// US 09.01.01
	public void testSyncMultipleChangesToSameClaim() throws Throwable {

		// CITATION http://stackoverflow.com/questions/6516441/why-does-androidtestcase-getcontext-getapplicationcontext-return-null, 2015-04-06
		context = ClaimApplication.getContext();
		while (context == null){
			context = ClaimApplication.getContext();
		}		
		
		onlineMapper = new OnlineMapper(ClaimApplication.getContext());	
		claimMapper = new ClaimMapper(ClaimApplication.getContext());

		// Mock disable internet connection
		Constants.CONNECTIVITY_STATUS = false;		
		
		
		// Generate a mock claim
		int newClaimId = cg.createMockClaim(true, true, true, true);
		Claim mockClaim = new Claim(newClaimId);
		
		newClaimId++; // ID of claim that will be created
		
		// Save the claim
		claimListController = new ClaimListController();
		claimListController.createClaim(mockClaim.getStartDate(), mockClaim.getEndDate(), 
				"Test 1", mockClaim.getDestinations(), mockClaim.getTags(),
				mockClaim.getStatus(), mockClaim.getCanEdit(), mockClaim.getExpenses(), 
				mockClaim.getUser());
		
		mockClaim = new Claim(newClaimId);
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);		
		
		// Search the server at the claim id we just made
		searchResult = searchServer(newClaimId);
		
		// There should be 0 claims
		assertEquals(0, searchResult.getTotalHits());
			
		// Change the claim
		ClaimController claimController = new ClaimController(mockClaim);
		claimController.updateClaim(mockClaim.getStartDate(), mockClaim.getEndDate(), 
				"Test 2", mockClaim.getDestinations(), mockClaim.getCanEdit());
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);	
		
		// Search the server at the claim id we just made again
		searchResult = searchServer(newClaimId);
		
		// There should still be 0 claims
		assertEquals(0, searchResult.getTotalHits());
		
		
		// Mock enable internet connection
		Constants.CONNECTIVITY_STATUS = true;	
		onlineMapper.sync();
		
		// Wait for the claim data to get pushed
		Thread.sleep(waitTime);		
		
		// Only the most recent change should be there
		searchResult = searchServer(newClaimId);
		assertEquals(1, searchResult.getTotalHits());
		assertEquals(mockClaim.getDescription(), searchResult.getRetrievedDescription());		
		
	}	
	
	
	public QueryObject searchServer(int newClaimId){
		HttpClient httpClient = new DefaultHttpClient();
		Type searchResponseType = new TypeToken<SearchResponse<Claim>>(){}.getType();
		int totalHits = -1;
		HttpPost searchRequest = new HttpPost(onlineMapper.getResourceURL() + claimMapper.getClaimFileName(newClaimId) + "/_search");
		searchRequest.setHeader("Accept", "application/json");
		String retrievedDescription = null;
		try {
			
			HttpResponse response = httpClient.execute(searchRequest);
			SearchResponse<Claim> result = gson.fromJson(
					new InputStreamReader(response.getEntity().getContent()), 
					searchResponseType);
			
			totalHits = result.getHits().getTotal();
			if (totalHits > 0){
				retrievedDescription = result.getHits().getHits().get(0).getSource().getDescription();
			} else {
				retrievedDescription = null;
			}
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		return new QueryObject(totalHits, retrievedDescription);
	}
	
	public class QueryObject {
		
		private int totalHits;
		private String retrievedDescription;
		
		public QueryObject(int totalHits, String retrievedDescription){
			this.totalHits = totalHits;
			this.retrievedDescription = retrievedDescription;
		}
		
		public int getTotalHits(){
			return totalHits;
		}
		
		public String getRetrievedDescription(){
			return retrievedDescription;
		}
	}
	
}