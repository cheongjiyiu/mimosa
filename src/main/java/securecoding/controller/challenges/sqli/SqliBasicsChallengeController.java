package securecoding.controller.challenges.sqli;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import securecoding.controller.template.ChallengeController;
import securecoding.controller.template.ChallengeControllerAdapter;
import securecoding.model.Attempt;
import weilianglol.acacia.database.DatabaseManager;
import weilianglol.acacia.database.Populator;

@ChallengeController("/challenges/sqli-basics")
public class SqliBasicsChallengeController extends ChallengeControllerAdapter {

	@Override
	public void unitTest(Attempt attempt, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String username = request.getParameter("username");
		String password = request.getParameter("password");

		try (DatabaseManager db = new DatabaseManager(attempt.getUser().getUsername())) {
			Populator.populateUsers(db);

			// Do unprotected SQL
			String sql = "select * from users where username = '" + username + "' and password = '" + password + "';";
			Statement stmt = db.getConnection().createStatement();
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next())
				attempt.addPoints(15);
		} catch (SQLException e) {
			// Fall through...
		}
	}

}
