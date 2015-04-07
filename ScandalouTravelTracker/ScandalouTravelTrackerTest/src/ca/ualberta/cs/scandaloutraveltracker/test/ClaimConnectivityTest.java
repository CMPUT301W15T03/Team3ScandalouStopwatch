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
import java.lang.reflect.Type;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import ca.ualberta.cs.scandaloutraveltracker.ClaimApplication;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimController;
import ca.ualberta.cs.scandaloutraveltracker.controllers.ClaimListController;
import ca.ualberta.cs.scandaloutraveltracker.mappers.ClaimMapper;
import ca.ualberta.cs.scandaloutraveltracker.mappers.OnlineMapper;
import ca.ualberta.cs.scandaloutraveltracker.models.Claim;

import junit.framework.TestCase;

public class ClaimConnectivityTest extends TestCase {
	
	private Context context;
	
	private Gson gson = new Gson();
	private OnlineMapper onlineMapper;
	private ClaimMapper claimMapper;
	
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
		ClaimGenerator cg = new ClaimGenerator();
		int newClaimId = cg.createMockClaim(true, true, true, true);
		Claim mockClaim = new Claim(newClaimId);
		
		newClaimId++; // ID of claim that will be created
		
		// Search the server at the claim id we just made
		int totalHits = -1;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost searchRequest = new HttpPost(onlineMapper.getResourceURL() + claimMapper.getClaimFileName(newClaimId) + "/_search");
		searchRequest.setHeader("Accept", "application/json");
		Type searchResponseType = new TypeToken<SearchResponse<Claim>>(){}.getType();
		try {
			
			HttpResponse response = httpClient.execute(searchRequest);
			SearchResponse<Claim> result = gson.fromJson(
					new InputStreamReader(response.getEntity().getContent()), 
					searchResponseType);
			
			totalHits = result.getHits().getTotal();		
			
		} catch (Exception e){
			e.printStackTrace();
		}
			
		// There should be 0 claims
		assertEquals(0, totalHits);
			
		// Save the claim
		ClaimListController claimListController = new ClaimListController();
		claimListController.createClaim(mockClaim.getStartDate(), mockClaim.getEndDate(), 
				mockClaim.getDescription(), mockClaim.getDestinations(), mockClaim.getTags(),
				mockClaim.getStatus(), mockClaim.getCanEdit(), mockClaim.getExpenses(), 
				mockClaim.getUser());
		
		// Wait for the claim data to get pushed
		Thread.sleep(3000);
		
		// Search the server at the claim id we just made again
		String retrievedDescription = null;
		try {
			
			HttpResponse response = httpClient.execute(searchRequest);
			SearchResponse<Claim> result = gson.fromJson(
					new InputStreamReader(response.getEntity().getContent()), 
					searchResponseType);
			
			totalHits = result.getHits().getTotal();
			retrievedDescription = result.getHits().getHits().get(0).getSource().getDescription();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		// There should be 1 claim
		assertEquals(1, totalHits);
		assertEquals(mockClaim.getDescription(), retrievedDescription);
			
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
		ClaimGenerator cg = new ClaimGenerator();
		int newClaimId = cg.createMockClaim(true, true, true, true);
		Claim mockClaim = new Claim(newClaimId);
		
		newClaimId++; // ID of claim that will be created
		
		// Customize the claim
		mockClaim.setDescription("Test 1");
		
		// Save the claim
		ClaimListController claimListController = new ClaimListController();
		claimListController.createClaim(mockClaim.getStartDate(), mockClaim.getEndDate(), 
				mockClaim.getDescription(), mockClaim.getDestinations(), mockClaim.getTags(),
				mockClaim.getStatus(), mockClaim.getCanEdit(), mockClaim.getExpenses(), 
				mockClaim.getUser());
		
		mockClaim = new Claim(newClaimId);
		
		// Wait for the claim data to get pushed
		Thread.sleep(3000);		
		
		// Search the server at the claim id we just made
		int totalHits = -1;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost searchRequest = new HttpPost(onlineMapper.getResourceURL() + claimMapper.getClaimFileName(newClaimId) + "/_search");
		searchRequest.setHeader("Accept", "application/json");
		Type searchResponseType = new TypeToken<SearchResponse<Claim>>(){}.getType();
		String retrievedDescription = null;
		try {
			
			HttpResponse response = httpClient.execute(searchRequest);
			SearchResponse<Claim> result = gson.fromJson(
					new InputStreamReader(response.getEntity().getContent()), 
					searchResponseType);
			
			totalHits = result.getHits().getTotal();
			retrievedDescription = result.getHits().getHits().get(0).getSource().getDescription();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		// There should be 1 claim
		assertEquals(1, totalHits);
		assertEquals(mockClaim.getDescription(), retrievedDescription);
			
		// Change the claim
		ClaimController claimController = new ClaimController(mockClaim);
		claimController.updateClaim(mockClaim.getStartDate(), mockClaim.getEndDate(), 
				"Test 2", mockClaim.getDestinations(), mockClaim.getCanEdit());
		
		// Wait for the claim data to get pushed
		Thread.sleep(3000);	
		
		Log.d("Mytest", Integer.toString(newClaimId));
		
		// Search the server at the claim id we just made again
		try {
			
			HttpResponse response = httpClient.execute(searchRequest);
			SearchResponse<Claim> result = gson.fromJson(
					new InputStreamReader(response.getEntity().getContent()), 
					searchResponseType);
			
			totalHits = result.getHits().getTotal();
			retrievedDescription = result.getHits().getHits().get(0).getSource().getDescription();
			
		} catch (Exception e){
			e.printStackTrace();
		}
		
		// There should be 1 claim with the updated description
		assertEquals(1, totalHits);
		assertEquals(mockClaim.getDescription(), retrievedDescription);
			
	}	
	
}