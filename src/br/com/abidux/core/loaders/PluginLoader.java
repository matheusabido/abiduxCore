package br.com.abidux.core.loaders;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.abidux.core.utils.JarUtils;

public class PluginLoader {
	
	public static boolean loadPlugin(JavaPlugin plugin) {
		JarUtils.queueEntries(plugin.getClass(), e -> {
			if (!e.getName().endsWith(".class")) return;
			String className = e.getName().replace("/", ".").substring(0, e.getName().length() - 6);
			try {
				Class<?> cls = Class.forName(className);
				System.out.println(className);
				if (cls.isAnnotation() || cls.isInterface() || cls.isEnum()) return;
				if (Listener.class.isAssignableFrom(cls)) {
					Bukkit.getPluginManager().registerEvents((Listener) cls.newInstance(), plugin);
				} else if (CommandExecutor.class.isAssignableFrom(cls) && cls.isAnnotationPresent(CommandDetails.class)) {
					CommandDetails details = cls.getAnnotation(CommandDetails.class);
					System.out.println(details.commandName());
					plugin.getCommand(details.commandName()).setExecutor((CommandExecutor) cls.newInstance());
				}
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {}
		});
		return true;
	}
	
}