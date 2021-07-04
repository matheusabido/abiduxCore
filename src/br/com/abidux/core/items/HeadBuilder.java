package br.com.abidux.core.items;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import org.bukkit.Material;
import org.bukkit.Warning;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class HeadBuilder extends ItemBuilder {
	
	public HeadBuilder() {
		super(Material.SKULL_ITEM);
		durability = 3;
	}
	
	@Override
	public HeadBuilder enchantment(Enchantment enchantment, int level) {
		super.enchantment(enchantment, level);
		return this;
	}
	
	@Override
	public HeadBuilder flags(ItemFlag... flags) {
		super.flags(flags);
		return this;
	}
	
	@Override
	public HeadBuilder lore(String... lore) {
		super.lore(lore);
		return this;
	}
	
	@Override
	public HeadBuilder name(String name) {
		super.name(name);
		return this;
	}
	
	
	private String textures;
	@Warning(reason = "This is a slow method, don't use it repeatedly")
	public HeadBuilder texturesByMinecraftHeadsLink(String link) {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(link).openStream()))) {
			String text = reader.lines().collect(Collectors.joining());
			this.textures = text.split("<textarea class=\"codes\" id=\"UUID-Value\" readonly=\"true\"")[1].split(">")[1].split("<")[0];
		} catch (IOException e) {}
		return this;
	}
	
	public HeadBuilder textures(String textures) {
		this.textures = textures;
		return this;
	}
	
	@Override
	public ItemStack build() {
		ItemStack item = new ItemStack(material);
		item.setDurability(durability);
		if (enchantments.size() > 0) item.addUnsafeEnchantments(enchantments);
		SkullMeta meta = (SkullMeta) item.getItemMeta();
		if (name != null) meta.setDisplayName(name);
		if (lore != null) meta.setLore(Arrays.asList(lore));
		if (flags != null) meta.addItemFlags(flags);
		if (textures != null) {
			GameProfile profile = new GameProfile(UUID.randomUUID(), null);
	        profile.getProperties().put("textures", new Property("textures", textures));
	        try {
	            Field field = meta.getClass().getDeclaredField("profile");
	            field.setAccessible(true);
	            field.set(meta, profile);
	        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
	            ex.printStackTrace();
	        }
		}
		item.setItemMeta(meta);
		return item;
	}
	
}