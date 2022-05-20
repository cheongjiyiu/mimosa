package securecoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import securecoding.model.Attempt;
import securecoding.model.Challenge;
import securecoding.model.User;

@RepositoryRestResource(path = "attempts")
public interface AttemptRepository extends JpaRepository<Attempt, Integer> {
	
	public Attempt findByUserAndChallenge(User user, Challenge challenge);

}
