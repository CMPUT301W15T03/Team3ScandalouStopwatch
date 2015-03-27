package ca.ualberta.cs.scandaloutraveltracker;

public class UserInputException extends Exception {
	public UserInputException(String message) {
        super(message);
    }

    public UserInputException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
