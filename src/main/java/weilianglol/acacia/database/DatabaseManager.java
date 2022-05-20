package weilianglol.acacia.database;

import java.io.Closeable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager implements Closeable {

	private String url;
	private Connection connection;

	public DatabaseManager(String name) {
		this.url = "jdbc:h2:mem:" + name + ";MODE=MySQL;DB_CLOSE_DELAY=10;CASE_INSENSITIVE_IDENTIFIERS=true";
		System.setProperty("h2.consoleTimeout", "10000");

		try {
			Class.forName("org.h2.Driver");
		} catch (ClassNotFoundException e) {
		}
	}
	
	public Connection getConnection() throws SQLException {
		if (connection == null || connection.isClosed())
			connection = DriverManager.getConnection(url, "acacia", "password");

		return connection;
	}

	@Override
	public void close() {
		try {
			Populator.unpopulateAll(this);
			connection.close();
		} catch (SQLException e) {
		}
	}

}
