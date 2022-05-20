package securecoding.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "achievements")
@Getter
@Setter
@NoArgsConstructor
public class Achievement {
	
	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) int id;
	private String code;
	private String title;
	private String description;
	
	private int points;
	private boolean enabled;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "unlocks", joinColumns = { @JoinColumn(name = "achievement") }, inverseJoinColumns = {
			@JoinColumn(name = "user") })
	@JsonBackReference
	private Set<User> users;
	
	public Achievement(String code, String title, String description, int points) {
		this.code = code;
		this.title = title;
		this.description = description;
		
		this.points = points;
		this.enabled = true;

		this.users = new HashSet<>();
	}
	
}
