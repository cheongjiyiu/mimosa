package securecoding.controller.challenges.sqli;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import securecoding.util.UserUtil;
import weilianglol.acacia.database.DatabaseManager;
import weilianglol.acacia.database.Populator;
import weilianglol.ixora.PageReader;

@ChallengeController("/challenges/dazala")
public class DazalaChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		if ("root".equals(username))
			attempt.addPoints(10);
		if ("secure123".equals(password))
			attempt.addPoints(10);
		if ("root".equals(username) && "secure123".equals(password))
			attempt.addPoints(10);
	}

	@RequestMapping(path = "/search", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> login(@RequestParam("search") String search) throws Exception {
		PageReader pageReader = new PageReader("templates/pages/challenges/dazala.html");
		String results = pageReader.getFragment("results").html();
		String content = "";

		try (DatabaseManager db = new DatabaseManager(UserUtil.getCurrentUser().getUsername())) {
			Populator.populateUsers(db);
			Populator.populateProducts(db);

			// Dazala entry
			db.getConnection().prepareStatement("INSERT INTO `users` VALUES ('root','secure123','Dazala','Root',1);")
					.execute();

			// Do unprotected SQL
			String sql = "select name, description, price from products where name like '%" + search
					+ "%' or description like '%" + search + "%' or price like '%" + search + "%';";
			Statement stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			// Add each entry
			while (rs.next())
				content += String.format("<tr><td>%s</td><td>%s</td><td>$%s</td></tr>", rs.getString("name"),
						rs.getString("description"), rs.getString("price"));
			
		} catch (SQLException e) {
			// Send to front end
			HashMap<String, String> error = new HashMap<>();
			error.put("error", "<hr/><p><small>" + e.getMessage() + "</small></p>");
			return ResponseEntity.badRequest().body(error);
		}

		if (content.isEmpty())
			return ResponseEntity.ok().body("<hr/><p><small>No results found! Try again!</small></p>");
		else
			return ResponseEntity.ok().body(results.replace("<tbody></tbody>", "<tbody>" + content + "</tbody>"));
	}

}
