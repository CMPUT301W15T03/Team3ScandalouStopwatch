package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

public class ClaimListMapper {

	private Context context;
	
	public ClaimListMapper(Context context){
		this.context = context;
	}	
	
	public ArrayList<Claim> loadClaims(){
		ArrayList<Claim> claims = new ArrayList<Claim>();
		
		SharedPreferences claimCounterFile = this.context.getSharedPreferences("claimCounter", 0);
		int mostRecentClaimId = claimCounterFile.getInt("claimCount", 0);	
		
		Claim claim;
		for (int i = 1; i <= mostRecentClaimId; i++){
			claim = new Claim(i);
			if (claim.getId() != -1){
				claims.add(claim);
			}
		}
		
		return claims;
	}	
	
}
