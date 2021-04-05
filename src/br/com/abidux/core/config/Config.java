package br.com.abidux.core.config;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;

import br.com.abidux.core.files.LocalFile;

public class Config {
	
	private YamlConfiguration config;
	private File path;
	private String fileName;
	public Config(File path, String fileName) {
		this.path = path;
		this.fileName = fileName;
		File file = new File(path, fileName);
		if (!file.exists()) {
			LocalFile lf = new LocalFile(path, fileName);
			lf.install();
		}
		this.config = YamlConfiguration.loadConfiguration(file);
	}
	
	public YamlConfiguration getConfig() {
		return config;
	}
	
	public void reload() {
		this.config = YamlConfiguration.loadConfiguration(new File(path, fileName));
	}
	
}