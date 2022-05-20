package securecoding.config.startup;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import securecoding.util.PropertiesUtil;
import weilianglol.mimosa.node.filemanager.NodePackager;

@Component
public class NodeIntegration {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@PostConstruct
	public void hookNodeJS() throws IOException, InterruptedException {
		if (PropertiesUtil.getProperty("securecoding.compiler.node.generate-packages").equals("true")) {
			NodePackager.initializationCheck();
			logger.info("Compiler: NodeJS packages imported");
		}
	}

}
