package org.springusage.mybatis.generator.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PropertyUtils {
	private static final Log log = LogFactory.getLog(PropertyUtils.class);
	
	private static Map<String, Properties> props;
	
	public static void init() {
		props = new ConcurrentHashMap<String, Properties>();
		log.debug("Beginning init the PropertyUtils.");
		List<String> list = null;
		try {
			list = scanClassPath();
		} catch (URISyntaxException e1) {
			throw new RuntimeException("scan the classpath appear error!");
		}
		for (String propFile : list) {
			Properties prop = new Properties();
//			String propFile = "jdbc.properties";
			InputStream in = PropertyUtils.class.getClassLoader().getResourceAsStream(propFile);
			try {
				prop.load(in);
				log.debug("load " + propFile + " complete.");
				props.put(propFile, prop);
			} catch (IOException e) {
				log.error("Error：load the " + propFile + "faild." + e.getMessage());
				e.printStackTrace();
			}
		}
		
		log.debug("Initalizied complete for PorpertyUtils.");
	}

	private static List<String> scanClassPath() throws URISyntaxException{
		List<String> list = new ArrayList<String>();
		File classPath = new File(PropertyUtils.class.getResource("/").toURI());
		File[] files = classPath.listFiles();
		if (files == null) {
			return list;
		}
		for (File f : files) {
			if (f.isDirectory()) {
				continue;
			}
			String fileName = f.getName();
			if (fileName.toLowerCase().endsWith(".properties")) {
				list.add(fileName);
			}
		}
		return list;
	}
	
	public static String getProperty(String key) {
		for (Entry<String, Properties> item : props.entrySet()) {
			Properties prop = item.getValue();
			String value = prop.getProperty(key);
			if (value != null) {
				return value;
			}
		}
		return null;
	}
	
	
	public static String getProperty(String key, String defaultValue) {
		String value = getProperty(key);
		return value == null ? defaultValue : value;
	}
	
	public static Properties getProperties(String propName) {
		String suffix = ".properties";
		String keyName = propName.toLowerCase();
		keyName = keyName.endsWith(suffix) ? keyName : keyName + suffix;
		return props.get(keyName);
	}
	
	public static void main(String[] args) {
		PropertyUtils.init();
		
		String test = PropertyUtils.getProperty("runtime.log", "null");
		System.out.println(test);
	}
	
}
