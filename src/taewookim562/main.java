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
		if(message.get("���ξ�") == null) {
			message.put("���ξ�", "&f[ &e����Ʈ &f] ");
		}
		if(message.get("��ɾ����") == null) {
			message.put("��ɾ����", "���� ��ɾ���?");
		}
		if(message.get("����������Ʈ") == null) {
			message.put("����������Ʈ", "&a�������� ����Ʈ");
		}
		if(message.get("����������Ʈ����") == null) {
			message.put("����������Ʈ����", "&b�������� ����Ʈ�� ���ϴ�.");
		}
		if(message.get("�Ϸ�����Ʈ") == null) {
			message.put("�Ϸ�����Ʈ", "&a�Ϸ��� ����Ʈ");
		}
		if(message.get("�Ϸ�����Ʈ����") == null) {
			message.put("�Ϸ�����Ʈ����", "&b�Ϸ��� ����Ʈ����� ���ϴ�.");
		}
		if(message.get("����ƮGUI") == null) {
			message.put("����ƮGUI", "&7����Ʈ");
		}
		if(message.get("����Ʈ���") == null) {
			message.put("����Ʈ���", "����Ʈ ��� :");
		}
		if(message.get("����Ʈ����") == null) {
			message.put("����Ʈ����", "����Ʈ�� �����Ͽ����ϴ�!");
		}
		if(message.get("��������Ʈ") == null) {
			message.put("��������Ʈ", "�ش� �̸��� ����Ʈ�� �̹� �����մϴ�!");
		}
		if(message.get("��������Ʈ") == null) {
			message.put("��������Ʈ", "�ش� �̸��� ���� ����Ʈ�� �����ϴ�.");
		}
		if(message.get("����Ʈ����") == null) {
			message.put("����Ʈ����", "&7����Ʈ����");
		}
		if(message.get("����Ʈ������") == null) {
			message.put("����Ʈ������", "&a������");
		}
		if(message.get("����Ʈ���񼳸���") == null) {
			message.put("����Ʈ���񼳸���", "&b����Ʈ ������ �����մϴ�.");
		}
		if(message.get("����Ʈ��ȭ����") == null) {
			message.put("����Ʈ��ȭ����", "&a��ȭ����");
		}
		if(message.get("����Ʈ��ȭ������") == null) {
			message.put("����Ʈ��ȭ������", "&b����Ʈ ��ȭ�� �����մϴ�.");
		}
		if(message.get("����Ʈ���Ǽ���") == null) {
			message.put("����Ʈ���Ǽ���", "&aŬ�������� ������ ����");
		}
		if(message.get("����Ʈ���Ǽ�����") == null) {
			message.put("����Ʈ���Ǽ�����", "&bŬ�������ǿ� �ʿ��� �������� �����մϴ�.");
		}
		if(message.get("����Ʈ������") == null) {
			message.put("����Ʈ������", "&a������");
		}
		if(message.get("����Ʈ���󼳸���") == null) {
			message.put("����Ʈ���󼳸���", "&b����Ʈ ���� �������� �����մϴ�.");
		}
		if(message.get("����Ʈ������") == null) {
			message.put("����Ʈ������", "&a������");
		}
		if(message.get("����Ʈ��������") == null) {
			message.put("����Ʈ��������", "&b����Ʈ ������ �����մϴ�.");
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
		return message.get(string).replace("&", "��");
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
		Inventory inv = Bukkit.createInventory(null, 27, getString("����ƮGUI"));
		//������������Ʈ
		ItemStack i1 = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta m1 = i1.getItemMeta();
		m1.setDisplayName(getString("����������Ʈ"));
		m1.setLore(Arrays.asList(getString("����������Ʈ����")));
		i1.setItemMeta(m1);
		//�Ϸ�������Ʈ
		ItemStack i2 = new ItemStack(Material.BOOK);
		ItemMeta m2 = i2.getItemMeta();
		m2.setDisplayName(getString("�Ϸ�����Ʈ"));
		m2.setLore(Arrays.asList(getString("�Ϸ�����Ʈ����")));
		m2.addEnchant(Enchantment.ARROW_DAMAGE, 1, true);
		m2.addItemFlags(ItemFlag.HIDE_ENCHANTS);
		i2.setItemMeta(m2);
		//�κ��丮����
		inv.setItem(11, i1);
		inv.setItem(15, i2);
		return inv;
	}
	
	public Inventory questoptionGUI() {
		Inventory inv = Bukkit.createInventory(null, 27, getString("����Ʈ����"));
		//������
		ItemStack i1 = new ItemStack(Material.SIGN);
		ItemMeta m1 = i1.getItemMeta();
		m1.setDisplayName(getString("����Ʈ������"));
		m1.setLore(Arrays.asList(getString("����Ʈ���񼳸���")));
		i1.setItemMeta(m1);
		//��ȭ����
		ItemStack i2 = new ItemStack(Material.BOOK_AND_QUILL);
		ItemMeta m2 = i2.getItemMeta();
		m2.setDisplayName(getString("����Ʈ��ȭ����"));
		m2.setLore(Arrays.asList(getString("����Ʈ��ȭ������")));
		i2.setItemMeta(m2);
		//���Ǿ����ۼ���
		ItemStack i3 = new ItemStack(Material.MAP);
		ItemMeta m3 = i3.getItemMeta();
		m3.setDisplayName(getString("����Ʈ���Ǽ���"));
		m3.setLore(Arrays.asList(getString("����Ʈ���Ǽ�����")));
		i3.setItemMeta(m3);
		//��������ۼ���
		ItemStack i4 = new ItemStack(Material.PAPER);
		ItemMeta m4 = i4.getItemMeta();
		m4.setDisplayName(getString("����Ʈ������"));
		m4.setLore(Arrays.asList(getString("����Ʈ���󼳸���")));
		i4.setItemMeta(m4);
		//������
		ItemStack i5 = new ItemStack(Material.NAME_TAG);
		ItemMeta m5 = i5.getItemMeta();
		m5.setDisplayName(getString("����Ʈ������"));
		m5.setLore(Arrays.asList(getString("����Ʈ��������")));
		i5.setItemMeta(m5);
		//�κ��丮����
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
			if(command.getName().contentEquals("����Ʈ")) {
				if(args.length == 0) {
					p.openInventory(questGUI());
				}else {
					p.sendMessage(getString("���ξ�") + getString("��ɾ����"));
				}
			}else if(command.getName().contentEquals("����Ʈ����")) {
				if(args.length == 0) {
					p.sendMessage(getString("���ξ�") + "/����Ʈ���� ���� <�̸�> - ����Ʈ�� �����մϴ�.");
					p.sendMessage(getString("���ξ�") + "/����Ʈ���� ���� <�̸�> - ����Ʈ�� �����մϴ�.");
					p.sendMessage(getString("���ξ�") + "/����Ʈ���� ���� <�̸�> - ����Ʈ�� �����մϴ�.");
					p.sendMessage(getString("���ξ�") + "/����Ʈ���� npc <�̸�> <npc�̸�> - ����Ʈ�� npc�� �����մϴ�.");
					p.sendMessage(getString("���ξ�") + "/����Ʈ���� ��� - ����Ʈ ����� ���ϴ�.");
				}else if(args.length == 1){
					if(args[0].toString().contentEquals("����")) {
						p.sendMessage(getString("���ξ�") + "/����Ʈ���� ���� <�̸�> - ����Ʈ�� �����մϴ�.");
					}else if(args[0].toString().contentEquals("����")) {
						p.sendMessage(getString("���ξ�") + "/����Ʈ���� ���� <�̸�> - ����Ʈ�� �����մϴ�.");
					}else if(args[0].toString().contentEquals("����")) {
						p.sendMessage(getString("���ξ�") + "/����Ʈ���� ���� <�̸�> - ����Ʈ�� �����մϴ�.");
					}else if(args[0].toString().contentEquals("npc")) {
						p.sendMessage(getString("���ξ�") + "/����Ʈ���� npc <�̸�> <npc�̸�> - ����Ʈ�� npc�� �����մϴ�.");
					}else if(args[0].toString().contentEquals("���")) {
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
						p.sendMessage(getString("���ξ�") + getString("����Ʈ���"));
						p.sendMessage(string);
					}else {
						p.sendMessage(getString("���ξ�") + getString("��ɾ����"));
					}
				}else if(args.length == 2) {
                    if(args[0].toString().contentEquals("����")) {
						if(!samenamequest(args[1].toString())) {
							quest quest = new quest(args[1].toString());
							questlist.add(quest);
							p.sendMessage(getString("���ξ�") + getString("����Ʈ����"));
						}else {
							p.sendMessage(getString("���ξ�") + getString("��������Ʈ"));
						}
					}else if(args[0].toString().contentEquals("����")) {
						if(samenamequest(args[1].toString())) {
							removenamequest(args[1].toString());
						}else {
							p.sendMessage(getString("���ξ�") + getString("��������Ʈ"));
						}
					}else if(args[0].toString().contentEquals("����")) {
						if(samenamequest(args[1].toString())) {
							optionquest.put(p, getquest(args[1].toString()));
							p.openInventory(questoptionGUI());
						}else {
							p.sendMessage(getString("���ξ�") + getString("��������Ʈ"));
						}
					}else if(args[0].toString().contentEquals("npc")) {
						p.sendMessage(getString("���ξ�") + "/����Ʈ���� npc <�̸�> <npc�̸�> - ����Ʈ�� npc�� �����մϴ�.");
					}else {
						p.sendMessage(getString("���ξ�") + getString("��ɾ����"));
					}
				}else if(args.length == 3) {
                    if(args[0].toString().contentEquals("npc")) {
                    	
					}else {
						p.sendMessage(getString("���ξ�") + getString("��ɾ����"));
					}
				}else {
					p.sendMessage(getString("���ξ�") + getString("��ɾ����"));
				}
			}else if(command.getName().contentEquals("����Ʈ����")) {
				
			}else {
				p.sendMessage(getString("���ξ�") + getString("��ɾ����"));
			}
		}else {
			System.out.println("�÷��̾ ĥ �� �ִ¸�ɾ��Դϴ�.");
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
