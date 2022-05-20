package securecoding;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import securecoding.config.startup.CommandLineIntegration;

@SpringBootApplication
// Required in order to jar with dependencies
@PropertySources({ @PropertySource("classpath:application.properties"),
		@PropertySource(value = "file:application.properties", ignoreResourceNotFound = true) })
public class Application {

	public static void main(String[] args) throws Exception {
		CommandLineIntegration.defaults(args);
		SpringApplication.run(Application.class, args);
	}

}
