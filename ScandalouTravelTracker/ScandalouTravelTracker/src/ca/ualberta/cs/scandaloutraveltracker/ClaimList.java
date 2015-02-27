package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;
import java.util.Collection;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ClaimList {
	protected static ArrayList<Claim> claimList;
    protected ArrayList<Listener> listeners;
    	
	public ClaimList(){
		claimList= new ArrayList<Claim>();
		listeners= new ArrayList<Listener>();
	}
	public Collection<Claim> getClaims(){
		return claimList;
	}
	public ArrayList<Claim> searchTag(String tag){
		
		return null;
	}
	public void addClaim(Claim string){
		/*claimList.add(string);
		if (claimList.size()>1){
			Collections.sort(claimList, new CustomComparator())
		}
		ClaimListgetClaims
		*/
	}
	
	public void deleteClaim(Claim removeclaim){
		claimList.remove(removeclaim);
		notifyListeners();
	}
	public static boolean isEmpty(){
		return claimList.size()==0;
	}
	public void notifyListeners(){
		for(Listener listener: listeners){
			listener.update();
		}
	}
	public void addListener(Listener l){
		listeners.add(l);
	}
	public void removeListener(Listener l){
		listeners.remove(l);
	}
}
