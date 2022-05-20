package securecoding.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import securecoding.model.Notice;

@RepositoryRestResource(path = "notices")
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
	
	public List<Notice> findAllByOrderByIdDesc();

}
