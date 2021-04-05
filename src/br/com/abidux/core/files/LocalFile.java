package br.com.abidux.core.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class LocalFile {
	
	public final String path, fileName;
	public LocalFile(String path, String fileName) {
		this.path = path;
		this.fileName = fileName;
	}
	
	public LocalFile(File path, String fileName) {
		this(path.getPath(), fileName);
	}
	
	protected JarFile getRunningJar() {
		try {
			return new JarFile(URLDecoder.decode(new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath()).getAbsolutePath(), "UTF-8"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected boolean extract() throws IOException {
		if (getRunningJar() == null) return false;
		File file = new File(path, fileName);
		if (file.isDirectory()) {
			file.mkdir();
			return false;
		}
		if (!file.exists()) file.getParentFile().mkdirs();
		
		JarFile jar = getRunningJar();
		Enumeration<JarEntry> entry = jar.entries();
		while (entry.hasMoreElements()) {
			JarEntry e = entry.nextElement();
			if (!e.getName().contains(fileName)) continue;
			InputStream input = new BufferedInputStream(jar.getInputStream(e));
			OutputStream output = new BufferedOutputStream(new FileOutputStream(file));
			copyInputStream(input, output);
			jar.close();
			return true;
		}
		jar.close();
		return false;
	}
	
	protected void copyInputStream(InputStream input, OutputStream output) throws IOException {
		try {
			byte[] buff = new byte[4096];
			int n;
			while ((n = input.read(buff)) > 0) {
				output.write(buff, 0, n);
			}
		} finally {
			output.flush();
			output.close();
			input.close();
		}
	}
	
	public void install() {
		try {
			File lib = new File(path, fileName);
			if (!lib.exists()) extract();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}