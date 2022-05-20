package securecoding.controller.challenges.xss;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.ixora.PageParser;

@ChallengeController("/challenges/christmas-workshop")
public class ChristmasWorkshopChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) {
		String msg = request.getParameter("msg");
		String html = "<html><head></head><body><div>" + msg + "</div></body></html>";

		PageParser parser = new PageParser(html);
		List<HtmlAnchor> anchors = parser.getPage().getAnchors();

		if (anchors.size() > 0)
			attempt.addPoints(5);
		if (anchors.stream()
				.anyMatch(x -> "https://google.com".equals(x.getHrefAttribute())))
			attempt.addPoints(10);
	}

}
