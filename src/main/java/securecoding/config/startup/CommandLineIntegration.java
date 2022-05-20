package securecoding.config.startup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import lombok.Getter;
import securecoding.util.PropertiesUtil;
import weilianglol.mimosa.node.filemanager.NodePackager;

public class CommandLineIntegration {

	private static CommandLineIntegration instance;

	private @Getter CommandLineParser parser;
	private @Getter Options options;
	private HelpFormatter formatter;

	private CommandLineIntegration() {
		parser = new DefaultParser();

		options = new Options();
		options.addOption("h", "help", false, "prints usage information");
		options.addOption("v", "version", false, "prints application version");
		options.addOption("g", "generate", true, "generates either node, sql or properties");
		options.getOption("g").setArgs(Option.UNLIMITED_VALUES);
		options.addOption("s", "service", false, "links mimosa as a windows service");

		formatter = new HelpFormatter();
	}

	private String[] clean(String[] args) {
		List<String> validArgs = new ArrayList<>();

		for (int i = 0; i < args.length; i++) {
	        if (options.hasOption(args[i])) {
	            validArgs.add(args[i]);
	            if (i + 1 < args.length && options.getOption(args[i]).hasArg())
	                validArgs.add(args[i + 1]);
	        }
	    }

		return validArgs.toArray(new String[0]);
	}

	private void process(String[] args) throws InterruptedException, IOException {
		try {
			CommandLine line = parser.parse(options, clean(args));

			if (line.hasOption("h")) {
				formatter.printHelp("Usage: mimosa [OPTIONS]", options);
				System.exit(0);
			} else if (line.hasOption("v")) {
				String name = PropertiesUtil.getProperty("securecoding.app.name");
				String version = PropertiesUtil.getProperty("securecoding.app.version");

				System.out.printf("%s v%s\n", name, version);
				System.exit(0);
			} else if (line.hasOption("g")) {
				String[] generationContent = line.getOptionValues("generate");

				for (String content : generationContent) {
					if ("node".equals(content))
						NodePackager.initPackage();
					if ("sql".equals(content))
						DumpIntegration.dumpSQL();
					if ("properties".equals(content))
						DumpIntegration.dumpProperties();
				}
				System.out.println("Package(s) created!");
				System.exit(0);
			} else if (line.hasOption("s")) {
				ServiceIntegration.initiateLink();
				System.exit(0);
			}
		} catch (ParseException e) {
			formatter.printHelp("Usage: mimosa [OPTIONS]", options);
			System.exit(1);
		}
	}

	public static void defaults(String[] args) throws InterruptedException, IOException {
		if (instance == null) {
			instance = new CommandLineIntegration();
			instance.process(args);
		}
	}

}
