package ca.ualberta.cs.scandaloutraveltracker;

public class SyncQueueItem {
	
	private String key;
	private Object object;
	private int mode;
	
	public SyncQueueItem(String key, Object object, int mode){
		this.key = key;
		this.object = object;
		this.mode = mode;
	}
	
	public SyncQueueItem(String key, int mode){
		this.key = key;
		this.object = null;
		this.mode = mode;
	}		
	
	public String getKey() {
		return key;
	}

	public Object getObject() {
		return object;
	}
	
	public int getMode() {
		return mode;
	}		
}
