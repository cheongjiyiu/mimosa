package securecoding.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import securecoding.util.UserUtil;

@Entity
@Table(name = "notices")
@Getter
@Setter
@NoArgsConstructor
public class Notice {

	private @Id @GeneratedValue(strategy = GenerationType.IDENTITY) int id;
	private String title;
	private String content;
	private Date date;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user")
	@JsonBackReference
	private User user;

	public Notice(String title, String content) {
		// Assume current user and date
		this(title, content, UserUtil.getCurrentUser(), new Date());
	}

	public Notice(String title, String content, User user, Date date) {
		this.title = title;
		this.content = content;
		this.user = user;
		this.date = date;
	}

}
