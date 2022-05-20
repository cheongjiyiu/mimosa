package securecoding.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hints")
@Getter
@Setter
@NoArgsConstructor
public class Hint {

	private @Id String url;
	private String hint;
	private int cost;
	private int priority;

	@ManyToOne
	@JoinColumn(name = "challenge")
	@JsonBackReference
	private Challenge challenge;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "contracts", joinColumns = { @JoinColumn(name = "hint") }, inverseJoinColumns = {
			@JoinColumn(name = "user") })
	@JsonBackReference
	private Set<User> users;

	public Hint(String url, String hint, int cost, int priority, Challenge challenge) {
		this.url = url;
		this.hint = hint;
		this.cost = cost;
		this.priority = priority;

		this.challenge = challenge;
		this.users = new HashSet<>();
	}
	
	public Hint(String hint, int cost, int priority, Challenge challenge) {
		this.url = challenge.getUrl() + "/" + priority;
		this.hint = hint;
		this.cost = cost;
		this.priority = priority;

		this.challenge = challenge;
		this.users = new HashSet<>();
	}

}
