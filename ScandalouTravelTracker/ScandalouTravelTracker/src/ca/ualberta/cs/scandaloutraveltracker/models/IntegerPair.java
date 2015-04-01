package ca.ualberta.cs.scandaloutraveltracker.models;
/**
 * Basic class that just stores two integers in an object.
 * @author jwu
 *
 */
public class IntegerPair {
	private int x;
	private int y;
	
	public IntegerPair(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	
}