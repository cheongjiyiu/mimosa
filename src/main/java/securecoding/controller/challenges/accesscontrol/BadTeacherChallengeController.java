package securecoding.controller.challenges.accesscontrol;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.ixora.PageReader;

@ChallengeController("/challenges/bad-teacher")
public class BadTeacherChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		
		if ("p5678901".equals(username))
			attempt.addPoints(15);
	}

	@RequestMapping(path = "/login", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<String> login(@RequestParam("username") String username,
			@RequestParam("password") String password) throws Exception {
		// Retrieve post login page
		PageReader pageReader = new PageReader("templates/pages/challenges/bad-teacher.html");
		String html = pageReader.getFragment("bad-teacher").html();
		
		if ("s12345".equals(username) && "password".equals(password))
			return ResponseEntity.ok().body(html);
		else
			return ResponseEntity.badRequest().body(null);
	}

}
