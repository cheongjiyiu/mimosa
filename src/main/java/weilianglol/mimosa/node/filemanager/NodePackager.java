package weilianglol.mimosa.node.filemanager;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class NodePackager {

	public static void initPackage() throws IOException {
		Resource resource = new ClassPathResource("node/package.json");
		File nodePackage = new File(".mimosa/node/package.json");

		FileUtils.copyInputStreamToFile(resource.getInputStream(), nodePackage);
	}

	public static void pullDependencies() throws IOException, InterruptedException {
		boolean windows = System.getProperty("os.name").toLowerCase().contains("win");

		String command = windows ? "npm.cmd install .mimosa/node --prefix .mimosa/node/node_modules"
				: "npm install .mimosa/node --prefix .mimosa/node";

		Process process = Runtime.getRuntime().exec(command);
		process.waitFor(60, TimeUnit.SECONDS);

		if (process.exitValue() != 0)
			throw new RuntimeException("npm pull failed!");
	}

	public static void initializationCheck() throws IOException, InterruptedException {
		File nodePackage = new File(".mimosa/node/node_modules");

		if (nodePackage.exists())
			return;

		NodePackager.initPackage();
		NodePackager.pullDependencies();
	}

}
