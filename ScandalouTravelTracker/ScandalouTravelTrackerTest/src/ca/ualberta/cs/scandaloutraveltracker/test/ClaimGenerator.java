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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.Destination;

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
	private static final Double amount1 = 1.0;
	private static final Double amount2 = 49.99;
	
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
		Destination dest1 = new Destination(destination + " " + i + ".1", "1"); 
		Destination dest2 = new Destination(destination + " " + i + ".2", "2");
		ArrayList<Destination> destinations = new ArrayList<Destination>();
		destinations.add(dest1);
		destinations.add(dest2);
		
		// Status
		this.status = status;
		
		// Amounts
		String currency1 = ClaimGenerator.currency1;
		String currency2 = ClaimGenerator.currency2;
		Double amount1 = ClaimGenerator.amount1 * i;
		Double amount2 = ClaimGenerator.amount2 * i;
		HashMap<String, Double> totals = new HashMap<String, Double>();
		totals.put(currency1, amount1);
		totals.put(currency2, amount2);
		
		// Approver name
		String approverName = ClaimGenerator.approverName;
		
		// Approver comment
		String approverComment = ClaimGenerator.approverComment;

		// Create claim
		Claim claim = new Claim(claimName, endDate, endDate);
		
		// Populate claim
		claim.setName(claimantName);
		claim.setStartDate(startDate);
		claim.setEndDate(endDate);
		claim.setDestinations(destinations);
		claim.setStatus(status);
		claim.setApproverName(approverName);
		//claim.setApproverComment(approverComment);
		
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

	public Double getAmount1(int i) {
		return amount1 * i;
	}

	public Double getAmount2(int i) {
		return amount2 * i;
	}

	public String getApproverName() {
		return approverName;
	}

	public String getApproverComment() {
		return approverComment;
	}	
}
