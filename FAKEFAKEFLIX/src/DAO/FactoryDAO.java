package DAO;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import control.Database;
import control.DatabaseClients;
import mysqlConnection.DBConnection_MySQL;
import model.Client;
import model.Film;

public class FactoryDAO {

	// ATRIBUTOS
	// -----------------------------------------------------

	private static FactoryDAO instance;
	private Properties properties;
	private String DAO_type;

	private String DAOsettingsXML = ".\\settings\\DAOsettings.xml";

	// CONSTRUCTOR
	// -----------------------------------------------------

	private FactoryDAO() {

		configDAO(DAOsettingsXML);

	}// Constructor Principal

	// MÉTODOS
	// -----------------------------------------------------

	private void configDAO(String DAOsettingsXML) {

		properties = new Properties();

		try {

			properties.loadFromXML(new FileInputStream(new File(DAOsettingsXML)));

		} catch (InvalidPropertiesFormatException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		DAO_type = properties.getProperty("set_dao");

	}// configDAO

	public static FactoryDAO getInstance() {

		if (instance == null)
			instance = new FactoryDAO();

		return instance;

	}// getInstance

	public DAO<Film, Integer> getDAO() {

		DAO<Film, Integer> dao = null;

		HashMap<String, String> connectionData;
		Connection connection = null;

		switch (DAO_type) {

		case "JDBC":

			connectionData = DBConnection_MySQL.getConnectionSettings();

			try {

				connection = DBConnection_MySQL.getInstance(connectionData).getConnection();

			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			dao = new Database(connection);

			break;

		}// switch

		return dao;

	}// getDAO

	public DAOClients<Client, Integer> getDAOClient() {

		DatabaseClients dao = null;

		HashMap<String, String> connectionData;
		Connection connection = null;

		switch (DAO_type) {

		case "JDBC":

			connectionData = DBConnection_MySQL.getConnectionSettings();

			try {

				connection = DBConnection_MySQL.getInstance(connectionData).getConnection();

			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			dao = new DatabaseClients(connection);

			break;

		}// switch

		return dao;

	}// getDAO

}// FactoriaDAO