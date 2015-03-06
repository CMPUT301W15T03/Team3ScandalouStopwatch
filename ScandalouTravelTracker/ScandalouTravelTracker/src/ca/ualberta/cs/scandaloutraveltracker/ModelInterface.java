package ca.ualberta.cs.scandaloutraveltracker;

import android.view.View;

public interface ModelInterface {
	void addView(View view);
	void removeView(View view);
	void notifyViews();
}
