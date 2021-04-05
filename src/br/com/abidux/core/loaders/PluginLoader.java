package br.com.abidux.core.loaders;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;

public class PluginLoader {
	
	public static int[] loadPlugin(JavaPlugin plugin) {
		try {
			int[] data = {0, 0};
			for (ClassInfo info : ClassPath.from(plugin.getClass().getClassLoader()).getTopLevelClassesRecursive(plugin.getClass().getPackage().getName())) {
				Class<?> cls = Class.forName(info.getName());
				if (Listener.class.isAssignableFrom(cls)) {
					Bukkit.getPluginManager().registerEvents(cls.getName().equals(plugin.getClass().getName()) ? (Listener) plugin : (Listener) cls.newInstance(), plugin);
					for (Method method : cls.getDeclaredMethods()) {
						if (method.isAnnotationPresent(EventHandler.class)) {
							data[0]++;
						}
					}
				} else if (CommandExecutor.class.isAssignableFrom(cls)) {
					CommandExecutor executor = (CommandExecutor) cls.newInstance();
					String[] commands;
					if (cls.isAnnotationPresent(CommandInformation.class)) {
						commands = cls.getAnnotation(CommandInformation.class).commands();
					} else {
						try {
							Field field = cls.getDeclaredField("command");
							field.setAccessible(true);
							commands = new String[] {(String) field.get(executor)};
						} catch (NoSuchFieldException | SecurityException e) {
							try {
								Field field = cls.getDeclaredField("commands");
								field.setAccessible(true);
								commands = (String[]) field.get(executor);
							} catch (NoSuchFieldException | SecurityException e1) {
								continue;
							}
						}
					}
					for (String command : commands) {
						plugin.getCommand(command).setExecutor(executor);
						data[1]++;
					}
				}
			}
			return data;
		} catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			return null;
		}
	}
	
}