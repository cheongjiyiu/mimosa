package securecoding.controller.challenges.xss;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Element;
import org.springframework.web.util.UriComponentsBuilder;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import securecoding.util.PropertiesUtil;
import weilianglol.ixora.PageParser;
import weilianglol.ixora.PageReader;

@ChallengeController("/challenges/biography")
public class BiographyChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String nav = request.getParameter("nav");

		// Append page response
		String content = fetchPage("https://samantha.iscool.com", nav).html();
		attempt.getData().put("page", content);

		// Get image
		String img = fetchQuery("https://samantha.iscool.com/about", nav, "img");
		String html = "<html><head></head><body><img src=\"" + PropertiesUtil.getProperty("securecoding.app.url")
				+ "/external/pages/challenges/biography/" + img + "\" /></body></html>";
		html = StringUtils.replaceIgnoreCase(html, "script", "");
		
		PageParser parser = new PageParser(html);
		parser.update();

		// Has an alert
		if (parser.getAlerts().size() > 0)
			attempt.addPoints(10);
		if (parser.getAlerts().contains("ethan_was_here"))
			attempt.addPoints(10);
	}

	private Element fetchPage(String mainPath, String url) throws Exception {
		PageReader pageReader = new PageReader("templates/pages/challenges/biography.html");
		url = url == null ? "" : url.trim();

		// Bad url
		if (url.isEmpty() || !url.startsWith(mainPath) || !(new URL(url).getHost().equals(new URL(mainPath).getHost())))
			return pageReader.getFragment("bad-request");

		// Get subpath
		String path = new URL(url).getPath();

		if (path.equals("") || path.equals("/"))
			return pageReader.getFragment("main-site");
		if (path.equals("/achievements"))
			return pageReader.getFragment("achievements");
		if (path.equals("/about"))
			return pageReader.getFragment("about-me");

		return pageReader.getFragment("not-found");
	}

	private String fetchQuery(String mainPath, String url, String query) throws Exception {
		url = url == null ? "" : url.trim();

		// Bad url
		if (url.isEmpty() || !url.startsWith(mainPath) || !(new URL(url).getHost().equals(new URL(mainPath).getHost()))
				|| !(new URL(url).getPath().equals(new URL(mainPath).getPath())))
			return null;

		return UriComponentsBuilder.fromHttpUrl(url).build().getQueryParams().getFirst(query);
	}

}
