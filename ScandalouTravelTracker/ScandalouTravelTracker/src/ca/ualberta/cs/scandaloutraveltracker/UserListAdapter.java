package ca.ualberta.cs.scandaloutraveltracker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserListAdapter extends BaseAdapter {
	protected UserList users;
	protected Context context;
	
	public UserListAdapter(Context context, UserList users) {
		this.users = users;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return users.getCount();
	}

	@Override
	public Object getItem(int position) {
		return users.getUser(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {
			LayoutInflater inflater = LayoutInflater.from(context);
			convertView = inflater.inflate(R.layout.list_user_display, parent, false);
		}
		
		// Fetch the textview and current user
		TextView userNameTV = (TextView) convertView.findViewById(R.id.userListNameTV);
		User currentUser = users.getUser(position);
		
		// Set the textview to user's name
		userNameTV.setText(currentUser.getName());
		
		return convertView;
	}

}
