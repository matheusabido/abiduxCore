package br.com.abidux.core;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import br.com.abidux.core.scheduler.Scheduler;

public class Core extends JavaPlugin {
	
	private static Set<Scheduler> syncSchedulers = new HashSet<>();
	private static Set<Scheduler> asyncSchedulers = new HashSet<>();
	
	@SuppressWarnings("deprecation")
	@Override
	public void onEnable() {
		String lastVersion;
		try {
			lastVersion = getLastVersion();
		} catch (Exception e) {
			lastVersion = getDescription().getVersion();
		}
		if (!getDescription().getVersion().equals(lastVersion)) {
			Bukkit.getConsoleSender().sendMessage("§c[abiduxCore] Versão mais recente detectada, por favor, instale-a.");
		}
		Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (Scheduler scheduler : asyncSchedulers) scheduler.tick();
			}
		}, 0, 1);
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				for (Scheduler scheduler : syncSchedulers) scheduler.tick();
			}
		}, 0, 1);
	}
	
	private static String getLastVersion() throws MalformedURLException, IOException {
		try(Scanner scanner = new Scanner(new URL("https://raw.githubusercontent.com/mabidux/abiduxCore/main/src/plugin.yml").openStream())) {
			String last = "";
			while(scanner.hasNext()) {
				String current = scanner.next();
				if (last.contains("version")) return current;
				last = current;
			}
		}
		return null;
	}
	
	public static void registerSyncScheduler(Scheduler scheduler) {
		syncSchedulers.add(scheduler);
	}
	
	public static void registerAsyncScheduler(Scheduler scheduler) {
		asyncSchedulers.add(scheduler);
	}
	
}