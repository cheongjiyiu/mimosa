package securecoding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import securecoding.model.Batch;
import securecoding.model.Challenge;
import securecoding.model.User;

@RepositoryRestResource(path = "challenges")
public interface ChallengeRepository extends JpaRepository<Challenge, String> {

	public Challenge findByUrl(String url);
	public Challenge findByUrlAndBatches(String url, Batch batch);
	public Challenge findByUrlAndBatches_students(String url, User user);

}
