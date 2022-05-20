package securecoding.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import securecoding.model.User;
import securecoding.repository.UserRepository;

@Component
public class MailUtil {

	private static JavaMailSender javaMailSender;
	private static UserRepository userRepository;

	@Autowired
	private MailUtil(UserRepository userRepository, JavaMailSender javaMailSender) {
		MailUtil.userRepository = userRepository;
		MailUtil.javaMailSender = javaMailSender;
	}

	public static void sendMail(User user, String subject, String content) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(PropertiesUtil.getProperty("securecoding.mail.bot.name"));
		message.setTo(user.getEmail());
		message.setSubject("[" + PropertiesUtil.getProperty("securecoding.app.name") + "] " + subject);
		message.setText(content);

		new Thread(() -> javaMailSender.send(message)).start();
	}
	
	public static void sendAllMail(String subject, String content) {
		userRepository.findByEnabledTrue().forEach(user -> sendMail(user, subject, content));
	}

}
