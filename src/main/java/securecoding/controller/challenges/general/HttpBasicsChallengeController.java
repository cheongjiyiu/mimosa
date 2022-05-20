package securecoding.controller.challenges.general;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;

@ChallengeController("/challenges/http-basics")
public class HttpBasicsChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String msg = request.getParameter("msg");
		attempt.setPoints(msg == null ? 0 : msg.equals("helloworld") ? 15 : 5);
	}
	
}
