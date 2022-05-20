package securecoding.util;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import securecoding.model.User;
import securecoding.repository.UserRepository;

@Component
public class UserUtil {

	private static UserRepository userRepository;
	private static SessionRegistry sessionRegistry;

	@Autowired
	private UserUtil(UserRepository userRepository, SessionRegistry sessionRegistry) {
		UserUtil.userRepository = userRepository;
		UserUtil.sessionRegistry = sessionRegistry;
	}

	public static User getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken)
			return null;

		return userRepository.findByUsername(authentication.getName());
	}

	public static List<User> getLoggedInUsers() {
		return sessionRegistry.getAllPrincipals().stream()
				.filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
				.map(p -> userRepository.findByUsername(((UserDetails) p).getUsername())).collect(Collectors.toList());
	}
	
	public static int rankUser(List<User> users, User user) {
		users.sort((a, b) -> b.getPoints() - a.getPoints());
		
		if (!users.contains(user))
			return -1;
		
		int rank = 1;
		for (int i = 1; i < users.size(); i++)
		    if (users.get(i).getPoints() != users.get(i - 1).getPoints())
		        rank++;
		return rank;
	}

}
