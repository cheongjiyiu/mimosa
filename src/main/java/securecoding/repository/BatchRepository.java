package securecoding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import securecoding.model.Batch;
import securecoding.model.Challenge;
import securecoding.model.User;

@RepositoryRestResource(path = "batches")
public interface BatchRepository extends JpaRepository<Batch, Integer> {

	public Batch findByNameAndYear(String name, String year);
	public Batch findByIdAndAssignments(int id, Challenge challenge);
	public List<Batch> findByLecturer(User user);
	public List<Batch> findByLecturerAndEnabled(User user, boolean enabled);
	public List<Batch> findAllByEnabled(boolean enabled);
	
}
