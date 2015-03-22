package ca.ualberta.cs.scandaloutraveltracker;
import java.util.ArrayList;

/**
 * TagParser returns a list of starting and ending indices of
 * tags prefixed with a # and post-fixed with a comma.  
 * @author jwu
 *
 */


public class TagParser {
	/**
	 * Takes in a string of tags. The list should have the appearance:
	 * #tag1, #tag2, ..., #tagN. 
	 * @param tags
	 * @return List of starting and ending indices for the tags (start, end)
	 */
	public ArrayList<IntegerPair> parse(String tags) {
		ArrayList<IntegerPair> tagIndices = new ArrayList<IntegerPair>();
		int stringLength = tags.length();
		int startIndex = -1;
		int endIndex = -1;
		char currentCharacter;
		
		for (int cursor = 1; cursor < stringLength; cursor++) {
			currentCharacter = tags.charAt(cursor);
			
			// Setting start and end index if found
			if (currentCharacter == '#') {
				startIndex = cursor;
			}
			else if (currentCharacter == ' ') {
				endIndex = cursor;
			}
			
			if (cursor == (stringLength - 1)) {
				endIndex = stringLength;
			}
			
			// Setting new integer pair and resetting start and end index
			if (startIndex != -1 && endIndex != -1) {
				IntegerPair newPair = new IntegerPair(startIndex, endIndex);
				startIndex = -1;
				endIndex = -1;
				
				tagIndices.add(newPair);
			}
		}
		
		return tagIndices;
	}
}
