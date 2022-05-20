package securecoding.controller.challenges.validation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;

@ChallengeController("/challenges/into-the-shadow")
public class IntoTheShadowChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		if ("shadow".equals(username))
			attempt.addPoints(5);
		if ("erihxpkw".equals(password))
			attempt.addPoints(20);
	}
	
}
