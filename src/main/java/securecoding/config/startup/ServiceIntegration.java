package securecoding.config.startup;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class ServiceIntegration {
	
	private static final Logger logger = LoggerFactory.getLogger(ServiceIntegration.class);
	
	public static File prepareWinsw() throws IOException {
		Resource winsw = new ClassPathResource("winsw/mimosa-winsw.exe");
		File winswFile = new File(".mimosa/winsw/mimosa-winsw.exe");

		if (winswFile.exists())
			winswFile.delete();
		
		FileUtils.copyInputStreamToFile(winsw.getInputStream(), winswFile);
		return winswFile;
	}
	
	public static File prepareXml() throws IOException {
		Resource xml = new ClassPathResource("winsw/mimosa-winsw.xml");
		File xmlFile = new File(".mimosa/winsw/mimosa-winsw.xml");
		
		if (xmlFile.exists())
			xmlFile.delete();
		
		String workingDir = ServiceIntegration.getWorkingDirectory();
		String jarName = ServiceIntegration.getJarName();
		String content = IOUtils.toString(xml.getInputStream(), StandardCharsets.UTF_8);
		
		content = content.replace("{mms-wd}", workingDir);
		content = content.replace("{mms-jar}", jarName);
		FileUtils.writeStringToFile(xmlFile, content, StandardCharsets.UTF_8);
		return xmlFile;
	}
	
	private static String getWorkingDirectory() {
		String jarPath = ServiceIntegration.getJarName();
		return Paths.get(jarPath).getParent().toString();
	}
	
	private static String getJarName() {
		String dirtyPath = ServiceIntegration.class.getResource("").toString();
	    String jarPath = dirtyPath.replaceAll("^.*file:/", "");
	    jarPath = jarPath.replaceAll("jar!.*", "jar");
	    jarPath = jarPath.replaceAll("%20", " ");
	    if (!jarPath.endsWith(".jar")) 
	    	jarPath = jarPath.replaceAll("/classes/.*", "/classes/");
	    return jarPath;
	}
	
	public static void initiateLink() throws IOException, InterruptedException {
		boolean windows = System.getProperty("os.name").toLowerCase().contains("win");
		if (!windows)
			throw new UnsupportedOperationException("Not a windows machine!");
		
		logger.info("Loading winsw and xml...");
		ServiceIntegration.prepareWinsw();
		ServiceIntegration.prepareXml();

		logger.info("Preparing winsw.exe installation...");
		String command = ".mimosa\\winsw\\mimosa-winsw.exe install";
		Process process = Runtime.getRuntime().exec(command);
		process.waitFor(30, TimeUnit.SECONDS);

		logger.info("Starting mimosa service...");
		command = ".mimosa\\winsw\\mimosa-winsw.exe start";
		process = Runtime.getRuntime().exec(command);
		process.waitFor(30, TimeUnit.SECONDS);

		logger.info("Startup complete.");
	}

}
