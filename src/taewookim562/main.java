package taewookim562;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin implements Listener {
	
	Map<String, File> file = new HashMap<String, File>();
	Map<String, YamlConfiguration> config = new HashMap<String, YamlConfiguration>();
	Map<String, String> message = new HashMap<String, String>();
	List<quest> questlist = new ArrayList<quest>();
	Map<OfflinePlayer, playerquest> playerquestlist = new HashMap<OfflinePlayer, playerquest>();
	Map<Player, quest> optionquest = new HashMap<Player, quest>();
	
	public void messageconfig(YamlConfiguration messageconfig) {
		for(String key : messageconfig.getKeys(false)) {
			message.put(key, messageconfig.getString(key));
		}
		if(message.get("접두어") == null) {
			message.put("접두어", "&f[ &e퀘스트 &f] ");
		}
		if(message.get("명령어오류") == null) {
			message.put("명령어오류", "무슨 명령어죠?");
		}
		if(message.get("진행중퀘스트") == null) {
			message.put("진행중퀘스트", "&a진행중인 퀘스트");
		}
		if(message.get("진행중퀘스트설명") == null) {
			message.put("진행중퀘스트설명", "&b진행중인 퀘스트를 봅니다.");
		}
		if(message.get("완료퀘스트") == null) {
			message.put("완료퀘스트", "&a완료한 퀘스트");
		}
		if(message.get("완료퀘스트설명") == null) {
			message.put("완료퀘스트설명", "&b완료한 퀘스트목록을 봅니다.");
		}
		if(message.get("퀘스트GUI") == null) {
			message.put("퀘스트GUI", "&7퀘스트");
		}
		if(message.get("퀘스트목록") == null) {
			message.put("퀘스트목록", "퀘스트 목록 :");
		}
		if(message.get("퀘스트생성") == null) {
			message.put("퀘스트생성", "퀘스트를 생성하였습니다!");
		}
		if(message.get("동일퀘스트") == null) {
			message.put("동일퀘스트", "해당 이름의 퀘스트가 이미 존재합니다!");
		}
		if(message.get("없는퀘스트") == null) {
			message.put("없는퀘스트", "해당 이름을 가진 퀘스트가 없습니다.");
		}
		if(message.get("퀘스트설정") == null) {
			message.put("퀘스트설정", "&7퀘스트설정");
		}
		if(message.get("퀘스트제목설정") == null) {
			message.put("퀘스트제목설정", "&a제목설정");
		}
		if(message.get("퀘스트제목설명설정") == null) {
			message.put("퀘스트제목설명설정", "&b퀘스트 제목을 설정합니다.");
		}
		if(message.get("퀘스트대화설정") == null) {
			message.put("퀘스트대화설정", "&a대화설정");
		}
		if(message.get("퀘스트대화설명설정") == null) {
			message.put("퀘스트대화설명설정", "&b퀘스트 대화를 설정합니다.");
		}
		if(message.get("퀘스트조건설정") == null) {
			message.put("퀘스트조건설정", "&a클리어조건 아이템 설정");
		}
		if(message.get("퀘스트조건설명설정") == null) {
			message.put("퀘스트조건설명설정", "&b클리어조건에 필요한 아이템을 설정합니다.");
		}
		if(message.get("퀘스트보상설정") == null) {
			message.put("퀘스트보상설정", "&a보상설정");
		}
		if(message.get("퀘스트보상설명설정") == null) {
			message.put("퀘스트보상설명설정", "&b퀘스트 보상 아이템을 설정합니다.");
		}
		if(message.get("퀘스트설명설정") == null) {
			message.put("퀘스트설명설정", "&a설명설정");
		}
		if(message.get("퀘스트설명설명설정") == null) {
			message.put("퀘스트설명설명설정", "&b퀘스트 설명을 설정합니다.");
		}
	}
	
	public void questconfig(YamlConfiguration questconfig) {
		for(String key : questconfig.getKeys(false)) {
			quest quest = new quest(key);
			quest.setifitem((ArrayList<ItemStack>) questconfig.get(key + ".ifitem"));
			quest.setTitle(questconfig.getString(key + ".title"));
			quest.setlore(questconfig.getString(key + ".lore"));
			quest.setpresentitem((ArrayList<ItemStack>) questconfig.get(key + ".presenditem"));
			quest.settalk((ArrayList<String>) questconfig.getStringList(key + ".talk"));
			questlist.add(quest);
		}
	}
	
	public void playerquestconfig(YamlConfiguration playerquestconfig) {
		for(String key : playerquestconfig.getKeys(false)) {
			playerquest playerquest = new playerquest();
			playerquest.setquesting((ArrayList<String>) playerquestconfig.getStringList(key + ".questing"));
			playerquest.setclearquest((ArrayList<String>) playerquestconfig.getStringList(key + ".clearquest"));
			playerquestlist.put(Bukkit.getOfflinePlayer(key), playerquest);
		}
	}
	
	public String getString(String string) {
		return message.get(string).replace("&", "§");
	}
	
	public void onEnable() {
		Bukkit.getPluginManager().registerEvents(this, this);
		File messagefile = new File(getDataFolder() + "/message.yml");
		YamlConfiguration messageconfig = YamlConfiguration.loadConfiguration(messagefile);
		File questfile = new File(getDataFolder() + "/quest.yml");
		YamlConfiguration questconfig = YamlConfiguration.loadConfiguration(questfile);
		File playerquestfile = new File(getDataFolder() + "/playerquest.yml");
		YamlConfiguration playerquestconfig = YamlConfiguration.loadConfiguration(playerquestfile);
		file.put("message", messagefile);
		config.put("message", messageconfig);
		file.put("quest", questfile);
		config.put("quest", questconfig);
		file.put("playerquest", playerquestfile);
		config.put("playerquest", playerquestconfig);
		messageconfig(messageconfig);
		questconfig(questconfig);
		playerquestconfig(playerquestconfig);
		firstsave();
		save();
	}
	
	public void onDisable() {
		save();
	}
	
	public Inventory questGUI() {
		Inventory inv = Bukkit.createInventory(null, 27, getString("퀘스트GUI"));
		//진행중인퀘스트
		ItemStack i1 = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta m1 = i1.getItemMeta();
		m1.setDisplayName(getString("진행중퀘스트"));
		m1.setLore(Arrays.asList(getString("진행중퀘스트설명")));
		i1.setItemMeta(m1);
		//완료한퀘스트
		ItemStack i2 = new ItemStack(Material.BOOK);
		ItemMeta m2 = i2.getItemMeta();
		m2.setDisplayName(getString("완료퀘스트"));
		m2.setLore(Arrays.asList(getString("완료퀘스트설명")));
		m2.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		m2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		i2.setItemMeta(m2);
		//인벤토리설정
		inv.setItem(11, i1);
		inv.setItem(15, i2);
		return inv;
	}
	
	public Inventory questoptionGUI() {
		Inventory inv = Bukkit.createInventory(null, 27, getString("퀘스트설정"));
		//제목설정
		ItemStack i1 = new ItemStack(Material.SIGN);
		ItemMeta m1 = i1.getItemMeta();
		m1.setDisplayName(getString("퀘스트제목설정"));
		m1.setLore(Arrays.asList(getString("퀘스트제목설명설정")));
		i1.setItemMeta(m1);
		//대화설정
		ItemStack i2 = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta m2 = i2.getItemMeta();
		m2.setDisplayName(getString("퀘스트대화설정"));
		m2.setLore(Arrays.asList(getString("퀘스트대화설명설정")));
		i2.setItemMeta(m2);
		//조건아이템설정
		ItemStack i3 = new ItemStack(Material.MAP);
		ItemMeta m3 = i3.getItemMeta();
		m3.setDisplayName(getString("퀘스트조건설정"));
		m3.setLore(Arrays.asList(getString("퀘스트조건설명설정")));
		i3.setItemMeta(m3);
		//보상아이템설정
		ItemStack i4 = new ItemStack(Material.PAPER);
		ItemMeta m4 = i4.getItemMeta();
		m4.setDisplayName(getString("퀘스트보상설정"));
		m4.setLore(Arrays.asList(getString("퀘스트보상설명설정")));
		i4.setItemMeta(m4);
		//설명설정
		ItemStack i5 = new ItemStack(Material.NAME_TAG);
		ItemMeta m5 = i5.getItemMeta();
		m5.setDisplayName(getString("퀘스트설명설정"));
		m5.setLore(Arrays.asList(getString("퀘스트설명설명설정")));
		i5.setItemMeta(m5);
		//인벤토리설정
		inv.setItem(10, i1);
		inv.setItem(11, i5);
		inv.setItem(13, i2);
		inv.setItem(15, i3);
		inv.setItem(16, i4);
		return inv;
	}
	
	public void firstsave() {
		File messagefile = file.get("message");
		YamlConfiguration messageconfig = config.get("message");
		for(Entry<String, String> entry : message.entrySet()) {
			messageconfig.set(entry.getKey(), entry.getValue());
		}
		try {
			messageconfig.save(messagefile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean samenamequest(String name) {
		for(quest quest : questlist) {
			if(quest.getname().contentEquals(name)) {
				return true;
			}
		}
		return false;
	}
	
	public void removenamequest(String name) {
		try {
			for(quest quest : questlist) {
				if(quest.getname().contentEquals(name)) {
					questlist.remove(quest);
				}
			}
		} catch (Exception e) {
		}
	}
	
	public quest getquest(String name) {
		for(quest quest : questlist) {
			if(quest.getname().contentEquals(name)) {
				return quest;
			}
		}
		return null;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			Player p = (Player) sender;
			if(command.getName().contentEquals("퀘스트")) {
				if(args.length == 0) {
					p.openInventory(questGUI());
				}else {
					p.sendMessage(getString("접두어") + getString("명령어오류"));
				}
			}else if(command.getName().contentEquals("퀘스트설정")) {
				if(args.length == 0) {
					p.sendMessage(getString("접두어") + "/퀘스트설정 생성 <이름> - 퀘스트를 생성합니다.");
					p.sendMessage(getString("접두어") + "/퀘스트설정 삭제 <이름> - 퀘스트를 삭제합니다.");
					p.sendMessage(getString("접두어") + "/퀘스트설정 설정 <이름> - 퀘스트를 설정합니다.");
					p.sendMessage(getString("접두어") + "/퀘스트설정 npc <이름> <npc이름> - 퀘스트를 npc에 적용합니다.");
					p.sendMessage(getString("접두어") + "/퀘스트설정 목록 - 퀘스트 목록을 봅니다.");
				}else if(args.length == 1){
					if(args[0].toString().contentEquals("생성")) {
						p.sendMessage(getString("접두어") + "/퀘스트설정 생성 <이름> - 퀘스트를 생성합니다.");
					}else if(args[0].toString().contentEquals("삭제")) {
						p.sendMessage(getString("접두어") + "/퀘스트설정 삭제 <이름> - 퀘스트를 삭제합니다.");
					}else if(args[0].toString().contentEquals("설정")) {
						p.sendMessage(getString("접두어") + "/퀘스트설정 설정 <이름> - 퀘스트를 설정합니다.");
					}else if(args[0].toString().contentEquals("npc")) {
						p.sendMessage(getString("접두어") + "/퀘스트설정 npc <이름> <npc이름> - 퀘스트를 npc에 적용합니다.");
					}else if(args[0].toString().contentEquals("목록")) {
						String string = "[";
						boolean b = false;
						for(quest quest : questlist) {
							string = string + quest.getname() + ", ";
							b = true;
						}
						if(b) {
							string = string.substring(0, string.length() - 2);
						}
						string = string + "]";
						p.sendMessage(getString("접두어") + getString("퀘스트목록"));
						p.sendMessage(string);
					}else {
						p.sendMessage(getString("접두어") + getString("명령어오류"));
					}
				}else if(args.length == 2) {
                    if(args[0].toString().contentEquals("생성")) {
						if(!samenamequest(args[1].toString())) {
							quest quest = new quest(args[1].toString());
							questlist.add(quest);
							p.sendMessage(getString("접두어") + getString("퀘스트생성"));
						}else {
							p.sendMessage(getString("접두어") + getString("동일퀘스트"));
						}
					}else if(args[0].toString().contentEquals("삭제")) {
						if(samenamequest(args[1].toString())) {
							removenamequest(args[1].toString());
						}else {
							p.sendMessage(getString("접두어") + getString("없는퀘스트"));
						}
					}else if(args[0].toString().contentEquals("설정")) {
						if(samenamequest(args[1].toString())) {
							optionquest.put(p, getquest(args[1].toString()));
							p.openInventory(questoptionGUI());
						}else {
							p.sendMessage(getString("접두어") + getString("없는퀘스트"));
						}
					}else if(args[0].toString().contentEquals("npc")) {
						p.sendMessage(getString("접두어") + "/퀘스트설정 npc <이름> <npc이름> - 퀘스트를 npc에 적용합니다.");
					}else {
						p.sendMessage(getString("접두어") + getString("명령어오류"));
					}
				}else if(args.length == 3) {
                    if(args[0].toString().contentEquals("npc")) {
                    	
					}else {
						p.sendMessage(getString("접두어") + getString("명령어오류"));
					}
				}else {
					p.sendMessage(getString("접두어") + getString("명령어오류"));
				}
			}else if(command.getName().contentEquals("퀘스트관리")) {
				
			}else {
				p.sendMessage(getString("접두어") + getString("명령어오류"));
			}
		}else {
			System.out.println("플레이어만 칠 수 있는명령어입니다.");
		}
		return super.onCommand(sender, command, label, args);
	}
	
	public void save() {
		File questfile = file.get("quest");
		YamlConfiguration questconfig = config.get("quest");
		File playerquestfile = file.get("playerquest");
		YamlConfiguration playerquestconfig = config.get("playerquest");
		for(String key : questconfig.getKeys(false)) {
			questconfig.set(key, null);
		}
		for(quest quest : questlist) {
			questconfig.set(quest.getname() + ".title", quest.getTitle());
			questconfig.set(quest.getname() + ".lore", quest.getlore());
			questconfig.set(quest.getname() + ".ifitem", quest.getifitem());
			questconfig.set(quest.getname() + ".presenditem", quest.getpresenditem());
			questconfig.set(quest.getname() + ".talk", quest.gettalk());
		}
		for(Entry<OfflinePlayer, playerquest> entry : playerquestlist.entrySet()) {
			playerquest playerquest = entry.getValue();
			playerquestconfig.set(entry.getKey().getName() + ".questing", playerquest.getquesting());
			playerquestconfig.set(entry.getKey().getName() + ".clearquest", playerquest.getclearquest());
		}
		try {
			questconfig.save(questfile);
			playerquestconfig.save(playerquestfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@EventHandler
	public void worldsave(WorldSaveEvent e) {
		save();
	}

}
