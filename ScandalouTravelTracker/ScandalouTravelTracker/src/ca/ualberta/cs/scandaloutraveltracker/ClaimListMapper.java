package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * ClaimListMapper loads all the claims in the list using ClaimMapper.
 * @author Team3ScandalouStopwatch
 *
 */
public class ClaimListMapper {

	private Context context;
	
	/**
	 * Constructor needs the current context to make a ClaimListMapper.
	 * @param context
	 */
	public ClaimListMapper(Context context){
		this.context = context;
	}	
	
	/**
	 * 
	 * @return List of all the saved claims
	 */
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
