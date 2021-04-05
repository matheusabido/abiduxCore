package br.com.abidux.core.files;

import java.io.File;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class LocalLib extends LocalFile {

	public LocalLib(File path, String fileName) {
		this(path.getPath(), fileName);
	}
	
	public LocalLib(String path, String fileName) {
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
	public void install() {
		try {
			File lib = new File(path, fileName);
			if (!lib.exists()) extract();
			addClassPath(getJarURL(lib));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}