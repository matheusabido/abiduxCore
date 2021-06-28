package br.com.abidux.core.utils;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.function.Consumer;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtils {
	
	public static JarFile getRunningJar(Class<?> cls) {
		try {
			return new JarFile(URLDecoder.decode(new File(cls.getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath(), "UTF-8"));
		} catch (IOException e) {}
		return null;
	}
	
	public static boolean queueEntries(Class<?> cls, Consumer<JarEntry> entry) {
		JarFile jar = JarUtils.getRunningJar(cls);
		if (jar == null) return false;
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) entry.accept(entries.nextElement());
		try {
			jar.close();
		} catch (IOException e) {return false;}
		return true;
	}
	
	public static void queueEntries(JarFile jar, Consumer<JarEntry> entry) {
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) entry.accept(entries.nextElement());
	}
	
}