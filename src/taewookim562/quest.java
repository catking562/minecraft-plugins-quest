package taewookim562;

import java.util.ArrayList;

import org.bukkit.inventory.ItemStack;

public class quest {
	
	private String name;
	private ArrayList<ItemStack> ifitem = new ArrayList<ItemStack>();
	private ArrayList<ItemStack> presentitem = new ArrayList<ItemStack>();
	private ArrayList<String> talk = new ArrayList<String>();
	private String lore;
	private String title;
	
	public quest(String name) {
		this.name = name;
		this.lore = name;
		this.title = name;
	}
	
	public String getname() {
		return this.name;
	}
	
	public void setifitem(ArrayList<ItemStack> ifitem) {
		this.ifitem = ifitem;
	}
	
	public ArrayList<ItemStack> getifitem() {
		return this.ifitem;
	}
	
	public void setpresentitem(ArrayList<ItemStack> presenditem) {
		this.presentitem = presenditem;
	}
	
	public ArrayList<ItemStack> getpresenditem() {
		return this.presentitem;
	}
	
	public void settalk(ArrayList<String> talk) {
		this.talk = talk;
	}
	
	public ArrayList<String> gettalk() {
		return this.talk;
	}
	
	public String getlore() {
		return this.lore;
	}
	
	public void setlore(String lore) {
		this.lore = lore;
	}
	
	public String getTitle() {
		return this.title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}

}
