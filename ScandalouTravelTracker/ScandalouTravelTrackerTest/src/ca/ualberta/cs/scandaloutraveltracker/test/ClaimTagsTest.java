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

import ca.ualberta.cs.scandaloutraveltracker.Claim;
import ca.ualberta.cs.scandaloutraveltracker.ClaimList;

import junit.framework.TestCase;

public class ClaimTagsTest extends TestCase {
	
	// Test UC 03.01.01
	public void testTagClaim() {
		Claim claim = new Claim();
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("Tag1");
		claim.setTags(tags);
		assertTrue("Tag1 not added to claim", (claim.getTags().size() == 1));
		tags.add("Tag2");
		claim.setTags(tags);
		assertTrue("Tag2 not added to claim", (claim.getTags().size() == 2));
	}
	
	//TODO: this test is incomplete; finish once desired functionality is decided
	// Test UC 03.02.01
	public void testViewTags() {
		assertTrue("Complete testViewTags()!", false);
		/*Claim claim = new Claim();
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("Tag1");
		tags.add("Tag2");
		claim.setTags(tags);
		ArrayList<String> usedTags = GET_ALL_USED_TAGS();
		assertFalse("Insufficient tags in list", (usedTags.size() < 2));
		if (usedTags.size() == 2) {
			assertTrue("Tag1 not in list", (usedTags.get(0) == tags.get(0)));
			assertTrue("Tag2 not in list", (usedTags.get(1) == tags.get(1)));
		}*/
	}
	
	// Test UC 03.02.02
	public void testAddTag() {
		Claim claim = new Claim();
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("Tag1");
		claim.setTags(tags);
		ArrayList<String> usedTags = claim.getTags();
		assertTrue("Tag not added to list", (usedTags.size() == 1));
		assertTrue("Wrong tag", (usedTags.get(0) == "Tag1"));
	}
	
	// Test UC 03.02.03
	public void testRenameTag() {
		Claim claim = new Claim();
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("Tag1");
		claim.setTags(tags);
		String rename = "Renamed tag";
		tags.set(0, rename);
		claim.setTags(tags);
		ArrayList<String> usedTags = claim.getTags();
		assertTrue("Rename failed", (usedTags.get(0).equals(rename)));
	}
	
	// Test UC 03.02.04
	public void testDeleteTag() {
		Claim claim = new Claim();
		ArrayList<String> tags = new ArrayList<String>();
		tags.add("Tag1");
		claim.setTags(tags);
		tags.remove(0);
		claim.setTags(tags);
		ArrayList<String> usedTags = claim.getTags();
		assertTrue("Delete failed", (usedTags.size() == 0));
	}
	
	// Test UC 03.03.01
	public void testFilterClaims() {
		Claim claim1 = new Claim();
		Claim claim2 = new Claim();
		ArrayList<String> tags1 = new ArrayList<String>();
		ArrayList<String> tags2 = new ArrayList<String>();
		tags1.add("Tag1");
		tags2.add("Tag2");
		claim1.setTags(tags1);
		claim2.setTags(tags2);
		ClaimList claimList = new ClaimList();
		claimList.addClaim(claim1);
		claimList.addClaim(claim2);
		ArrayList<Claim> filteredList = claimList.searchTag("Tag1");
		assertTrue("Filter failed", (filteredList.size() == 1));
		assertTrue("Wrong claim", (filteredList.get(0) == claim1));
	}

}