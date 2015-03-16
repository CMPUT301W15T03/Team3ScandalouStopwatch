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
import java.util.Collections;
import java.util.Date;

import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimList;
import ca.ualberta.cs.scandaloutraveltracker.ViewInterface;
import junit.framework.TestCase;

public class ClaimListTest extends TestCase {

	// Test UC 02.01.01
	// create an empty claim list
	public void testEmptyClaimList(){
		ClaimList claimList = new ClaimList(true);
		assertTrue("Empty claim list", claimList.getCount()==0);
		}

	// Test UC 02.01.01		
	// create a claim list with a claim and get claims
	public void testGetClaims(){
		ClaimList claimList = new ClaimList(true);
		String claimName="A claim";
		Claim testClaim= new Claim(claimName,null , null);
		claimList.addClaim(testClaim);
		assertTrue("Claim List Size", claimList.getCount()==1);
		assertTrue("Test Claim not contained", 
				claimList.getClaims().contains(testClaim));
	
	}

	// Test UC 02.02.01
	//sort claims
	public void testSortClaim(){
		ClaimList claimList = new ClaimList(true);
		String claimName="A claim";
		String claimName2="B claim";
		Claim testClaim1=new Claim(claimName, new Date(), new Date());
		@SuppressWarnings("deprecation")
		Claim testClaim2=new Claim(claimName2, new Date(2014, 01, 01), 
				new Date(2014, 01, 01));
		claimList.addClaim(testClaim1);
		claimList.addClaim(testClaim2);
		Collections.sort(claimList.getClaims());

		assertEquals("test claim 1 should be after test claim 2",
				testClaim1.compareTo(testClaim2),1);
		assertEquals("test claim 1 should be in position 1 of claimlist",
				claimList.getClaim(1).compareTo(testClaim1),0);
	}

	// Test UC 02.01.01, UC 02.02.01
	//updates the claim list in the listview using listeners
	boolean updated=false;
	public void testNotifyListeners(){
		ClaimList claimList = new ClaimList(true);
		updated= false;
		ViewInterface l = new ViewInterface(){
			public void update(){
					ClaimListTest.this.updated=true;
			}
		};
		claimList.addView(l);
		Claim testClaim=new Claim("New", null, null);
		claimList.addClaim(testClaim);
		assertTrue("ClaimList didnt fire an update", this.updated);
		updated=false;
		claimList.removeClaim(0);
		assertTrue("Removing claim from claimlist didnt fire an update off", this.updated);
	}

}
