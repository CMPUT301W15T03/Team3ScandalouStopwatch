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
		Tag tag1 = new Tag("Tag1");
		Tag tag2 = new Tag("Tag2");
		claim.addTag(tag1);
		assertTrue("Tag1 not added to claim", (claim.getTags().size() == 1));
		claim.addTag(tag2);
		assertTrue("Tag2 not added to claim", (claim.getTags().size() == 2));
	}
	
	// Test UC 03.02.01
	public void testViewTags() {
		Tag tag1 = new Tag("Tag1");
		Tag tag2 = new Tag("Tag2");
		ArrayList<Tag> tags = Tag.getAllInstances();
		assertFalse("Insufficient tags in list", (tags.size() < 2));
		if (tags.size() == 2) {
			assertTrue("Tag1 not in list", (tags.get(0) == tag1));
			assertTrue("Tag2 not in list", (tags.get(1) == tag2));
		}
	}
	
	// Test UC 03.02.02
	public void testAddTag() {
		Tag tag1 = new Tag("Tag1");
		ArrayList<Tag> tags = Tag.getAllInstances();
		assertTrue("Tag not added to list", (tags.size() == 1));
		assertTrue("Wrong tag", (tags.get(0) == tag1));
	}
	
	// Test UC 03.02.03
	public void testRenameTag() {
		Tag tag1 = new Tag("Tag1");
		String rename = "Renamed tag";
		tag1.setName(rename);
		assertTrue("Rename failed", (tag1.getName().equals(rename)));
	}
	
	// Test UC 03.02.04
	public void testDeleteTag() {
		Tag tag1 = new Tag("Tag1");
		tag1.delete();
		assertTrue("Delete failed", (Tag.getAllInstances().size() == 0));
	}
	
	// Test UC 03.03.01
	public void testFilterClaims() {
		Claim claim1 = new Claim();
		Claim claim2 = new Claim();
		Tag tag1 = new Tag("Tag1");
		Tag tag2 = new Tag("Tag2");
		claim1.addTag(tag1);
		claim2.addTag(tag2);
		ClaimList.addClaim(claim1);
		ClaimList.addClaim(claim2);
		ClaimList.filterByTag(tag1);
		ArrayList<Claim> claims = ClaimList.getAllClaims();
		assertTrue("Filter failed", (claims.size() == 1));
		assertTrue("Wrong claim", (claims.get(0) == claim1));
	}

}