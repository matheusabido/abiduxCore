package br.com.abidux.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Core extends JavaPlugin {
	
	@Override
	public void onEnable() {
		String lastVersion;
		try {
			lastVersion = getLastVersion();
		} catch (Exception e) {
			lastVersion = getDescription().getVersion();
		}
		if (!getDescription().getVersion().equals(lastVersion))
			Bukkit.getConsoleSender().sendMessage("§c[abiduxCore] Versão mais recente (" + lastVersion + ") detectada. Por favor, instale-a.");
	}
	
	private static String getLastVersion() throws IOException {
		try(BufferedReader reader = new BufferedReader(new InputStreamReader(new URL("https://raw.githubusercontent.com/mabidux/abiduxCore/main/src/plugin.yml").openStream()))) {
			return reader.lines().collect(Collectors.toList()).get(1).split("version: ")[1];
		}
	}
	
}