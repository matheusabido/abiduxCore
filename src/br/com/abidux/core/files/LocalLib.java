package br.com.abidux.core.files;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class LocalLib extends LocalFile {

	public LocalLib(String path, String fileName) {
		super(path, fileName);
	}
	
	public LocalLib(File path, String fileName) {
		super(path, fileName);
	}
	
	private static URL getJarURL(File file) throws MalformedURLException {
		return new URL("jar:" + file.toURI().toURL().toExternalForm() + "!/");
	}
	
	private static void addClassPath(URL url) {
		URLClassLoader uc = (URLClassLoader) ClassLoader.getSystemClassLoader();
		try {
			Method method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] {URL.class});
			method.setAccessible(true);
			method.invoke(uc, new Object[] {url});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public boolean install() {
		try {
			File lib = new File(path, fileName);
			if (!lib.exists()) {
				boolean extracted = extract();
				if (extracted) addClassPath(getJarURL(lib));
				return extracted;
			}
		} catch (Exception e) {}
		return false;
	}
	
}