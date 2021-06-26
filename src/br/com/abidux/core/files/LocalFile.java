package br.com.abidux.core.files;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.jar.JarFile;

import br.com.abidux.core.utils.JarUtils;

public class LocalFile {
	
	protected final String path, fileName;
	private File file;
	public LocalFile(String path, String fileName) {
		this.path = path;
		this.fileName = fileName;
	}
	
	public LocalFile(File path, String fileName) {
		this(path.getPath(), fileName);
	}
	
	protected boolean extract() throws FileNotFoundException, IOException {
		JarFile jar = JarUtils.getRunningJar();
		if (jar == null) return false;
		File file = new File(path, fileName);
		if (file.isDirectory()) {
			file.mkdir();
			return false;
		}
		if (!file.exists()) file.getParentFile().mkdirs();
		
		JarUtils.queueEntries(jar, e -> {
			if (!e.getName().contains(fileName)) return;
			try {
				copyFile(new BufferedInputStream(jar.getInputStream(e)), new BufferedOutputStream(new FileOutputStream(file)));
			} catch (IOException ex) {}
		});
		jar.close();
		return false;
	}
	
	private void copyFile(InputStream input, OutputStream output) throws IOException {
		try {
			byte[] buff = new byte[4096];
			int n;
			while ((n = input.read(buff)) > 0)
				output.write(buff, 0, n);
		} finally {
			output.flush();
			output.close();
			input.close();
		}
	}
	
	public boolean install() {
		file = new File(path, fileName);
		if (!file.exists()) {
			try {
				return extract();
			} catch (IOException e) {}
		}
		return false;
	}
	
	public File getFile() {
		return file;
	}
	
}