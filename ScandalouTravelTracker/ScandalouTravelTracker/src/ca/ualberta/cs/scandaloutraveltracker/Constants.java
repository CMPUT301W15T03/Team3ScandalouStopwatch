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

/**
 *  Class contains all the values that are constant throughout the
 *  application. The statuses section is especially useful since the
 *  status is set through various places in the application and is 
 *  handy not to have to declare the constant in each activity.
 * @author Team3ScandalouStopwatch
 *
 */
public class Constants {

	// Labels
	public final static String claimIdLabel = "ca.ualberta.cs.scandaloutraveltracker.claimId";
	//public static final String receiptPathLabel = "ca.ualberta.cs.scandaloutraveltracker.receiptPath";
	public static final String receiptPhotoLabel = "ca.ualberta.cs.scandaloutraveltracker.receiptPath";
	
	// Statuses (These should be put in strings.xml or something)
	public final static String statusInProgress = "In progress";
	public final static String statusSubmitted = "Submitted";
	public final static String statusReturned = "Returned";
	public final static String statusApproved = "Approved";
	
	// Misc.
	public final static String dateFormat = "M/d/yyyy";
	public static final long MAX_PHOTO_SIZE = 65536; // 64 KB	
	
	// Modes for the ClaimList
	public final static String APPROVER_MODE = "approver";
	public final static String TAG_MODE = "tag";
	
	// Connectivity
	public static boolean CONNECTIVITY_STATUS = false; // not the best place for this
}
