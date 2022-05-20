package securecoding.controller.challenges.xss;

import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.HtmlUtils;
import org.springframework.web.util.UriComponentsBuilder;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.ixora.PageParser;
import weilianglol.ixora.PageReader;

@ChallengeController("/challenges/syndica")
public class SyndicaChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String nav = request.getParameter("nav");
		nav = StringUtils.replaceIgnoreCase(nav, "alert", "[redacted]");

		// Append page response
		String content = fetchPage("https://dism.edu.sg", nav);
		attempt.getData().put("page", content);

		// Replace content
		String html = "<html><head></head><body><p>"  + checkBad("https://dism.edu.sg/search", nav) + "</p></body></html>";
		PageParser parser = new PageParser(html);
		parser.update();

		// Has an alert
		if (parser.getAlerts().size() > 0)
			attempt.addPoints(10);
		if (parser.getAlerts().contains("xss_by_sam_tan"))
			attempt.addPoints(15);
	}

	private String fetchPage(String mainPath, String url) throws Exception {
		PageReader pageReader = new PageReader("templates/pages/challenges/syndica.html");
		url = url == null ? "" : url.trim();

		// Bad url
		if (url.isEmpty() || !url.startsWith(mainPath) || !(new URL(url).getHost().equals(new URL(mainPath).getHost())))
			return pageReader.getFragment("bad-request").html();

		// Get subpath
		String path = new URL(url).getPath();
		// Get offending param
		String badParam = checkBad("https://dism.edu.sg/search", url);

		if (path.equals("") || path.equals("/"))
			return pageReader.getFragment("main-site").html();
		if (path.equals("/search") && badParam == null) {
			String query = fetchQuery("https://dism.edu.sg/search", url, "q");
			String content = pageReader.getFragment("search").html();
			// Replace page with query param
			return content.replace("{mm-replace}", query == null ? "" : HtmlUtils.htmlEscape(query));
		}
		if (path.equals("/search") && badParam != null) {
			String content = pageReader.getFragment("syndica").html();
			return content.replace("{mm-replace}", badParam);
		}

		return pageReader.getFragment("not-found").html();
	}

	private String checkBad(String mainPath, String url) throws MalformedURLException {
		url = url == null ? "" : url.trim();

		// Bad url
		if (url.isEmpty() || !url.startsWith(mainPath) || !(new URL(url).getHost().equals(new URL(mainPath).getHost()))
				|| !(new URL(url).getPath().equals(new URL(mainPath).getPath())))
			return null;
		
		MultiValueMap<String, String> params = UriComponentsBuilder.fromHttpUrl(url).build().getQueryParams();
		for (String param : params.keySet()) {
			String val = params.getFirst(param);
			if (StringUtils.containsAny(val, "<", ">", "/", "script", "alert"))
				return param;
		}

		return null;
	}

	private String fetchQuery(String mainPath, String url, String query) throws MalformedURLException {
		url = url == null ? "" : url.trim();

		// Bad url
		if (url.isEmpty() || !url.startsWith(mainPath) || !(new URL(url).getHost().equals(new URL(mainPath).getHost()))
				|| !(new URL(url).getPath().equals(new URL(mainPath).getPath())))
			return null;

		return UriComponentsBuilder.fromHttpUrl(url).build().getQueryParams().getFirst(query);
	}

}
