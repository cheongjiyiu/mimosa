package weilianglol.acacia.database;

import java.sql.Connection;
import java.sql.SQLException;

public class Populator {

	public static void populateAll(DatabaseManager databaseManager) throws SQLException {
		Populator.populateUsers(databaseManager);
		Populator.populateProducts(databaseManager);
	}

	public static void populateUsers(DatabaseManager databaseManager) throws SQLException {
		Connection connection = databaseManager.getConnection();

		// Create table
		connection.prepareStatement("CREATE TABLE `users`( `username` varchar(45) NOT NULL, "
				+ " `password` char(65) NOT NULL, `firstname` varchar(45) NOT NULL, "
				+ "`lastname` varchar(45) NOT NULL, `enabled` tinyint(1) NOT NULL," + " PRIMARY KEY (`username`))")
				.execute();
		// Create entries
		connection.prepareStatement("INSERT INTO `users` VALUES ('albert','crystal','Albert','Tay',1),"
				+ "('angel','highlight','Amenadiel','Firstborn',1)," + "('crystal','meliodas','Crystal','Chiang',1),"
				+ "('devil','shaded','Lucifer','Morningstar',1)," + "('eliot','tictac','Eliot','Lim',1),"
				+ "('hacker','12345678','Unknown','The One',0)," + "('josephine','password','Josephine','Peh',1),"
				+ "('lily','flower','Lily','Swein',1)," + "('patrick','pandas','Patrick','Star',1),"
				+ "('spartacus','roman','Spartacus','The Great',0);").execute();
	}
	
	public static void populateMenu(DatabaseManager databaseManager) throws SQLException {
		Connection connection = databaseManager.getConnection();

		// Create table
		connection.prepareStatement("CREATE TABLE `menu`( `id` INT NOT NULL AUTO_INCREMENT, "
				+ " `name` VARCHAR(45) NOT NULL, `description` VARCHAR(150) NOT NULL, "
				+ "`price` INT NOT NULL, `quantity` INT NOT NULL," + " PRIMARY KEY (`id`))").execute();
		// Create entries
		connection
				.prepareStatement(
						"INSERT INTO `menu` VALUES (1,'Cheese Burger','Juicy burger, now with cheese.',3,10),"
								+ "(2,'Pancake Burger','Tasty pancakes, shaped like a burger.',4,10),"
								+ "(3,'Plastic Burger','...is this even edible?',4,40),"
								+ "(4,'Green Burger','It is green.',3,10),"
								+ "(5,'Mini Burger','Like a burger, but smaller.',2,20),"
								+ "(6,'Zoey''s Burger','A girl''s favourite.',5,10),"
								+ "(7,'Lemonade','When life gives you lemons.',1,40)," 
								+ "(8,'Grenade','When life gives you problems.',1,50);")
				.execute();
	}

	public static void populateProducts(DatabaseManager databaseManager) throws SQLException {
		Connection connection = databaseManager.getConnection();

		// Create table
		connection.prepareStatement("CREATE TABLE `products`( `id` INT NOT NULL AUTO_INCREMENT, "
				+ " `name` VARCHAR(45) NOT NULL, `description` VARCHAR(150) NOT NULL, "
				+ "`price` INT NOT NULL," + " PRIMARY KEY (`id`))").execute();
		// Create entries
		connection
				.prepareStatement(
						"INSERT INTO `products` VALUES (1,'Classy Book Stand','A nice wooden bookshelf.',1000),"
								+ "(2,'Woolen Pillow','Woven piece that goes well with red sofas.',30),"
								+ "(3,'Red Sofa','A comfortable red sofa.',400),"
								+ "(4,'Broken Stool','It is broken.',10),"
								+ "(5,'Mini Desk Lamp','Provides a small amount of light.',20),"
								+ "(6,'Classical Piano','Provides the joy of music.',3000),"
								+ "(7,'Toy Car','Zoom zoom.',5)," + "(8,'Barbie Girl','In a Barbie world.',5);")
				.execute();
	}

	public static void unpopulateAll(DatabaseManager databaseManager) throws SQLException {
		Populator.unpopulateUsers(databaseManager);
		Populator.unpopulateMenu(databaseManager);
		Populator.unpopulateProducts(databaseManager);
	}

	public static void unpopulateUsers(DatabaseManager databaseManager) throws SQLException {
		Connection connection = databaseManager.getConnection();
		connection.prepareStatement("drop table if exists users").execute();
	}
	
	public static void unpopulateMenu(DatabaseManager databaseManager) throws SQLException {
		Connection connection = databaseManager.getConnection();
		connection.prepareStatement("drop table if exists menu").execute();
	}
	
	public static void unpopulateProducts(DatabaseManager databaseManager) throws SQLException {
		Connection connection = databaseManager.getConnection();
		connection.prepareStatement("drop table if exists products").execute();
	}

}
