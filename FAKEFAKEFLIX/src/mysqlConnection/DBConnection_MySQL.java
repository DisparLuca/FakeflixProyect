package mysqlConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

public class DBConnection_MySQL {

	// ATRIBUTOS
	// -----------------------------------------------------

	private static String DB_urlComplete;

	private static String user;
	private static String pass;

	private static DBConnection_MySQL instance;
	private Connection connection;

	// CONSTRUCTOR
	// -----------------------------------------------------

	private DBConnection_MySQL()
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		Class.forName("com.mysql.jdbc.Driver").newInstance();

		connection = DriverManager.getConnection(DB_urlComplete, user, pass);

	}// Constructor Principal

	// MÉTODOS
	// -----------------------------------------------------

	public static DBConnection_MySQL getInstance(HashMap<String, String> connectionData)
			throws SQLException, InstantiationException, IllegalAccessException, ClassNotFoundException {

		if (instance == null) {

			DB_urlComplete = connectionData.get("db_urlcomplete");
			user = connectionData.get("user");
			pass = connectionData.get("pass");

			instance = new DBConnection_MySQL();

		} // if

		return instance;

	}// getInstance

	public Connection getConnection() {

		return connection;

	}// getConnection

	public static HashMap<String, String> getConnectionSettings() {

		Properties properties = new Properties();
		HashMap<String, String> connectionData = null;

		try {
			properties.loadFromXML(new FileInputStream(new File(".\\settings\\DAOsettings.xml")));

			connectionData = new HashMap<String, String>();

			String DB_urlComplete = properties.getProperty("mysql_db_url") + properties.getProperty("mysql_db_name")
					+ properties.getProperty("mysql_db_properties_ssl")
					+ properties.getProperty("mysql_db_properties_servertimezone");
			String user = properties.getProperty("mysql_user");
			String pass = properties.getProperty("mysql_pass");

			connectionData.put("db_urlcomplete", DB_urlComplete);
			connectionData.put("user", user);
			connectionData.put("pass", pass);

		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return connectionData;

	}// getConnectionSetting

}// DBConnection_MySQL