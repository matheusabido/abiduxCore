package br.com.abidux.core.inventory;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

public class Menu {
	
	protected static final Set<Menu> menus = new HashSet<>();
	
	public final String name;
	public final int columns;
	public final InventoryItem[] items;
	public final Inventory commonInventory;
	public Menu(String name, int columns, InventoryItem[] items) {
		this.name = name;
		this.columns = columns;
		this.items = items;
		this.commonInventory = getNewInventory();
	}
	
	public void register() {
		if (!menus.contains(this))
			menus.add(this);
	}
	
	public Inventory getNewInventory() {
		Inventory inventory = Bukkit.createInventory(null, columns * 9, name);
		for (InventoryItem item : items)
			inventory.setItem(item.slot, item.itemStack);
		return inventory;
	}
	
}