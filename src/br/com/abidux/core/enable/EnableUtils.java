package br.com.abidux.core.enable;

import org.bukkit.Bukkit;

public class EnableUtils {
	
	public static void sendTitle(String... lines) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < lines.length; i++) {
			builder.append(lines[i]).append("\n");
		}
		Bukkit.getConsoleSender().sendMessage(builder.toString());
	}
	
}