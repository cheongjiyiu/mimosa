package securecoding.config.services;

import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import securecoding.model.User;
import securecoding.repository.UserRepository;
import securecoding.util.MailUtil;
import securecoding.util.PropertiesUtil;
import securecoding.util.UserUtil;

@Component
public class PostAuthenticationHandler extends SimpleUrlAuthenticationSuccessHandler {

	@Autowired
	private UserRepository userRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		super.onAuthenticationSuccess(request, response, authentication);

		User user = UserUtil.getCurrentUser();
		user.setLastLogin(new Date());
		userRepository.saveAndFlush(user);

		if (PropertiesUtil.getProperty("securecoding.mail.prompt-login").equals("true"))
			MailUtil.sendMail(user, "Login", "You have logged in on " + new Date() + " as " + user.getUsername() + ".");
	}

}
