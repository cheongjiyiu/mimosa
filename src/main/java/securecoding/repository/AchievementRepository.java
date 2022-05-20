package securecoding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import securecoding.model.Achievement;
import securecoding.model.User;

@RepositoryRestResource(path = "achievements")
public interface AchievementRepository extends JpaRepository<Achievement, Integer> {

	public Achievement findByCode(String code);
	public List<Achievement> findByUsers(User user);
	
}
