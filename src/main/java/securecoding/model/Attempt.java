package securecoding.model;

import java.util.Date;
import java.util.HashMap;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "attempts")
@Getter
@Setter
@NoArgsConstructor
public class Attempt {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) int id;
	private int points;
	private String status;
	private String submission;

	private Date dateSubmitted;

	@Transient
	private HashMap<String, Object> data = new HashMap<>();

	@ManyToOne
	@JoinColumn(name = "user")
	@JsonBackReference
	private User user;
	@ManyToOne
	@JoinColumn(name = "challenge")
	@JsonBackReference
	private Challenge challenge;

	public Attempt(User user, Challenge challenge) {
		this.user = user;
		this.challenge = challenge;

		this.dateSubmitted = new Date();
	}
	
	public void addPoints(int points) {
		this.points += points;
	}
	
	public void deductPoints(int points) {
		this.points -= points;
	}

}
