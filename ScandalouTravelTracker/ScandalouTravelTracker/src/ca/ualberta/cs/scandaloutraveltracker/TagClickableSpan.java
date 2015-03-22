package ca.ualberta.cs.scandaloutraveltracker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TagClickableSpan extends ClickableSpan {
	private Context context;
	
	public TagClickableSpan(Context context) {
		this.context = context;
	}

	@Override
	public void onClick(View textView) {
		TextView tv = (TextView) textView;
		Spanned s = (Spanned) tv.getText();
		int start = s.getSpanStart(this);
		int end = s.getSpanEnd(this);
		Log.d("TAG", s.subSequence(start, end).toString());
		
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Options for " + s.subSequence(start, end).toString())
		.setCancelable(true)
		.setItems(R.array.tag_menu, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		});
		
		AlertDialog alert = builder.create();
		alert.show();
	}

}