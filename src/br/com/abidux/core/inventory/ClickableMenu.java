package br.com.abidux.core.inventory;

import org.bukkit.event.inventory.InventoryClickEvent;

public abstract class ClickableMenu extends Menu {

	public ClickableMenu(String name, int columns, InventoryItem[] items) {
		super(name, columns, items);
	}
	
	public abstract void onClick(InventoryClickEvent event);
	
}