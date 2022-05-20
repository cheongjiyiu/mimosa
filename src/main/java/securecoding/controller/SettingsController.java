package securecoding.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import securecoding.model.User;
import securecoding.model.property.Roles;
import securecoding.repository.UserRepository;
import securecoding.util.UserUtil;

@Controller
public class SettingsController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/settings/statistics", method = RequestMethod.GET)
	public String getStatistics(Model model) {
		User user = UserUtil.getCurrentUser();

		if (user.getRole().equals(Roles.STUDENT))
			model.addAttribute("position", UserUtil.rankUser(userRepository.findByRole(Roles.STUDENT), user));
		else
			model.addAttribute("position", UserUtil.rankUser(userRepository.findAll(), user));
		
		if (user.getBatch() != null)
			model.addAttribute("batchPosition", UserUtil.rankUser(userRepository.findByBatch(user.getBatch()), user));

		return "pages/settings/statistics";
	}

	@RequestMapping(value = "/settings/update", method = RequestMethod.GET)
	public String getUpdate() {
		return "pages/settings/update";
	}

	@RequestMapping(value = "/settings/update/password", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<User> getUpdatePassword(@RequestParam("oldPassword") String oldPassword,
			@RequestParam("password") String password) {
		User user = UserUtil.getCurrentUser();
		
		if (passwordEncoder.matches(oldPassword, user.getPassword()) && password.length() >= 8)
			user.setPassword(passwordEncoder.encode(password));
		else
			return ResponseEntity.badRequest().body(null);

		return ResponseEntity.ok().body(userRepository.saveAndFlush(user));
	}

}
