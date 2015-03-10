package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;

public class SModel {
	protected ArrayList<ViewInterface> views;
	
	public SModel() {
		views = new ArrayList<ViewInterface>();
	}
	
	public void addView(ViewInterface view) {
		views.add(view);
		
	}
	public void removeView(ViewInterface view) {
		views.remove(view);
		
	}
	public void notifyViews() {
		for (ViewInterface view : views) {
			view.update();
		}
	}
}
