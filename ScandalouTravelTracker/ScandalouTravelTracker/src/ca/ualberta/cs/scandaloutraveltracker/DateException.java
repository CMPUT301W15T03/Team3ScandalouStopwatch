package ca.ualberta.cs.scandaloutraveltracker;

public class DateException extends Exception {
	public DateException(String message) {
        super(message);
    }

    public DateException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
