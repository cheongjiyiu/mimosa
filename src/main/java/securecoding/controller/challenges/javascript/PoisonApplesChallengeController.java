package securecoding.controller.challenges.javascript;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;

@ChallengeController("/challenges/poison-apples")
public class PoisonApplesChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// Award for reaching post
		attempt.addPoints(10);
		
		try {
			JsonObject json = JsonParser.parseReader(request.getReader()).getAsJsonObject();
			
			int highscore = json.get("highscore").getAsInt();
			if (highscore >= 500)
				attempt.addPoints(15);
		} catch (NullPointerException | IllegalStateException e) {
			// Fall through
		}
	}
	
}
