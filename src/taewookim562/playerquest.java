package taewookim562;

import java.util.ArrayList;

import org.bukkit.entity.Player;

public class playerquest {
	
	private ArrayList<String> questing = new ArrayList<String>();
	private ArrayList<String> clearquest = new ArrayList<String>();
	
	public playerquest() {
		
	}
	
	public ArrayList<String> getquesting() {
		return this.questing;
	}
	
	public void setquesting(ArrayList<String> questing) {
		this.questing = questing;
	}
	
	public void setclearquest(ArrayList<String> clearquest) {
		this.clearquest = clearquest;
	}
	
	public ArrayList<String> getclearquest() {
		return this.clearquest;
	}
	
	public void addquesting(String quest) {
		this.questing.add(quest);
	}
	
	public void removequesting(String quest) {
		this.questing.remove(quest);
	}
	
	public void clearquest(String quest) {
		if(questing.contains(quest)) {
			questing.remove(quest);
			clearquest.add(quest);
		}
	}
	
	public void removeclearquest(String quest) {
		clearquest.remove(quest);
	}

}
