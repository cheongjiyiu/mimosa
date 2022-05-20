package securecoding.controller.challenges.javascript;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;

@ChallengeController("/challenges/faster-than-light")
public class FasterThanLightChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String msg = request.getParameter("msg");		
		if (msg.equals("i_weave_light"))
			attempt.addPoints(15);
	}
	
}
