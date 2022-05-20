package securecoding.controller.challenges.sqli;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.acacia.database.DatabaseManager;
import weilianglol.acacia.database.Populator;
import weilianglol.mimosa.java.compiler.Compiler;

@ChallengeController("/challenges/sqli-recompilation")
public class SqliRecompilationChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = request.getParameter("code");
		attempt.setSubmission(code);

		// Populate entries
		try (DatabaseManager db = new DatabaseManager(attempt.getUser().getUsername())) {
			Populator.populateUsers(db);

			try (Compiler compiler = new Compiler("EmployeeController", code)) {
				compiler.createInstance();

				// Check if original code was broken
				attempt.addPoints(assertPost(attempt, compiler, "lily", "flower", "Lily", "Swein") ? 5 : 0);
				attempt.addPoints(assertPost(attempt, compiler, "albert", "crystal", "Albert", "Tay") ? 5 : 0);
				attempt.addPoints(assertPost(attempt, compiler, "hacker", "12345678", "Unknown", "The One") ? 5 : 0);
				// Attack
				attempt.addPoints(assertPost(attempt, compiler, "' or '1'= '1' -- ", "", null, null) ? 5 : -5);
				attempt.addPoints(assertPost(attempt, compiler, "", "' or '1'= '1' -- ", null, null) ? 5 : -5);
			}
		}
	}

	private boolean assertPost(Attempt attempt, Compiler compiler, String username, String password, String firstname,
			String lastname) throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		MockHttpServletResponse response = new MockHttpServletResponse();

		request.setParameter("db_name", attempt.getUser().getUsername());
		// Set up request username and password
		request.setParameter("username", username);
		request.setParameter("password", password);
		// Push to doPost
		compiler.renderMethod("doPost", new Class[] { HttpServletRequest.class, HttpServletResponse.class },
				new Object[] { request, response });

		// Get results
		String fn = (String) request.getAttribute("firstname");
		String ln = (String) request.getAttribute("lastname");
		// Compare with expected
		return (firstname == fn || (firstname != null && firstname.equals(fn)))
				&& (lastname == ln || (lastname != null && lastname.equals(ln)));
	}

}
