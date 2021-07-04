package br.com.abidux.core.items;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemBuilder {
	
	protected Material material;
	public ItemBuilder(Material material) {
		this.material = material;
	}
	
	protected String name;
	public ItemBuilder name(String name) {
		this.name = name;
		return this;
	}
	
	protected String[] lore;
	public ItemBuilder lore(String... lore) {
		this.lore = lore;
		return this;
	}
	
	protected short durability = 0;
	public ItemBuilder durability(int durability) {
		this.durability = (short) durability;
		return this;
	}
	
	protected ItemFlag[] flags;
	public ItemBuilder flags(ItemFlag... flags) {
		this.flags = flags;
		return this;
	}
	
	protected Map<Enchantment, Integer> enchantments = new HashMap<>();
	public ItemBuilder enchantment(Enchantment enchantment, int level) {
		enchantments.put(enchantment, level);
		return this;
	}
	
	public ItemStack build() {
		ItemStack item = new ItemStack(material);
		item.setDurability(durability);
		if (enchantments.size() > 0) item.addUnsafeEnchantments(enchantments);
		ItemMeta meta = item.getItemMeta();
		if (name != null) meta.setDisplayName(name);
		if (lore != null) meta.setLore(Arrays.asList(lore));
		if (flags != null) meta.addItemFlags(flags);
		item.setItemMeta(meta);
		return item;
	}
	
}