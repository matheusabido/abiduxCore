package br.com.abidux.core.config;

import java.io.File;

public class ConfigUtils {
	
	public static Config getConfig(File path, String fileName) {
		return new Config(path, fileName);
	}
	
}