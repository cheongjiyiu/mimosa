package securecoding.util;

import java.sql.Connection;
import java.sql.SQLException;

import weilianglol.acacia.database.DatabaseManager;

public class DatabaseUtil {

	@SuppressWarnings("resource")
	public static Connection getConnection(String name) throws SQLException {
		return new DatabaseManager(name).getConnection();
	}
	
}
