package securecoding.util;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

public class PropertiesUtil {

	public static Properties getProperties() {
		try {
			Resource external = new FileSystemResource("application.properties");
			if (external.exists())
				return PropertiesLoaderUtils.loadProperties(external);
			
			return PropertiesLoaderUtils.loadAllProperties("application.properties");
		} catch (IOException e) {
			throw new RuntimeException("Could not load properties.", e);
		}
	}
	
	public static String getProperty(String name) {
		return PropertiesUtil.getProperties().getProperty(name);
	}
	
}
