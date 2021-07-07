package taewookim562;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryInteractEvent;

public class inventoryclick implements Listener {
	
	@EventHandler
	public void inventory(InventoryInteractEvent e) {
		if(e.getWhoClicked() instanceof Player) {
			Player p = (Player) e.getWhoClicked();
			if(e.getView().getTitle().equalsIgnoreCase("¡×7Äù½ºÆ®")) {
				
			}
		}
	}
}
