package taewookim562;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_16_R2.command.BukkitCommandWrapper;
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
	
	static Map<String, String> message = new HashMap<String, String>();
	static Map<String, File> file = new HashMap<String, File>();
	static Map<String, YamlConfiguration> config = new HashMap<String, YamlConfiguration>();
	static Map<Player, OfflinePlayer> getquestplayer = new HashMap<Player, OfflinePlayer>();
	static Map<String, String> questname = new HashMap<String, String>();
	static Map<String, String> questlore = new HashMap<String, String>();
	static Map<Player, Integer> page = new HashMap<Player, Integer>();
	static Map<Player, String> setquestoption = new HashMap<Player, String>();
	static Map<String, Boolean> questclearitem = new HashMap<String, Boolean>();
	static Map<String, Boolean> repeatingquest = new HashMap<String, Boolean>();
	static Map<OfflinePlayer, ArrayList<String>> clearquest = new HashMap<OfflinePlayer, ArrayList<String>>();
	static Map<OfflinePlayer, ArrayList<String>> questing = new HashMap<OfflinePlayer, ArrayList<String>>();
	
	public void onEnable() {
		if(!(getDescription().getAuthors().size() == 1 && getDescription().getAuthors().get(1).toString() == "taewookim562")) {
			System.out.println("----------quest----------\n"
					+ "무언가 잘못되었습니다.!\n"
					+ "플러그인 제작자 : taewookim562\n"
					+ "플러그인을 다시 다운로드 해주세요.\n"
					+ "https://github.com/catking562/minecraft-plugins-quest.git\n"
					+ "-------------------------");
			Bukkit.getPluginManager().disablePlugin(this);
			return;
		}
		File messagefile = new File(getDataFolder() + "/message.yml");
		File questfile = new File(getDataFolder() + "/quest.yml");
		File playerfile = new File(getDataFolder() + "/player.yml");
		YamlConfiguration messageconfig = YamlConfiguration.loadConfiguration(messagefile);
		YamlConfiguration questconfig = YamlConfiguration.loadConfiguration(questfile);
		YamlConfiguration playerconfig = YamlConfiguration.loadConfiguration(playerfile);
		ArrayList<String> meli = new ArrayList<String>();
		//messageconfig
        if(messageconfig.getString("접두어") == null) {
        	messageconfig.set("접두어", "&f[ &equest&f] ");
        }
        for(String s : meli) {
        	message.put(s, messageconfig.getString(s));
        }
        meli.add("접두어");
        //questconfig
        ArrayList<String> quli = (ArrayList<String>) questconfig.getStringList("questlist");
        if(quli.size() != 0) {
        	for(String string : quli) {
        		questname.put(string, questconfig.getString(string + ".name"));
        		questlore.put(string, questconfig.getString(string + ".lore"));
        		questclearitem.put(string, questconfig.getBoolean(string + ".clearitem"));
        		repeatingquest.put(string, questconfig.getBoolean(string + ".repeating"));
        	}
        }
        //playerconfig
        if(Bukkit.getOfflinePlayers().length != 0) {
        	for(OfflinePlayer offp : Bukkit.getOfflinePlayers()) {
        		ArrayList<String> clli = (ArrayList<String>) playerconfig.getStringList(offp.getName() + ".clearlist");
        		ArrayList<String> qeli = (ArrayList<String>) playerconfig.getStringList(offp.getName() + ".questinglist");
        		if(clli.size() != 0) {
        			clearquest.put(offp, clli);
        		}
        		if(qeli.size() != 0) {
        			questing.put(offp, qeli);
        		}
        	}
        }
        try {
			messageconfig.save(messagefile);
			questconfig.save(questfile);
			playerconfig.save(playerfile);
			Bukkit.broadcastMessage("§a플러그인이 정상적으로 작동되었습니다. (quest 플러그인)");
		} catch (IOException e) {
			Bukkit.broadcastMessage("§4플러그인이 정상적으로 작동되지 않았습니다. (quest 플러그인)");
		}
        file.put("message", messagefile);
        config.put("message", messageconfig);
        file.put("quest", questfile);
        config.put("quest", questconfig);
        file.put("player", playerfile);
        config.put("player", playerconfig);
        return;
	}
	
	public String SetColorInString(String string) {
		return string.replace("&", "§");
	}
	
	public void questmaingui(Player player, OfflinePlayer questplayer) {
		Inventory inv = Bukkit.createInventory(player, 27, "§7퀘스트");
		ItemStack i = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemStack i1 = new ItemStack(Material.ENCHANTED_BOOK);
		ItemStack i2 = new ItemStack(Material.BOOK);
		ItemStack i3 = new ItemStack(Material.BOOK);
		ItemMeta m = i.getItemMeta();
		ItemMeta m1 = i1.getItemMeta();
		ItemMeta m2 = i2.getItemMeta();
		ItemMeta m3 = i3.getItemMeta();
		m.setDisplayName("");
		m1.setDisplayName("§a완료한 퀘스트 목록");
		m2.setDisplayName("§c완료하지 못한 퀘스트 목록");
		m3.setDisplayName("§7시작 가능한 퀘스트 목록");
		for (int j = 0; j < 27; j++) {
			inv.setItem(j, i);
		}
		inv.setItem(10, i1);
		inv.setItem(13, i2);
		inv.setItem(16, i3);
		player.openInventory(inv);
		getquestplayer.put(player, questplayer);
		return;
	}
	
	public void questlist(Player p) {
		Inventory inv = Bukkit.createInventory(p, 54, "§7퀘스트목록");
		ItemStack i = new ItemStack(Material.BOOK);
		ItemMeta m = i.getItemMeta();
		int a = 0;
		int b = 0;
		page.put(p, 0);
		for(Map.Entry<String, String> string : questname.entrySet()) {
			if(b + page.get(p) * 35 == a) {
				m.setDisplayName(string.getValue());
				m.setLore(Arrays.asList(questlore.get(string.getKey())));
				i.setItemMeta(m);
				inv.setItem(b, i);
				b++;
			}
			a++;
		}
		ItemStack i1 = new ItemStack(Material.PAPER);
		ItemStack i2 = new ItemStack(Material.PAPER);
		ItemStack i3 = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta m1 = i1.getItemMeta();
		ItemMeta m2 = i2.getItemMeta();
		ItemMeta m3 = i3.getItemMeta();
		m1.setDisplayName("§c이전페이지");
		m2.setDisplayName("§a다음페이지");
		m3.setDisplayName("§7" + (page.get(p) + 1) + "페이지");
		for (int j = 37; j < 54; j++) {
			inv.setItem(j, i3);
		}
		inv.setItem(36, i1);
		inv.setItem(54, i3);
		p.openInventory(inv);
		return;
	}
	
	public boolean isquest(String questname) {
		for(Map.Entry<String, String> string : main.questname.entrySet()) {
			if(string.getKey().equalsIgnoreCase(questname)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean addquest(String string) {
		if(!isquest(string)) {
			questname.put(string, "null");
			questlore.put(string, "null");
			return true;
		}
		return false;
	}
	
	public boolean removequest(String string) {
		if(isquest(string)) {
			questname.put(string, null);
			questlore.put(string, null);
			return false;
		}
		return false;
	}
	
	public void setoptionquest(Player p, String string) {
		Inventory inv = Bukkit.createInventory(p, 54, "§7퀘스트 옵션 변경");
		ItemStack i = new ItemStack(Material.WHITE_STAINED_GLASS_PANE);
		ItemMeta m = i.getItemMeta();
		m.setDisplayName("");
		i.setItemMeta(m);
		for (int j = 0; j < 54; j++) {
			inv.setItem(j, i);
		}
		ItemStack i1 = new ItemStack(Material.BOOK);
		ItemStack i2 = new ItemStack(Material.BOOKSHELF);
		ItemStack i3 = new ItemStack(Material.ITEM_FRAME);
		ItemStack i4 = new ItemStack(Material.WRITTEN_BOOK);
		ItemStack i5 = new ItemStack(Material.NETHER_STAR);
		ItemStack i6 = new ItemStack(Material.CHEST_MINECART);
		ItemStack i7 = new ItemStack(Material.MINECART);
		ItemMeta m1 = i1.getItemMeta();
		ItemMeta m2 = i2.getItemMeta();
		ItemMeta m3 = i3.getItemMeta();
		ItemMeta m4 = i4.getItemMeta();
		ItemMeta m5 = i5.getItemMeta();
		ItemMeta m6 = i6.getItemMeta();
		ItemMeta m7 = i7.getItemMeta();
		m1.setDisplayName("§a이름변경");
		m2.setDisplayName("§5선행퀘스트 설정");
		m3.setDisplayName("§6클리어 조건 설정");
		m4.setDisplayName("§e설명변경");
		m5.setDisplayName("§b보상설정");
		m6.setDisplayName("§c클리어 시 아이템 회수여부");
		m7.setDisplayName("§d반복퀘스트여부");
		m1.setLore(Arrays.asList("§7퀘스트 이름을 변경합니다."));
		m2.setLore(Arrays.asList("§7이 퀘스트를 시작하기 위한 조건으로, 먼저 클리어 해야하는 퀘스트를 설정합니다."));
		m3.setLore(Arrays.asList("§7퀘스트 클리어를 위해 필요한 아이템을 설정 할 수 있습니다."));
		m4.setLore(Arrays.asList("§7퀘스트 설명문을 작성할 수 있습니다."));
		m5.setLore(Arrays.asList("§7퀘스트 클리어 시 보상을 설정합니다."));
		if(questclearitem.get(string)) {
			m6.setLore(Arrays.asList("§a퀘스트 클리어 시 조건으로 설정된 아이템을 회수합니다."));
			m6.addEnchant(Enchantment.ARROW_FIRE, 1, true);
			m6.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}else {
			m6.setLore(Arrays.asList("§c퀘스트 클리어 시 조건으로 설정된 아이템을 회수하지 않습니다."));
		}
		if(repeatingquest.get(string)) {
			m7.setLore(Arrays.asList("§a퀘스트를 클리어했음에도 클리어목록에 등재되지 않고 다시 할 수 있음."));
			m7.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			m7.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}else {
			m7.setLore(Arrays.asList("§c퀘스트를 클리어하면 클리어목록에 등재되고 다시 할 수 없음."));
		}
		i1.setItemMeta(m1);
		i2.setItemMeta(m2);
		i3.setItemMeta(m3);
		i4.setItemMeta(m4);
		i5.setItemMeta(m5);
		i6.setItemMeta(m6);
		i7.setItemMeta(m7);
		inv.setItem(10, i1);
		inv.setItem(12, i2);
		inv.setItem(14, i3);
		inv.setItem(16, i4);
		inv.setItem(38, i5);
		inv.setItem(41, i6);
		inv.setItem(44, i7);
		setquestoption.put(p, string);
		p.openInventory(inv);
	}
	
	public boolean isplayer(String string) {
		for(OfflinePlayer offp : Bukkit.getOfflinePlayers()) {
			if(offp.getName().equalsIgnoreCase(string)) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isclearquest(OfflinePlayer p, String string) {
		for(String str : clearquest.get(p)) {
			if(string.equalsIgnoreCase(str)) {
				return true;
			}
		}
		return false;
	}
	
	public void removeplayerquest(OfflinePlayer offlineplayer, String string) {
		ArrayList<String> clearlist = clearquest.get(offlineplayer);
		for (int i = 0; i < clearlist.size(); i++) {
			if(clearlist.get(i).equalsIgnoreCase(string)) {
				clearlist.remove(i);
			}
		}
		clearquest.put(offlineplayer, clearlist);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(sender instanceof Player) {
			if(sender.hasPermission(command.getPermission())) {
				if(command.getName().equalsIgnoreCase("붸스트") || command.getName().equalsIgnoreCase("quest")) {
					questmaingui((Player) sender, (Player) sender);
				}else if(command.getName().equalsIgnoreCase("퀘스트설정") || command.getName().equalsIgnoreCase("questoption")) {
					if(args.length == 0) {
						sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트옵션  --  퀘스트옵션에 대한 명령어들을 봅니다.");
						sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트옵션 추가 <이름>  --  <이름>을 가진 퀘스트를 새로 추가합니다.");
						sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트옵션 삭제 <이름>  --  해당 퀘스트를 삭제합니다.");
						sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트옵션 설정 <이름>  --  퀘스트를 설정할 수 있습니다.");
						sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트옵션 목록  --  퀘스트목록을 봅니다.");
					}else if(args.length == 1) {
						if(args[0].toString().equalsIgnoreCase("추가")) {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트옵션 추가 <이름>  --  <이름>을 가진 퀘스트를 새로 추가합니다.");
						}else if(args[0].toString().equalsIgnoreCase("삭제")) {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트옵션 삭제 <이름>  --  해당 퀘스트를 삭제합니다.");
						}else if(args[0].toString().equalsIgnoreCase("설정")) {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트옵션 설정 <이름>  --  퀘스트를 설정할 수 있습니다.");
						}else if(args[0].toString().equalsIgnoreCase("목록")) {
							questlist((Player) sender);
						}else {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "무슨 명령어인지 모르겠어요!");
						}
					}else if(args.length == 2) {
						if(args[0].toString().equalsIgnoreCase("추가")) {
							if(addquest(args[1].toString())) {
								sender.sendMessage(SetColorInString(message.get("접두어")) + "퀘스트를 추가했습니다.");
							}else {
								sender.sendMessage(SetColorInString(message.get("접두어")) + "이미 존재하는 퀘스트입니다.");
							}
						}else if(args[0].toString().equalsIgnoreCase("삭제")) {
							if(removequest(args[1].toString())) {
								sender.sendMessage(SetColorInString(message.get("접두어")) + "퀘스트가 삭제되었습니다.");
							}else {
								sender.sendMessage(SetColorInString(message.get("접두어")) + "퀘스트를 찾을 수 없습니다.");
							}
						}else if(args[0].toString().equalsIgnoreCase("설정")) {
							if(isquest(args[1])) {
								setoptionquest((Player) sender, args[1]);
							}else {
								sender.sendMessage(SetColorInString(message.get("접두어")) + "퀘스트를 찾을 수 없습니다.");
							}
						}else if(args[0].toString().equalsIgnoreCase("목록")) {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트옵션 목록  --  퀘스트목록을 봅니다.");
						}else {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "무슨 명령어인지 모르겠어요!");
						}
					}else {
						sender.sendMessage(SetColorInString(message.get("접두어")) + "무슨 명령어인지 모르겠어요!");
					}
				}else if(command.getName().equalsIgnoreCase("퀘스트관리") || command.getName().equalsIgnoreCase("quesmanager")) {
					if(args.length == 0) {
						sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트관리  --  퀘스트관리에 대한 명령어들을 봅니다.");
						sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트관리 추가 <이름> <퀘스트이름>  --  <이름>을 가진 플레이어에게 <퀘스트이름>을 완료합니다.");
						sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트관리 삭제 <이름> <퀘스트이름>  --  <이름>을 가신 플레이어에게 <퀘스트이름> 완료를 취소합니다.");
						sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트관리 목록 <이름>  --  해탕 플레이어의 퀘스트 목록을 엽니다.");
					}else if(args.length == 1) {
						if(args[0].toString().equalsIgnoreCase("추가")) {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트관리 추가 <이름> <퀘스트이름>  --  <이름>을 가진 플레이어에게 <퀘스트이름>을 완료합니다.");	
						}else if(args[0].toString().equalsIgnoreCase("삭제")) {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트관리 삭제 <이름> <퀘스트이름>  --  <이름>을 가신 플레이어에게 <퀘스트이름> 완료를 취소합니다.");
						}else if(args[0].toString().equalsIgnoreCase("목록")) {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트관리 목록 <이름>  --  해탕 플레이어의 퀘스트 목록을 엽니다.");
						}else {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "무슨 명령어인지 모르겠어요!");
						}
					}else if(args.length == 2) {
						if(args[0].toString().equalsIgnoreCase("추가")) {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트관리 추가 <이름> <퀘스트이름>  --  <이름>을 가진 플레이어에게 <퀘스트이름>을 완료합니다.");	
						}else if(args[0].toString().equalsIgnoreCase("삭제")) {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트관리 삭제 <이름> <퀘스트이름>  --  <이름>을 가신 플레이어에게 <퀘스트이름> 완료를 취소합니다.");
						}else if(args[0].toString().equalsIgnoreCase("목록")) {
							if(isplayer(args[1].toString())) {
								questmaingui((Player) sender, Bukkit.getOfflinePlayer(args[1].toString()));
							}else {
								sender.sendMessage(SetColorInString(message.get("접두어")) + "찾을 수 없는 플레이어입니다.");
							}
						}else {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "무슨 명령어인지 모르겠어요!");
						}
					}else if(args.length == 3) {
						if(args[0].toString().equalsIgnoreCase("추가")) {
							if(isplayer(args[1].toString())) {
								if(isquest(args[2].toString())) {
									sender.sendMessage(SetColorInString(message.get("접두어")) + "완료한 퀘스트를 하나 추가했습니다.");
									ArrayList<String> clearlist = clearquest.get(Bukkit.getOfflinePlayer(args[1]));
									clearlist.add(args[2]);
									clearquest.put(Bukkit.getOfflinePlayer(args[1]), clearlist);
								}else {
									sender.sendMessage(SetColorInString(message.get("접두어")) + "퀘스트를 찾을 수 없습니다.");
								}
							}else {
								sender.sendMessage(SetColorInString(message.get("접두어")) + "찾을 수 없는 플레이어입니다.");
							}
						}else if(args[0].toString().equalsIgnoreCase("삭제")) {
							if(isplayer(args[1].toString())) {
								if(isquest(args[2].toString())) {
									if(isclearquest(Bukkit.getOfflinePlayer(args[1]), args[2])) {
										removeplayerquest(Bukkit.getOfflinePlayer(args[1]), args[2]);
										sender.sendMessage(SetColorInString(message.get("접두어")) + "해당퀘스트를 제거했습니다.");
									}else {
										sender.sendMessage(SetColorInString(message.get("접두어")) + "해당플레이어는 해당퀘스트를 완료하지 않았습니다.");
									}
								}else {
									sender.sendMessage(SetColorInString(message.get("접두어")) + "퀘스트를 찾을 수 없습니다.");
								}
							}else {
								sender.sendMessage(SetColorInString(message.get("접두어")) + "찾을 수 없는 플레이어입니다.");
							}
						}else if(args[0].toString().equalsIgnoreCase("목록")) {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "/퀘스트관리 목록 <이름>  --  해탕 플레이어의 퀘스트 목록을 엽니다.");
						}else {
							sender.sendMessage(SetColorInString(message.get("접두어")) + "무슨 명령어인지 모르겠어요!");
						}
					}else {
						sender.sendMessage(SetColorInString(message.get("접두어")) + "무슨 명령어인지 모르겠어요!");
					}
				}
			}
		}else {
			System.out.println("이 명령어는 플레이어 제외하고 칠 수 없는 명령어입니다.! (quest 플러그인)");
		}
		return true;
	}
	
	@EventHandler
	public void worldsave(WorldSaveEvent e) {
		YamlConfiguration questconfig = config.get("quest");
		YamlConfiguration playerconfig = config.get("player");
		//questconfig
		ArrayList<String> quli = new ArrayList<String>();
		for(Map.Entry<String, String> questname : main.questname.entrySet()) {
			quli.add(questname.getKey());
			questconfig.set(questname.getKey() + ".name", questname.getValue());
			questconfig.set(questname.getKey() + ".lore", questlore.get(questname.getKey()));
			questconfig.set(questname.getKey() + ".clearitem", questclearitem.get(questname.getKey()));
			questconfig.set(questname.getKey() + ".repeating", repeatingquest.get(questname.getKey()));
		}
		//playerconfig
		if(Bukkit.getOfflinePlayers().length != 0) {
        	for(OfflinePlayer offp : Bukkit.getOfflinePlayers()) {
        		ArrayList<String> clli = clearquest.get(offp);
        		ArrayList<String> qeli = questing.get(offp);
        		if(clli.size() != 0) {
        			playerconfig.set(offp.getName() + ".clearlist", clli);
        		}
        		if(qeli.size() != 0) {
        			playerconfig.set(offp.getName() + ".questinglist", qeli);
        		}
        	}
        }
		try {
			for (Map.Entry<String, File> file : main.file.entrySet()) {
				if(!file.getKey().equalsIgnoreCase("message")) {
					config.get(file.getKey()).save(main.file.get(file.getKey()));
				}
			}
		} catch (Exception e2) {
			System.out.println("데이터가 정상적으로 저장됨 (quest 플러그인)");
		}
		return;
	}
}
