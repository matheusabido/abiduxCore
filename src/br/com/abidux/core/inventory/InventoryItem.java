package br.com.abidux.core.inventory;

import org.bukkit.inventory.ItemStack;

public class InventoryItem {
	
	protected ItemStack itemStack;
	protected int slot;
	public InventoryItem(ItemStack itemStack, int slot) {
		this.itemStack = itemStack;
		this.slot = slot;
	}
	
}