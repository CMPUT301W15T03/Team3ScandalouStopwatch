package ca.ualberta.cs.scandaloutraveltracker;

import java.util.Collections;

public class ClaimListController {
	// TODO: Have ClaimListController load the list of claims using Claim Manager
	
	private ClaimList claims;
	
	// Constructor for testing out Claim List functions quickly
	public ClaimListController(ClaimList claims) {
		this.claims = claims;
	}
	
	public void addView(ViewInterface view) {
		claims.addView(view);
	}
	
	public void removeView(ViewInterface view) {
		claims.removeView(view);
	}
	
	public void addClaim(Claim claim) {
		claims.addClaim(claim);
		Collections.sort(claims.getClaims());
	}
	
	public void removeClaim(Claim claim) {
		claims.deleteClaim(claim);
	}
	
	public void notifyViews() {
		claims.notifyViews();
	}
	
	public Claim getClaim() {
		return new Claim();
	}
	
	public ClaimList getClaimList() {
		return claims;
	}
}
