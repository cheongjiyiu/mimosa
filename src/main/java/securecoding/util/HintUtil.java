package securecoding.util;

import java.util.List;
import java.util.stream.Collectors;

import securecoding.model.Challenge;
import securecoding.model.Hint;
import securecoding.model.User;

public class HintUtil {

	public static List<Hint> getUnlockedHints(Challenge challenge, User user) {
		return challenge.getHints().stream().filter(hint -> user.getHints().contains(hint))
				.collect(Collectors.toList());
	}
	
	public static List<Hint> getLockedHints(Challenge challenge, User user) {
		return challenge.getHints().stream().filter(hint -> !user.getHints().contains(hint))
				.collect(Collectors.toList());
	}

	public static Hint getNextLockedHint(Challenge challenge, User user) {
		return HintUtil.getLockedHints(challenge, user).stream().sorted((h1, h2) -> h1.getPriority() - h2.getPriority())
				.findFirst().orElse(null);
	}

}
