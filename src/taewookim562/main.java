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
					+ "���� �߸��Ǿ����ϴ�.!\n"
					+ "�÷����� ������ : taewookim562\n"
					+ "�÷������� �ٽ� �ٿ�ε� ���ּ���.\n"
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
        if(messageconfig.getString("���ξ�") == null) {
        	messageconfig.set("���ξ�", "&f[ &equest&f] ");
        }
        for(String s : meli) {
        	message.put(s, messageconfig.getString(s));
        }
        meli.add("���ξ�");
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
			Bukkit.broadcastMessage("��a�÷������� ���������� �۵��Ǿ����ϴ�. (quest �÷�����)");
		} catch (IOException e) {
			Bukkit.broadcastMessage("��4�÷������� ���������� �۵����� �ʾҽ��ϴ�. (quest �÷�����)");
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
		return string.replace("&", "��");
	}
	
	public void questmaingui(Player player, OfflinePlayer questplayer) {
		Inventory inv = Bukkit.createInventory(player, 27, "��7����Ʈ");
		ItemStack i = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemStack i1 = new ItemStack(Material.ENCHANTED_BOOK);
		ItemStack i2 = new ItemStack(Material.BOOK);
		ItemStack i3 = new ItemStack(Material.BOOK);
		ItemMeta m = i.getItemMeta();
		ItemMeta m1 = i1.getItemMeta();
		ItemMeta m2 = i2.getItemMeta();
		ItemMeta m3 = i3.getItemMeta();
		m.setDisplayName("");
		m1.setDisplayName("��a�Ϸ��� ����Ʈ ���");
		m2.setDisplayName("��c�Ϸ����� ���� ����Ʈ ���");
		m3.setDisplayName("��7���� ������ ����Ʈ ���");
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
		Inventory inv = Bukkit.createInventory(p, 54, "��7����Ʈ���");
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
		m1.setDisplayName("��c����������");
		m2.setDisplayName("��a����������");
		m3.setDisplayName("��7" + (page.get(p) + 1) + "������");
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
		Inventory inv = Bukkit.createInventory(p, 54, "��7����Ʈ �ɼ� ����");
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
		m1.setDisplayName("��a�̸�����");
		m2.setDisplayName("��5��������Ʈ ����");
		m3.setDisplayName("��6Ŭ���� ���� ����");
		m4.setDisplayName("��e������");
		m5.setDisplayName("��b������");
		m6.setDisplayName("��cŬ���� �� ������ ȸ������");
		m7.setDisplayName("��d�ݺ�����Ʈ����");
		m1.setLore(Arrays.asList("��7����Ʈ �̸��� �����մϴ�."));
		m2.setLore(Arrays.asList("��7�� ����Ʈ�� �����ϱ� ���� ��������, ���� Ŭ���� �ؾ��ϴ� ����Ʈ�� �����մϴ�."));
		m3.setLore(Arrays.asList("��7����Ʈ Ŭ��� ���� �ʿ��� �������� ���� �� �� �ֽ��ϴ�."));
		m4.setLore(Arrays.asList("��7����Ʈ ������ �ۼ��� �� �ֽ��ϴ�."));
		m5.setLore(Arrays.asList("��7����Ʈ Ŭ���� �� ������ �����մϴ�."));
		if(questclearitem.get(string)) {
			m6.setLore(Arrays.asList("��a����Ʈ Ŭ���� �� �������� ������ �������� ȸ���մϴ�."));
			m6.addEnchant(Enchantment.ARROW_FIRE, 1, true);
			m6.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}else {
			m6.setLore(Arrays.asList("��c����Ʈ Ŭ���� �� �������� ������ �������� ȸ������ �ʽ��ϴ�."));
		}
		if(repeatingquest.get(string)) {
			m7.setLore(Arrays.asList("��a����Ʈ�� Ŭ������������ Ŭ�����Ͽ� ������� �ʰ� �ٽ� �� �� ����."));
			m7.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
			m7.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		}else {
			m7.setLore(Arrays.asList("��c����Ʈ�� Ŭ�����ϸ� Ŭ�����Ͽ� ����ǰ� �ٽ� �� �� ����."));
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
				if(command.getName().equalsIgnoreCase("�޽�Ʈ") || command.getName().equalsIgnoreCase("quest")) {
					questmaingui((Player) sender, (Player) sender);
				}else if(command.getName().equalsIgnoreCase("����Ʈ����") || command.getName().equalsIgnoreCase("questoption")) {
					if(args.length == 0) {
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ�ɼ�  --  ����Ʈ�ɼǿ� ���� ��ɾ���� ���ϴ�.");
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ�ɼ� �߰� <�̸�>  --  <�̸�>�� ���� ����Ʈ�� ���� �߰��մϴ�.");
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ�ɼ� ���� <�̸�>  --  �ش� ����Ʈ�� �����մϴ�.");
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ�ɼ� ���� <�̸�>  --  ����Ʈ�� ������ �� �ֽ��ϴ�.");
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ�ɼ� ���  --  ����Ʈ����� ���ϴ�.");
					}else if(args.length == 1) {
						if(args[0].toString().equalsIgnoreCase("�߰�")) {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ�ɼ� �߰� <�̸�>  --  <�̸�>�� ���� ����Ʈ�� ���� �߰��մϴ�.");
						}else if(args[0].toString().equalsIgnoreCase("����")) {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ�ɼ� ���� <�̸�>  --  �ش� ����Ʈ�� �����մϴ�.");
						}else if(args[0].toString().equalsIgnoreCase("����")) {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ�ɼ� ���� <�̸�>  --  ����Ʈ�� ������ �� �ֽ��ϴ�.");
						}else if(args[0].toString().equalsIgnoreCase("���")) {
							questlist((Player) sender);
						}else {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "���� ��ɾ����� �𸣰ھ��!");
						}
					}else if(args.length == 2) {
						if(args[0].toString().equalsIgnoreCase("�߰�")) {
							if(addquest(args[1].toString())) {
								sender.sendMessage(SetColorInString(message.get("���ξ�")) + "����Ʈ�� �߰��߽��ϴ�.");
							}else {
								sender.sendMessage(SetColorInString(message.get("���ξ�")) + "�̹� �����ϴ� ����Ʈ�Դϴ�.");
							}
						}else if(args[0].toString().equalsIgnoreCase("����")) {
							if(removequest(args[1].toString())) {
								sender.sendMessage(SetColorInString(message.get("���ξ�")) + "����Ʈ�� �����Ǿ����ϴ�.");
							}else {
								sender.sendMessage(SetColorInString(message.get("���ξ�")) + "����Ʈ�� ã�� �� �����ϴ�.");
							}
						}else if(args[0].toString().equalsIgnoreCase("����")) {
							if(isquest(args[1])) {
								setoptionquest((Player) sender, args[1]);
							}else {
								sender.sendMessage(SetColorInString(message.get("���ξ�")) + "����Ʈ�� ã�� �� �����ϴ�.");
							}
						}else if(args[0].toString().equalsIgnoreCase("���")) {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ�ɼ� ���  --  ����Ʈ����� ���ϴ�.");
						}else {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "���� ��ɾ����� �𸣰ھ��!");
						}
					}else {
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "���� ��ɾ����� �𸣰ھ��!");
					}
				}else if(command.getName().equalsIgnoreCase("����Ʈ����") || command.getName().equalsIgnoreCase("quesmanager")) {
					if(args.length == 0) {
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ����  --  ����Ʈ������ ���� ��ɾ���� ���ϴ�.");
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ���� �߰� <�̸�> <����Ʈ�̸�>  --  <�̸�>�� ���� �÷��̾�� <����Ʈ�̸�>�� �Ϸ��մϴ�.");
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ���� ���� <�̸�> <����Ʈ�̸�>  --  <�̸�>�� ���� �÷��̾�� <����Ʈ�̸�> �ϷḦ ����մϴ�.");
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ���� ��� <�̸�>  --  ���� �÷��̾��� ����Ʈ ����� ���ϴ�.");
					}else if(args.length == 1) {
						if(args[0].toString().equalsIgnoreCase("�߰�")) {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ���� �߰� <�̸�> <����Ʈ�̸�>  --  <�̸�>�� ���� �÷��̾�� <����Ʈ�̸�>�� �Ϸ��մϴ�.");	
						}else if(args[0].toString().equalsIgnoreCase("����")) {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ���� ���� <�̸�> <����Ʈ�̸�>  --  <�̸�>�� ���� �÷��̾�� <����Ʈ�̸�> �ϷḦ ����մϴ�.");
						}else if(args[0].toString().equalsIgnoreCase("���")) {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ���� ��� <�̸�>  --  ���� �÷��̾��� ����Ʈ ����� ���ϴ�.");
						}else {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "���� ��ɾ����� �𸣰ھ��!");
						}
					}else if(args.length == 2) {
						if(args[0].toString().equalsIgnoreCase("�߰�")) {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ���� �߰� <�̸�> <����Ʈ�̸�>  --  <�̸�>�� ���� �÷��̾�� <����Ʈ�̸�>�� �Ϸ��մϴ�.");	
						}else if(args[0].toString().equalsIgnoreCase("����")) {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ���� ���� <�̸�> <����Ʈ�̸�>  --  <�̸�>�� ���� �÷��̾�� <����Ʈ�̸�> �ϷḦ ����մϴ�.");
						}else if(args[0].toString().equalsIgnoreCase("���")) {
							if(isplayer(args[1].toString())) {
								questmaingui((Player) sender, Bukkit.getOfflinePlayer(args[1].toString()));
							}else {
								sender.sendMessage(SetColorInString(message.get("���ξ�")) + "ã�� �� ���� �÷��̾��Դϴ�.");
							}
						}else {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "���� ��ɾ����� �𸣰ھ��!");
						}
					}else if(args.length == 3) {
						if(args[0].toString().equalsIgnoreCase("�߰�")) {
							if(isplayer(args[1].toString())) {
								if(isquest(args[2].toString())) {
									sender.sendMessage(SetColorInString(message.get("���ξ�")) + "�Ϸ��� ����Ʈ�� �ϳ� �߰��߽��ϴ�.");
									ArrayList<String> clearlist = clearquest.get(Bukkit.getOfflinePlayer(args[1]));
									clearlist.add(args[2]);
									clearquest.put(Bukkit.getOfflinePlayer(args[1]), clearlist);
								}else {
									sender.sendMessage(SetColorInString(message.get("���ξ�")) + "����Ʈ�� ã�� �� �����ϴ�.");
								}
							}else {
								sender.sendMessage(SetColorInString(message.get("���ξ�")) + "ã�� �� ���� �÷��̾��Դϴ�.");
							}
						}else if(args[0].toString().equalsIgnoreCase("����")) {
							if(isplayer(args[1].toString())) {
								if(isquest(args[2].toString())) {
									if(isclearquest(Bukkit.getOfflinePlayer(args[1]), args[2])) {
										removeplayerquest(Bukkit.getOfflinePlayer(args[1]), args[2]);
										sender.sendMessage(SetColorInString(message.get("���ξ�")) + "�ش�����Ʈ�� �����߽��ϴ�.");
									}else {
										sender.sendMessage(SetColorInString(message.get("���ξ�")) + "�ش��÷��̾�� �ش�����Ʈ�� �Ϸ����� �ʾҽ��ϴ�.");
									}
								}else {
									sender.sendMessage(SetColorInString(message.get("���ξ�")) + "����Ʈ�� ã�� �� �����ϴ�.");
								}
							}else {
								sender.sendMessage(SetColorInString(message.get("���ξ�")) + "ã�� �� ���� �÷��̾��Դϴ�.");
							}
						}else if(args[0].toString().equalsIgnoreCase("���")) {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "/����Ʈ���� ��� <�̸�>  --  ���� �÷��̾��� ����Ʈ ����� ���ϴ�.");
						}else {
							sender.sendMessage(SetColorInString(message.get("���ξ�")) + "���� ��ɾ����� �𸣰ھ��!");
						}
					}else {
						sender.sendMessage(SetColorInString(message.get("���ξ�")) + "���� ��ɾ����� �𸣰ھ��!");
					}
				}
			}
		}else {
			System.out.println("�� ��ɾ�� �÷��̾� �����ϰ� ĥ �� ���� ��ɾ��Դϴ�.! (quest �÷�����)");
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
			System.out.println("�����Ͱ� ���������� ����� (quest �÷�����)");
		}
		return;
	}
}
