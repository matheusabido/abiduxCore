package br.com.abidux.core.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import br.com.abidux.core.files.LocalFile;
import br.com.abidux.core.utils.JarUtils;

public class ConfigManager {
	
	public static void createConfigClass(Plugin plugin, String fileName, Class<?> cls) {
		if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdir();
		File file = new File(plugin.getDataFolder(), fileName);
		
		saveConfigClass(file, cls);
		loadConfigClass(file, cls);
	}
	
	public static boolean saveConfigClass(File file, Class<?> cls) {
		if (!file.exists()) {
			try (PrintWriter writer = new PrintWriter(file)) {
				for (Field field : cls.getDeclaredFields()) {
					if (field.getType().toString().equals("class java.lang.String")) {
						writer.print(field.getName().toLowerCase() + ": '" + field.get(null) + "'\n");
					} else writer.print(field.getName().toLowerCase() + ": " + field.get(null) + "\n");
				}
			} catch (FileNotFoundException | IllegalArgumentException | IllegalAccessException ex) {
				return false;
			}
		}
		return true;
	}
	
	public static void loadConfigClass(File file, Class<?> cls) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (Field field : cls.getDeclaredFields()) {
			String type = field.getType().toString();
			String fieldName = field.getName().toLowerCase();
			try {
				if (type.equals("class java.lang.String")) field.set(null, config.getString(fieldName));
				else if (type.equals("int") || type.equals("class java.lang.Integer")) field.set(null, config.getInt(fieldName));
				else if (type.equals("double") || type.equals("class java.lang.Double")) field.set(null, config.getDouble(fieldName));
				else if (type.equals("float") || type.equals("class java.lang.Float")) field.set(null, (float) config.getDouble(fieldName));
			} catch (IllegalArgumentException | IllegalAccessException e) {continue;}
		}
	}
	
	public static LocalFile createConfig(Plugin plugin, String fileName) {
		JarFile jar = JarUtils.getRunningJar();
		if (jar == null) return null;
		Enumeration<JarEntry> entries = jar.entries();
		LocalFile file = null;
		while (entries.hasMoreElements()) {
			JarEntry e = entries.nextElement();
			if (!e.getName().contains(fileName)) continue;
			file = new LocalFile(plugin.getDataFolder(), fileName);
			file.install();
			break;
		}
		try {
			jar.close();
		} catch (IOException e) {return null;}
		return file;
	}
	
}