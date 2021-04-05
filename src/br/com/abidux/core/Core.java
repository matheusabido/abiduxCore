package br.com.abidux.core;

import java.util.HashSet;
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
	
	public static void registerSyncScheduler(Scheduler scheduler) {
		syncSchedulers.add(scheduler);
	}
	
	public static void registerAsyncScheduler(Scheduler scheduler) {
		asyncSchedulers.add(scheduler);
	}
	
}