package securecoding.controller.challenges.cryptography;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.mimosa.java.compiler.Compiler;

@ChallengeController("/challenges/hash-recompilation")
public class HashRecompilationChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = request.getParameter("code");
		attempt.setSubmission(code);

		try (Compiler compiler = new Compiler("MD5Exercise", code)) {
			compiler.createInstance();

			attempt.addPoints(assertPost(compiler, "password", "5f4dcc3b5aa765d61d8327deb882cf99") ? 5 : 0);
			attempt.addPoints(assertPost(compiler, "samantha", "f01e0d7992a3b7748538d02291b0beae") ? 5 : 0);
			attempt.addPoints(assertPost(compiler, "vanessa", "282bbbfb69da08d03ff4bcf34a94bc53") ? 5 : 0);
		}
	}

	private boolean assertPost(Compiler compiler, String password, String hash) throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		// Set up request password
		request.setParameter("password", password);
		// Push to doPost
		compiler.renderMethod("doPost", new Class[] { HttpServletRequest.class, HttpServletResponse.class },
				new Object[] { request, response });

		// Get results
		String output = (String) request.getAttribute("hash");
		// Compare with expected
		return output != null && output.equalsIgnoreCase(hash);
	}

}
