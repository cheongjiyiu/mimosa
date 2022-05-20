package securecoding.controller.template;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Retention(RetentionPolicy.RUNTIME)
@Controller
@RequestMapping
public @interface ChallengeController {
	
	@AliasFor(attribute = "path", annotation = RequestMapping.class)
	String value();
	
	boolean overrideSubmission() default false;
	
}
