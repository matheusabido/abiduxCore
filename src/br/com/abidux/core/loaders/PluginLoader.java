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
				if (cls.isAnnotation() || cls.isInterface() || cls.isEnum()) return;
				if (Listener.class.isAssignableFrom(cls)) {
					Listener listener = (Listener) (cls.getName().equals(plugin.getClass().getName()) ? plugin : cls.newInstance());
					Bukkit.getPluginManager().registerEvents(listener, plugin);
				} else if (CommandExecutor.class.isAssignableFrom(cls) && cls.isAnnotationPresent(CommandDetails.class)) {
					CommandDetails details = cls.getAnnotation(CommandDetails.class);
					CommandExecutor commandExecutor = (CommandExecutor) (cls.getName().equals(plugin.getClass().getName()) ? plugin : cls.newInstance());
					for (String command : details.commands())
						plugin.getCommand(command).setExecutor(commandExecutor);
				}
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {}
		});
		return true;
	}
	
}