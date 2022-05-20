package securecoding.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import securecoding.model.Batch;
import securecoding.model.User;
import securecoding.model.property.Roles;
import securecoding.repository.BatchRepository;
import securecoding.repository.UserRepository;
import securecoding.util.MailUtil;
import securecoding.util.PropertiesUtil;

@Controller
public class AccountController {

	@Autowired
	private BatchRepository batchRepository;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(path = "/register", method = RequestMethod.GET)
	public String getRegister(Model model) {
		model.addAttribute("batches", batchRepository.findAll());
		return "pages/account/register";
	}

	@RequestMapping(path = "/resetPassword", method = RequestMethod.GET)
	public String getResetPassword() {
		return "pages/account/resetpw";
	}

	@RequestMapping(path = "/newPassword", method = RequestMethod.GET)
	public String getResetPassword(Model model) {
		if (model.getAttribute("uuid") == null)
			return "redirect:/login";

		return "pages/account/newpw";
	}

	@RequestMapping(path = "/register", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<User> postRegister(@RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("adminNo") String adminNo,
			@RequestParam("email") String email, @RequestParam("firstName") String firstName,
			@RequestParam("lastName") String lastName, @RequestParam("batchId") int batchId) {
		Batch batch = batchRepository.findById(batchId).orElse(null);

		// Validate username longer than 3 characters, with sizable password length
		if (username == null || username.length() < 3 || password == null || password.length() < 8
				|| userRepository.findByUsername(username) != null || batch == null || !batch.isEnabled()
				|| !batch.isOpen())
			return ResponseEntity.badRequest().body(null);

		// Create and send email
		User user = new User(username, passwordEncoder.encode(password), firstName, lastName, email, adminNo, batch);
		// Demo all admins
		user.setRole(Roles.ADMIN);
		String mailContent = "Your account '" + username + "' has been successfully created. ";
		// Check for email verification
		if (PropertiesUtil.getProperty("securecoding.mail.require-verify").equals("true")) {
			user.setUuid(UUID.randomUUID().toString());
			mailContent += "Click on the following link within 24 hours to complete registration.\n"
					+ PropertiesUtil.getProperty("securecoding.app.url") + "/activate/" + user.getUuid();
		} else {
			user.setVerified(true);
			mailContent += "Click on the following link to log in now!\n"
					+ PropertiesUtil.getProperty("securecoding.app.url") + "/login";
		}

		userRepository.saveAndFlush(user);
		MailUtil.sendMail(user, "Account Registered", mailContent);
		return ResponseEntity.ok().body(user);
	}

	@RequestMapping(value = "/activate/{uuid}", method = RequestMethod.GET)
	public String getActivate(RedirectAttributes redir, @PathVariable String uuid) {
		User user = userRepository.findByUuid(uuid);

		if (user == null) {
			redir.addFlashAttribute("accStatus", "not-found");
		} else if (!user.isVerified()) {
			user.setVerified(true);
			// Clear uuid
			user.setUuid(null);
			userRepository.saveAndFlush(user);

			redir.addFlashAttribute("accStatus", "activated");
		}

		return "redirect:/login";
	}

	@RequestMapping(path = "/resetPassword", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<User> postResetPassword(@RequestParam("email") String email) {
		User user = userRepository.findByEmail(email);

		if (user == null || !user.isVerified())
			return ResponseEntity.badRequest().body(new User());

		user.setUuid(UUID.randomUUID().toString());
		MailUtil.sendMail(user, "Reset Password",
				"You have requested to reset your password. "
						+ "Click on the following link within 24 hours to reset it.\n"
						+ PropertiesUtil.getProperty("securecoding.app.url") + "/resetPassword/" + user.getUuid()
						+ "\n\nIf you did not request for a password reset, please contact your administrator.");
		userRepository.saveAndFlush(user);
		
		User resp = new User();
		resp.setVerified(user.isVerified());
		resp.setEmail(user.getEmail());
		return ResponseEntity.ok().body(resp);
	}

	@RequestMapping(value = "/resetPassword/{uuid}", method = RequestMethod.GET)
	public String getResetPassword(RedirectAttributes redir, Model model, @PathVariable String uuid) {
		User user = userRepository.findByUuid(uuid);

		if (user == null || !user.isVerified()) {
			redir.addFlashAttribute("accStatus", "not-found");
			return "redirect:/login";
		}

		redir.addFlashAttribute("uuid", uuid);
		return "redirect:/newPassword";
	}

	@RequestMapping(value = "/newPassword", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<User> postNewPassword(@RequestParam("uuid") String uuid,
			@RequestParam("password") String password) {
		User user = userRepository.findByUuid(uuid);

		if (user == null || !user.isVerified())
			return ResponseEntity.badRequest().body(user);

		user.setPassword(passwordEncoder.encode(password));
		// Clear uuid
		user.setUuid(null);
		return ResponseEntity.ok().body(userRepository.saveAndFlush(user));
	}

}
