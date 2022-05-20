package securecoding.controller.challenges.general;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.mimosa.java.compiler.Compiler;

@ChallengeController("/challenges/compiler-basics")
public class CompilerBasicsController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = request.getParameter("code");
		attempt.setSubmission(code);
		
		try (Compiler compiler = new Compiler("HelloWorld", code)) {
			compiler.createInstance();
			
			// Prepare listening
			compiler.clearSystemOutput();
			compiler.renderMethod("main", new Class[] { String[].class }, new Object[] { new String[] {} });
			
			// Collect all sysout
			List<String> output = compiler.getSystemOutput();
			if (!output.isEmpty())
				attempt.addPoints(5);
			if (output.contains("helloworld"))
				attempt.addPoints(10);
		}
	}

}
