package securecoding.controller.challenges.cryptography;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;


@ChallengeController("/challenges/url-encoding")
public class URLencoding extends ChallengeControllerAdapter {
	
	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String ans = request.getParameter("ans");
		attempt.setPoints(ans.contains("homer") ? 25 : 0);

	}


}
