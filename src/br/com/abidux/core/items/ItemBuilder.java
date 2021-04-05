package br.com.abidux.core.items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
	private String name;
	public ItemBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	private List<String> lore;
	public ItemBuilder lore(String... lore) {
		this.lore = Arrays.asList(lore);
		return this;
	}
	
	private List<Object[]> enchantments = new ArrayList<>();
	public ItemBuilder enchantment(Enchantment enchantment, int level) {
		enchantments.add(new Object[] {enchantment, level});
		return this;
	}
	
	private short durability = 0;
	public ItemBuilder durability(short durability) {
		this.durability = durability;
		return this;
	}
	
	private int amount = 1;
	public ItemBuilder amount(int amount) {
		this.amount = amount;
		return this;
	}
	
	private Material material = Material.STONE;
	public ItemBuilder material(Material material) {
		this.material = material;
		return this;
	}
	
	public ItemStack build() {
		ItemStack item = new ItemStack(material);
		item.setAmount(amount);
		item.setDurability(durability);
		for (Object[] current : enchantments) item.addUnsafeEnchantment((Enchantment) current[0], (int) current[1]);
		ItemMeta meta = item.getItemMeta();
		if (name != null) meta.setDisplayName(name);
		if (lore != null) meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
	
}