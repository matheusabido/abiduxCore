package br.com.abidux.core.inventory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class MenuClickListener implements Listener {
	
	@EventHandler
	void click(InventoryClickEvent event) {
		for (Menu menu : Menu.menus) {
			if (event.getClickedInventory().getTitle().equals(menu.name)) {
				event.setCancelled(true);
				if (menu instanceof ClickableMenu) {
					((ClickableMenu) menu).onClick(event);
				}
			}
		}
	}
	
}