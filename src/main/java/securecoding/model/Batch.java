package securecoding.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "batches")
@Getter
@Setter
@NoArgsConstructor
public class Batch {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) int id;
	private String name;
	private String year;
	private boolean enabled;
	private boolean open;

	@OneToMany(mappedBy = "batch", fetch = FetchType.LAZY)
	@JsonManagedReference
	private Set<User> students;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "lecturer")
	@JsonBackReference
	private User lecturer;
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "batches")
	@JsonBackReference
	private Set<Challenge> assignments;

	public Batch(String name, String year, User lecturer) {
		this.name = name;
		this.year = year;
		
		this.enabled = true;
		this.open = true;
		this.lecturer = lecturer;
		
		this.students = new HashSet<>();
		this.assignments = new HashSet<>();
	}
	
	public Batch(String name, String year) {
		this(name, year, null);
	}

}
