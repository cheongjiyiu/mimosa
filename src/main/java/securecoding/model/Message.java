package securecoding.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Message {

	private Attempt attempt;
	private Exception exception;
	private Object data;
	private String type;
	
}
