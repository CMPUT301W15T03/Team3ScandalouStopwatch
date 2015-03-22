package ca.ualberta.cs.scandaloutraveltracker;

import java.util.ArrayList;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class TagClickableSpan extends ClickableSpan {

	@Override
	public void onClick(View textView) {
		TextView tv = (TextView) textView;
		Spanned s = (Spanned) tv.getText();
		int start = s.getSpanStart(this);
		int end = s.getSpanEnd(this);
		Log.d("TAG", s.subSequence(start, end).toString());
	}

}

/*
// Set clickable elements in tags list
// http://stackoverflow.com/questions/10696986/how-to-set-the-part-of-the-text-view-is-clickable 03/19/2015
spannableString = new SpannableString(tagsString);

TagParser parser = new TagParser();
ArrayList<IntegerPair> indices = parser.parse(tagsString);
Log.d("TAG", tagsString);
Log.d("TAG", "Indices size: " + indices.size());

for (int i = 0; i < indices.size(); i++) {
	IntegerPair currentIndex = indices.get(i);
	Log.d("TAG", ""+currentIndex.getX()+", "+currentIndex.getY());
	spannableString.setSpan(new TagClickableSpan(), currentIndex.getX(), 
							currentIndex.getY(), 0);
}

tagsDisplay.setText(spannableString);
tagsDisplay.setMovementMethod(LinkMovementMethod.getInstance());
*/