package securecoding.util;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import securecoding.model.Attempt;
import securecoding.model.Challenge;
import securecoding.model.User;
import securecoding.model.property.Statuses;
import securecoding.repository.AttemptRepository;

@Component
public class AttemptUtil {

	private static AttemptRepository attemptRepository;

	@Autowired
	private AttemptUtil(AttemptRepository attemptRepository) {
		AttemptUtil.attemptRepository = attemptRepository;
	}

	public static void updateAttempt(Attempt attempt, boolean force) {
		// Update attempt
		AttemptUtil.suppressPoints(attempt);
		AttemptUtil.fillStatus(attempt);

		// Check database for records
		Attempt oAttempt = attemptRepository.findByUserAndChallenge(attempt.getUser(), attempt.getChallenge());
		if (oAttempt == null) {
			// Save first attempt
			attemptRepository.saveAndFlush(attempt);
		} else if (attempt.getPoints() >= oAttempt.getPoints()) {
			// Remove outdated attempt
			attemptRepository.delete(oAttempt);
			attemptRepository.saveAndFlush(attempt);
		} else if (force) {
			// Force submission override
			oAttempt.setSubmission(attempt.getSubmission());
			attemptRepository.saveAndFlush(oAttempt);
		}
	}

	public static Attempt findAttempt(User user, Challenge challenge) {
		return attemptRepository.findByUserAndChallenge(user, challenge);
	}

	public static Double passRate(Set<Challenge> assignments) {
		double total = 0;
		double passes = 0;

		for (Challenge assignment : assignments) {
			for (Attempt attempt : assignment.getAttempts()) {
				total++;
				if (attempt.getPoints() >= attempt.getChallenge().getPoints() * 0.5)
					passes++;
			}
		}
		
		if (total == 0)
			return null;

		return Math.floor((passes / total) * 10000) / 100;
	}
	
	public static Double failRate(Set<Challenge> assignments) {
		return 100 - AttemptUtil.passRate(assignments);
	}

	public static void suppressPoints(Attempt attempt) {
		if (attempt.getPoints() < 0)
			attempt.setPoints(0);
		if (attempt.getPoints() > attempt.getChallenge().getPoints())
			attempt.setPoints(attempt.getChallenge().getPoints());
	}

	public static void fillStatus(Attempt attempt) {
		if (attempt.getStatus() != null)
			return;
		else
			attempt.setStatus(Statuses.IN_PROGRESS);

		if (attempt.getPoints() >= attempt.getChallenge().getPoints())
			attempt.setStatus(Statuses.PERFECT);
		else if (attempt.getPoints() >= attempt.getChallenge().getPoints() * 0.5)
			attempt.setStatus(Statuses.COMPLETED);
		else
			attempt.setStatus(Statuses.FAIL);
	}

}
