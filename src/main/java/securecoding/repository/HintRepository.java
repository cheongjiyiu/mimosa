package securecoding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import securecoding.model.Challenge;
import securecoding.model.Hint;
import securecoding.model.User;

@RepositoryRestResource(path = "hints")
public interface HintRepository extends JpaRepository<Hint, String> {

	public Hint findByUrl(String url);
	public List<Hint> findByChallenge(Challenge challenge);
	public List<Hint> findByUsers(User user);
	public List<Hint> findByUsersAndChallenge(User user, Challenge challenge);
	
}
