/*

Copyright 2015 Team3ScandalouStopwatch

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

*/

package ca.ualberta.cs.scandaloutraveltracker;
import java.util.ArrayList;

import ca.ualberta.cs.scandaloutraveltracker.models.IntegerPair;

/**
 * TagParser returns a list of starting and ending indices of
 * tags prefixed with a # and post-fixed with a space. The TagParser
 * class helps the ClickableSpan determine the starting and ending
 * points of tags so they can be clickable.
 * @author Team3ScandalouStopwatch
 *
 */

public class TagParser {
	/**
	 * Takes in a string of tags. The list should have the appearance:
	 * #tag1 #tag2 ... #tagN
	 * @param tags
	 * @return List of starting and ending indices for the tags (start, end)
	 */
	public ArrayList<IntegerPair> parse(String tags) {
		ArrayList<IntegerPair> tagIndices = new ArrayList<IntegerPair>();
		int stringLength = tags.length();
		int startIndex = -1;
		int endIndex = -1;
		char currentCharacter;
		
		for (int cursor = 0; cursor < stringLength; cursor++) {
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
