package securecoding.controller.challenges.general;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;

@ChallengeController("/challenges/nuclear-winter")
public class NuclearWinterChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String code = Optional.ofNullable(request.getParameter("code")).orElse("");
		
		String password = "m1m0s4";
		String junk = "@4l\n # code : " + password;
		String reply = "Incorrect passphrase!";

		// Correct password
		if (code.equals(password)) {
			attempt.setPoints(15);
			reply = "Atomic bomb MK12-11 released.";
		} else if (code.length() > 6) {
			attempt.addPoints(5);
			
			// Iterate through the junk per excess character
			for (int i = 0; i < code.substring(6).length() 
				&& i < junk.length(); i++)
				reply += junk.charAt(i);
		}
		
		attempt.getData().put("reply", reply);
	}

}
