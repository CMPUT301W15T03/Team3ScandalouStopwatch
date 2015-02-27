package ca.ualberta.cs.scandaloutraveltracker.test;

import junit.framework.TestCase;

public class ClaimListTest extends TestCase {
	//Tests for US02.01.01
	//create an empty claim list
	public void testEmptyClaimList(){
		ClaimList claimlist=new ClaimList();
		assertTrue("Empty claim list", claimlist.size()==0);
		}
	//create a claim list with a claim and get claims
	public void testGetClaims(){
		ClaimList claimlist = new ClaimList();
		String claimName="A claim";
		Claim testClaim= new Claim(claimName);
		claimlist.addClaim(testClaim);
		Collection<Claim> claims = claimList.getClaims();
		assertTrue("Claim List Size", claims.size()==1);
		assertTrue("Test Claim not contained", claims.contains(testClaim));
	
	}
	//Test for US02.02.01
	//sort claims
	public void testSortClaim(){
		ClaimList claimlist = new ClaimList();
		String claimName="A claim";
		String claimName2="B claim";
		Claim testClaim1=new Claim(claimName);
		Claim testClaim2=new Claim(claimName2);
		claimlist.addClaim(testClaim1);
		claimlist.addClaim(testClaim2);
		
		
		Collection<Claim> claims= claimList.getClaims().sort();
		assertTrue("Student list is not sorted",)
	}
	//test for US02.01.01 and US02.02.01
	//updates the claim list in the listview using listeners
	boolean updated=false;
	public void testNotifyListeners(){
		ClaimList claimlist = new ClaimList();
		updated= false;
		Listener 1 = new Listener(){
			public void update(){
					ClaimListTest.this.updated=true;
			}
		};
		ClaimList.addListener(1);
		Claim testClaim=new Claim("New");
		ClaimList.addClaim(testClaim);
		assertTrue("ClaimList didnt fire an update", this.updated);
		updated=false;
		claimlist.removeClaim(testClaim);
		assertTrue("Removing claim from claimlist didnt fire an update off", this.updated);
	}

}
