package budget.info;

import java.util.Date;

public class Entry implements Comparable<Entry>{
	
	private Date date;
	private String reason;
	private double amount;
	
	public Entry(String reason, double amount){
		date = new Date();
		this.reason = reason;
		this.amount = amount;
	}
	
	public Entry(String reason, double amount, long time){
		this.reason = reason;
		this.amount = amount;
		date = new Date(time);
	}
	
	public String forSave(){
		return date.getTime()+":"+reason+":"+amount;
	}
	
	public String toString(){
		return "Date: ["+date.toString()+"], Reason: "+reason+", "+(amount < 0 ? "Removed" : "Added")+": $"+String.format("%.2f", Math.abs(amount));
	}

	public long getTime(){
		return date.getTime();
	}
	
	@Override
	public int compareTo(Entry e) {
		return (int) (date.getTime() - e.getTime());
	}
	
}
