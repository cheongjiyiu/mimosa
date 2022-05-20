package securecoding.controller.challenges.session;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.ui.Model;
import org.springframework.util.Base64Utils;
import org.springframework.web.util.WebUtils;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import securecoding.util.UserUtil;

@ChallengeController("/challenges/locked-out")
public class LockedOutController extends ChallengeControllerAdapter {

	@Override
	public String doGet(HttpServletRequest request, HttpServletResponse response, Model model) {
		// Append cookies
		String username = UserUtil.getCurrentUser().getUsername();
		Cookie cookie = new Cookie("mms-username", Base64Utils.encodeToString(username.getBytes()));
		response.addCookie(cookie);

		return super.doGet(request, response, model);
	}

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Cookie cookie = WebUtils.getCookie(request, "mms-username");
		String reply = "Something went wrong!";

		// Invalid username
		if (cookie != null) {
			attempt.addPoints(5);
			reply = "You are not a lecturer!";
		}
		// Logged in
		if (cookie != null && cookie.getValue().equals("d2VpbGlhbmc=")) {
			attempt.addPoints(10);
			reply = "Code: 104062";
		}
		
		attempt.getData().put("reply", reply);
	}

}
