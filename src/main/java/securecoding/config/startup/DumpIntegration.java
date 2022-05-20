package securecoding.config.startup;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class DumpIntegration {

	public static void dumpSQL() throws IOException {
		Resource resource = new ClassPathResource("mimosa-dump.sql");
		File sqlDump = new File("./mimosa-dump.sql");

		FileUtils.copyInputStreamToFile(resource.getInputStream(), sqlDump);
	}
	
	public static void dumpProperties() throws IOException {
		Resource resource = new ClassPathResource("application.properties");
		File dump = new File("./application.properties");

		FileUtils.copyInputStreamToFile(resource.getInputStream(), dump);
	}
	
}
