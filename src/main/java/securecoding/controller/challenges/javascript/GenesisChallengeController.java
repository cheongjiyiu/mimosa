package securecoding.controller.challenges.javascript;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.ixora.PageReader;

@ChallengeController("/challenges/genesis")
public class GenesisChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Prepare reading of scriptlets
		PageReader pageReader = new PageReader("templates/pages/challenges/genesis.html");
		
		try {
			JsonObject json = JsonParser.parseReader(request.getReader()).getAsJsonObject();
			String key = json.get("key").getAsString();

			if ("start".equals(key)) {
				attempt.getData().put("code", "level-0");
				attempt.getData().put("raw", cleanRead(pageReader, "script-level-0"));
			} else if ("level-0".equals(key)) {
				attempt.getData().put("code", "level-1");
				attempt.getData().put("raw", cleanRead(pageReader, "script-level-1"));
			} else if ("level-1".equals(key)) {
				attempt.getData().put("code", "level-2");
				attempt.getData().put("raw", cleanRead(pageReader, "script-level-2"));
			} else if ("level-2".equals(key)) {
				attempt.getData().put("code", "level-3");
				attempt.getData().put("raw", cleanRead(pageReader, "script-level-3"));
			} else if ("level-3".equals(key)) {
				attempt.getData().put("code", "level-4");
				attempt.getData().put("raw", cleanRead(pageReader, "script-level-4"));
			} else if ("level-4".equals(key)) {
				attempt.getData().put("code", "level-5");
				attempt.getData().put("raw", cleanRead(pageReader, "script-level-5"));
			} else if ("level-5".equals(key)) {
				attempt.getData().put("code", "level-6");
				attempt.getData().put("raw", cleanRead(pageReader, "script-level-6"));
			} else if ("level-6".equals(key)) {
				attempt.getData().put("code", "level-7");
				attempt.getData().put("raw", cleanRead(pageReader, "script-level-7"));
			} else if ("level-7".equals(key)) {
				attempt.getData().put("code", "level-8");
				attempt.getData().put("raw", "");
			} else if ("level-8".equals(key)) {
				attempt.getData().put("code", "delta");
				attempt.getData().put("raw", "/* no one said my level names are numeric */");
			} else if ("delta".equals(key)) {
				attempt.getData().put("code", "zenith");
				attempt.getData().put("raw", "");
			} else if ("zenith".equals(key)) {
				attempt.getData().put("code", "wall");
				attempt.getData().put("raw", cleanRead(pageReader, "script-wall"));
			} else if ("wall".equals(key)) {
				attempt.getData().put("code", "wait-what");
				attempt.getData().put("raw", cleanRead(pageReader, "script-wait-what"));
				attempt.setPoints(10);
			} else if ("wait-what".equals(key) || "recursion".equals(key)) {
				attempt.getData().put("code", "recursion");
				attempt.getData().put("raw", cleanRead(pageReader, "script-recursion"));
			} else if ("castle".equals(key)) {
				attempt.getData().put("code", "bad-ending");
				attempt.getData().put("raw", cleanRead(pageReader, "script-castle"));
				attempt.setPoints(15);
			} else if ("bad-ending".equals(key)) {
				attempt.getData().put("code", "bad-ending");
				attempt.getData().put("raw", cleanRead(pageReader, "script-bad-ending"));
			} else if ("svg".equals(key) || "SVG".equals(key)) {
				attempt.getData().put("code", "start");
				attempt.getData().put("raw", cleanRead(pageReader, "script-svg-hint"));
				attempt.setPoints(15);
			} else if ("eternity".equals(key)) {
				attempt.getData().put("code", "eternity");
				attempt.getData().put("raw", cleanRead(pageReader, "script-eternity"));
				attempt.setPoints(25);
			} else {
				attempt.getData().put("code", "hacker");
				attempt.getData().put("raw", cleanRead(pageReader, "script-hacker"));
				attempt.setPoints(5);
			}
		} catch (NullPointerException | IllegalStateException e) {
			attempt.getData().put("code", "error");
		}
	}
	
	private String cleanRead(PageReader pageReader, String site) {
		return pageReader.getFragment(site).html().replace("\t", "").replace("\n", " ");
	}

}
