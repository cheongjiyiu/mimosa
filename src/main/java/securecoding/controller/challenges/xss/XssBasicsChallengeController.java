package securecoding.controller.challenges.xss;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.ixora.PageParser;

@ChallengeController("/challenges/xss-basics")
public class XssBasicsChallengeController extends ChallengeControllerAdapter {
	
	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) {
		String msg = request.getParameter("msg");
		String html = "<html><head></head><body><div id='myDiv'>" + msg + "</div></body></html>";

		PageParser parser = new PageParser(html);
		
		// Has an alert
		if (parser.getAlerts().size() > 0)
			attempt.addPoints(5);
		if (parser.getAlerts().contains("helloworld"))
			attempt.addPoints(10);
	}

}
