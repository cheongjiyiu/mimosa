package securecoding.controller.challenges.general;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.ixora.PageParser;
import weilianglol.ixora.PageReader;

@ChallengeController("/challenges/directory-traversal")
public class DirectoryTraversal extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String nav = request.getParameter("nav");
		
		// Append page response
		String content = fetchPage("https://dism.edu.sg", nav);
		
		attempt.getData().put("page", content);
		
		// Replace content
		String html = "<html><head></head><body><p>" + nav + "</p></body></html>";
		
		PageParser parser = new PageParser(html);
	
		parser.update();
		if (nav.equals("https://dism.edu.sg/..")||nav.equals("https://dism.edu.sg/../"))
			attempt.setPoints(5);
		if (nav.equals("https://dism.edu.sg/../..") || nav.equals("https://dism.edu.sg/../../"))
			attempt.setPoints(10);
		if (nav.equals("https://dism.edu.sg/../../quantumrealm") || nav.equals("https://dism.edu.sg/../../quantumrealm/"))
			attempt.setPoints(15);
		if (nav.equals("https://dism.edu.sg/../../quantumrealm/antman") || nav.equals("https://dism.edu.sg/../../quantumrealm/antman/"))
			attempt.setPoints(20);
	}

	private String fetchPage(String mainPath, String url) throws Exception {
		PageReader pageReader = new PageReader("templates/pages/challenges/directory-traversal.html");
		url = url == null ? "" : url.trim();

		// Bad url
		if (url.isEmpty() || !url.startsWith(mainPath) || !(new URL(url).getHost().equals(new URL(mainPath).getHost())))
			return pageReader.getFragment("bad-request").html();

		// Get subpath
		String path = new URL(url).getPath();

		if (path.equals("") || path.equals("/")) {
			return pageReader.getFragment("main-site").html();
		}
	
		if (path.equals("/..") || path.equals("/../") ) {
			return pageReader.getFragment("first").html();
		}
		
		if (path.equals("/../..") || path.equals("/../../") ) {
			return pageReader.getFragment("second").html();
		}

		if (path.equals("/../../quantumrealm")||path.equals("/../../quantumrealm/") ) {
			return pageReader.getFragment("third").html();
		}

		if (path.equals("/../../quantumrealm/antman")||path.equals("/../../quantumrealm/antman/")) {
			return pageReader.getFragment("final").html();
		}

		return pageReader.getFragment("not-found").html();
	}

}
