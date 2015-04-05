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

@SuppressWarnings("serial")
/**
 * The UserInputException class helps throughout the application for
 * dealing with UserInputExceptions. Especially used to help throw
 * these types of exceptions.
 * @author Team3ScandalouStopwatch
 *
 */
public class UserInputException extends Exception {
	/**
	 * Constructor
	 * @param message Text that you wish to throw to the user on exception
	 */
	public UserInputException(String message) {
        super(message);
    }

	/**
	 * Constructor
	 * @param message Text that you wish to throw to the user on exception
	 * @param throwable The throwable object that you wish to also throw on exception
	 */
    public UserInputException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
