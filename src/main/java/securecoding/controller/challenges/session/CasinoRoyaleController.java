package securecoding.controller.challenges.session;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.ixora.PageReader;

@ChallengeController("/challenges/casino-royale")
public class CasinoRoyaleController extends ChallengeControllerAdapter {

	@RequestMapping(path = "/view", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<String> getPage(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PageReader pageReader = new PageReader("templates/pages/challenges/casino-royale.html");

		String url = request.getParameter("nav").trim();
		String path = new URL(url).getPath();

		// Landing site
		String landingSite = "https://task.mi6.com";
		if (url.startsWith(landingSite) && new URL(url).getHost().equals(new URL(landingSite).getHost())) {
			// Agent check
			String agent = HtmlUtils.htmlEscape(request.getHeader("User-Agent"));
			
			if (!"double07".equals(agent)) {
				String content = pageReader.getFragment("main-landing").html();
				return ResponseEntity.ok().body(content.replace("{mm-agent}", agent));
			} else if ("".equals(path) || "/".equals(path)) {
				String content = pageReader.getFragment("main-decrypted").html();
				return ResponseEntity.ok().body(content);
			}

			String content = pageReader.getFragment("not-found-mi6").html();
			return ResponseEntity.ok().body(content);
		}

		// Landing site
		String casinoSite = "https://royale.casino.ca";
		if (url.startsWith(casinoSite) && new URL(url).getHost().equals(new URL(casinoSite).getHost())) {
			String token = createToken();
			String role = verifyToken(request);

			if ("".equals(path) || "/".equals(path)) {
				String content = pageReader.getFragment("casino-landing").html();
				content = content.replace("{mm-jwt-token}", token);
				return ResponseEntity.ok().body(content);
			} else if ("/feedback".equals(path)) {
				String content = pageReader.getFragment("casino-feedback").html();
				return ResponseEntity.ok().body(content);
			} else if ("/admin".equals(path) && "admin".equals(role)) {
				String content = pageReader.getFragment("casino-admin").html();
				return ResponseEntity.ok().body(content);
			} else if ("/admin".equals(path)) {
				String content = pageReader.getFragment("casino-fail").html();
				content = content.replace("{mm-role}", role);
				return ResponseEntity.ok().body(content);
			}

			String content = pageReader.getFragment("not-found-casino").html();
			return ResponseEntity.ok().body(content);
		}

		return ResponseEntity.ok().body(pageReader.getFragment("bad-request").html());
	}

	private String createToken() {
		return JWT.create().withClaim("role", "guest").sign(Algorithm.HMAC256("weiliang_s3cr3t"));
	}

	private String verifyToken(HttpServletRequest request) {
		try {
			String token = request.getHeader("Authorization");
			if (token.startsWith("Bearer ")) {
				token = token.replaceFirst("Bearer ", "");

				String algo = JWT.decode(token).getAlgorithm();
				DecodedJWT decoded = "none".equals(algo)
						? JWT.require(Algorithm.none()).build().verify(token)
						: JWT.require(Algorithm.HMAC256("weiliang_s3cr3t")).build().verify(token);
	
				return decoded.getClaim("role").asString();
			}
		} catch (NullPointerException | IllegalArgumentException | JWTVerificationException e) {
			// Fall through
		}

		return "::error_deciphering::";
	}

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String role = verifyToken(request);

		if ("admin".equals(role))
			attempt.setPoints(25);
	}

}
