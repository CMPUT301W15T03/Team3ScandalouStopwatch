package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;


import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class ElasticSearch {

	/*private ClaimListAdapter claimListAdapter;
	private ExpenseListActivity expenseListAdapter;
	private ClaimController claimController;
	private ClaimListController claimListController;*/
	private static Context applicationContext;
	
	
	public ElasticSearch() {
		
	}
	public static void initializeContext(Context context) {
	applicationContext = context.getApplicationContext();
	}
	
	public static ArrayList<Claim> getSubmittedClaims(){
		return null;
		/*
		 * add try/ catch exceptions
		 */
	}
	
	public static Claim getClaim(String s){
		Gson gson = new Gson();
		return null;
		/*
		 * add try/ catch exceptions
		 */
	}

	public static void addClaim(){
		Gson gson = new Gson();
		/*
		 * add try/ catch exceptions
		 */
	}
	
		
	public static void deleteClaim(){
		/*
		 * add try/ catch exceptions
		 */
	}
	 
	public static void updateClaim(){
		/*
		 * add try/ catch exceptions
		 */
	}
	 
	public static void saveUser(){
		/*
		 * add try/ catch exceptions
		 */
	}
	public static void loadUser() {
		/*
		 * add try/ catch exceptions
		 */
	}
	
	public static void loadClaims() {
		/*
		 * add try/ catch exceptions
		 */
	}
	
	
}
