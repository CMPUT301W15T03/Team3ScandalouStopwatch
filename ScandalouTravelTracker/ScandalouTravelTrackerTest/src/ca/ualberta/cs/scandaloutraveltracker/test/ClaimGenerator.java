/*

Copyright 2015 ScandalouStopwatch

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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClaimGenerator {

	// Claim name
	private static final String claimName = "Claim";
	
	// Claimant name
	private static final String claimantName = "Claimant";
	
	// Start date
	private static final int year = 1950;
	private static final int month = 0;
	private static final int startDay = 1;
	private static final int endDay = 2;
	
	// Claim destinations
	private static final String destination = "Destination";
	
	// Status
	private String status;
	
	// Amounts
	private static final String currency1 = "USD";
	private static final String currency2 = "CAD";
	private static final Float amount1 = 1f;
	private static final Float amount2 = 49.99f;
	
	// Approver name
	private static final String approverName = "";
	
	// Approver comment
	private static final String approverComment = "";
	
	public Claim generateClaim(int i, String status){

		// Claim name
		String claimName = ClaimGenerator.claimName + " " + i;
		
		// Claimant name
		String claimantName = ClaimGenerator.claimantName + " " + i;
		
		// Dates
		// 	Date's deprecated; maybe fix it later;
		Date startDate = new Date(year+i, month, startDay);
		Date endDate = new Date(year+i, month, endDay);
		
		// Destinations
		String destination1 = destination + " " + i + ".1"; 
		String destination2 = destination + " " + i + ".2";
		ArrayList<String> destinations = new ArrayList<String>();
		destinations.add(destination1);
		destinations.add(destination2);
		
		// Status
		this.status = status;
		
		// Amounts
		String currency1 = ClaimGenerator.currency1;
		String currency2 = ClaimGenerator.currency2;
		Float amount1 = ClaimGenerator.amount1 * i;
		Float amount2 = ClaimGenerator.amount2 * i;
		Map<String, Float> totals = new HashMap<String, Float>();
		totals.put(currency1, amount1);
		totals.put(currency2, amount2);
		
		// Approver name
		String approverName = ClaimGenerator.approverName;
		
		// Approver comment
		String approverComment = ClaimGenerator.approverComment;

		// Create claim
		Claim claim = new Claim(claimName);
		
		// Populate claim
		claim.setClaimantName(claimantName);
		claim.setStartDate(startDate);
		claim.setEndDate(endDate);
		claim.setDestinations(destinations);
		claim.setStatus(status);
		claim.setTotals(totals);
		claim.setApproverName(approverName);
		claim.setApproverComment(approverComment);
		
		return claim;	
	}

	public String getClaimName(int i) {
		return claimName + " " + i;
	}

	public String getClaimantName(int i) {
		return claimantName + " " + i;
	}

	public Date getStartDate(int i) {
		Date startDate = new Date(year+i, month, startDay);
		return startDate;
	}

	public Date getEndDate(int i) {
		Date endDate = new Date(year+i, month, endDay);
		return endDate;
	}

	public String getDestination1(int i) {
		return destination + i + ".1";
	}

	public String getDestination2(int i) {
		return destination + i + ".1";
	}	
	
	public String getStatus() {
		return status;
	}

	public String getCurrency1() {
		return currency1;
	}

	public String getCurrency2() {
		return currency2;
	}

	public Float getAmount1(int i) {
		return amount1 * i;
	}

	public Float getAmount2(int i) {
		return amount2 * i;
	}

	public String getApproverName() {
		return approverName;
	}

	public String getApproverComment() {
		return approverComment;
	}	
}