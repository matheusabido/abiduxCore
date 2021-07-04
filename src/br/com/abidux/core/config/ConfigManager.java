package br.com.abidux.core.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.List;
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
				boolean wasInstanciated = false;
				for (Field field : cls.getDeclaredFields()) {
					if (field.isAnnotationPresent(ConfigurationSection.class)) {
						if (!wasInstanciated) {
							cls.newInstance();
							wasInstanciated = true;
						}
						String name = field.getAnnotation(ConfigurationSection.class).name();
						writer.print(name + ":\n");
						for (Field f : field.getType().getDeclaredFields()) writeField(writer, "  ", f, field.get(null));
					} else writeField(writer, field, null);
				}
			} catch (FileNotFoundException | IllegalArgumentException | IllegalAccessException | InstantiationException ex) {
				return false;
			}
		}
		return true;
	}
	
	private static void writeField(PrintWriter writer, Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
		writeField(writer, "", field, object);
	}
	
	private static void writeField(PrintWriter writer, String prefix, Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
		if (field.getType().toString().equals("class java.lang.String")) {
			writer.print(prefix + field.getName().toLowerCase() + ": '" + field.get(object) + "'\n");
		} else if(field.getType().toString().equals("interface java.util.List")) {
			writer.print(prefix + field.getName().toLowerCase() + ":\n");
			List<?> list = (List<?>) field.get(object);
			saveList(writer, prefix + "  ", list);
		} else writer.print(prefix + field.getName().toLowerCase() + ": " + field.get(object) + "\n");
	}
	
	private static void saveList(PrintWriter writer, String prefix, List<?> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getClass().toString().equals("class java.lang.String")) {
				writer.print(prefix + "- '" + list.get(i) + "'\n");
			} else writer.print(prefix + "- " + list.get(i) + "\n");
		}
	}
	
	public static void loadConfigClass(File file, Class<?> cls) {
		YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
		for (Field field : cls.getDeclaredFields()) {
			try {
				if (field.isAnnotationPresent(ConfigurationSection.class)) {
					String name = field.getAnnotation(ConfigurationSection.class).name();
					for (Field f : field.getType().getDeclaredFields())
						loadField(config, name + ".", f, field.get(null));
				} else loadField(config, field, null);
			} catch (IllegalArgumentException | IllegalAccessException e) {continue;}
		}
	}
	
	private static void loadField(YamlConfiguration config, Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
		loadField(config, "", field, object);
	}
	
	private static void loadField(YamlConfiguration config, String prefix, Field field, Object object) throws IllegalArgumentException, IllegalAccessException {
		String type = field.getType().toString();
		String fieldName = field.getName().toLowerCase();
		if (type.equals("class java.lang.String")) field.set(object, config.getString(prefix + fieldName));
		else if (type.equals("int") || type.equals("class java.lang.Integer")) field.set(object, config.getInt(prefix + fieldName));
		else if (type.equals("double") || type.equals("class java.lang.Double")) field.set(object, config.getDouble(prefix + fieldName));
		else if (type.equals("float") || type.equals("class java.lang.Float")) field.set(object, (float) config.getDouble(prefix + fieldName));
		else if (type.equals("boolean") || type.equals("class java.lang.Boolean")) field.set(object, config.getBoolean(prefix + fieldName));
		else if (type.equals("interface java.util.List")) {
			if (field.getGenericType().getTypeName().equals("java.util.List<java.lang.String>")) {
				field.set(object, config.getStringList(prefix + fieldName));
			} else if (field.getGenericType().getTypeName().equals("java.util.List<java.lang.Integer>")) {
				field.set(object, config.getIntegerList(prefix + fieldName));
			}
		}
	}
	
	public static LocalFile createConfig(Plugin plugin, String fileName) {
		JarFile jar = JarUtils.getRunningJar(plugin.getClass());
		if (jar == null) return null;
		Enumeration<JarEntry> entries = jar.entries();
		LocalFile file = null;
		while (entries.hasMoreElements()) {
			JarEntry e = entries.nextElement();
			if (!e.getName().contains(fileName)) continue;
			file = new LocalFile(plugin.getDataFolder(), fileName);
			file.install(plugin);
			break;
		}
		try {
			jar.close();
		} catch (IOException e) {return null;}
		return file;
	}
	
}