package control;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import DAO.DAOClients;
import DAO.DAOException;
import model.Client;
import model.Input;

public class DatabaseClients implements DAOClients<Client, Integer>{
	
	private Connection connection;
	// private final String GET= "SELECT shirt_num, name, position FROM players
	// WHERE shirt_num = ?";
	private static final String SQL_SELECT = "SELECT idCliente, name, dateBirth, city, premium FROM cliente";
	private static final String SQL_GETFILMSBYUSER = "SELECT name FROM pelicula";
	private static final String SQL_USERCHECK = "SELECT premium FROM cliente WHERE idCliente = ?";
	private static final String SQL_INSERTUSER = "INSERT INTO cliente(name, dateBirth, city, premium) VALUES(?, ?, ?, ?)";
	private static final String SQL_DELETEUSER = "DELETE FROM cliente WHERE idCliente = ?";

	private static Logger logger;
	static {
		try {
			// you need to do something like below instead of Logger.getLogger(....);
			// Logger.getLogger se usaba hasta la versiÃ³n 2.7 y muchos ejemplos de internet
			// estan asi
			logger = LogManager.getLogger(Database.class);
		} catch (Throwable e) {
			System.out.println("Don't work");
		}
	}
	
	public DatabaseClients(Connection connection) {
		this.connection= connection;
	}
	
	public List <Client> getAll() throws DAOException {
		
		List<Client> listaClientes = new ArrayList<Client>();
		
		ResultSet rs= null;
		PreparedStatement stmt = null;
		
		
		try {
            stmt = connection.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while(rs.next()){
            	                
            	int id_cliente = rs.getInt("idCliente");
            	String name = rs.getString("name");
            	String city = rs.getString("city");
            	boolean premium = rs.getBoolean("premium");
            	int birthDate = rs.getInt("dateBirth");
                
                Client cliente = new Client();
                cliente.setId_cliente(id_cliente);
                cliente.setName(name);
                cliente.setCity(city);
                cliente.setPremium(premium);
                cliente.setBirthdate(birthDate);
                
                listaClientes.add(cliente);
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally{
        }
		System.out.println(listaClientes);	
		return listaClientes;
		
		
	}
	
	/**
	 * @author David. listado de pelçiculas que puede ver un cliente.
	 */
	public void getFilmsByUser() throws DAOException {

		PreparedStatement stmt = null;

		System.out.println("Introduzca el id de un usuario para comprobar su disponibilidad de catálogo: ");
		ResultSet rs;
		int idCheck = Input.readInt();
		int isPremium = 0;
		try {
			logger.info("buscando al usuario de id. indicada...");
			stmt = connection.prepareStatement(SQL_USERCHECK);
			stmt.setInt(1, idCheck);
			rs = stmt.executeQuery();
			if (rs.next()) {
				isPremium = rs.getInt("premium");
			}
		} catch (SQLException e) {
			logger.info("ese usuario no existe");
			System.out.println(e);
		}
		if (isPremium == 1) {
			try {
				logger.info("devolviendo la lista de películas disponibles para el usuario...");
				stmt = connection.prepareStatement(SQL_GETFILMSBYUSER);
				rs = stmt.executeQuery(SQL_GETFILMSBYUSER);
				System.out.println("La lista de películas disponibles para este usuario es: \n");
				while (rs.next()) {
					System.out.println(rs.getString("name"));

				}

			} catch (SQLException e) {

			}
		} else {
			// aquí se añadirían querys específicas para varios tipos de usuarios, si los
			// hubiese.
		}

	}
	
	/**
	 * @author David Pacios FernÃ¡ndez CreaciÃ³n del mÃ©todo que intorduce un/a
	 *         cliente en la base de datos. no habÃ­a tocado base de datos nunca,
	 *         muy divertido. El mÃ©todo pide al usuario los datos del cliente:
	 *         nombre, fecha de nacimiento, ciudad y si es o no cliente premium.
	 */
	@Override
	public void saveUser() {

		PreparedStatement stmt = null;
		Client u = new Client();
		Input in = new Input();

		System.out.println("now select the client's name: ");
		String name = in.readString();
		u.setName(name);
		System.out.println("select the client's date of birth: ");
		int dateBirth = in.readInt();
		u.setBirthdate(dateBirth);

		System.out.println("select the client's city of residence: ");
		String city = in.readString();
		u.setCity(city);
		System.out.println("select if the client's contract is of the premium modality(Y/N):");
		int isPremium = 0;
		String modality;
		do {
			modality = in.readString();
			if (modality.equalsIgnoreCase("y")) {
				isPremium = 1;
			} else if (modality.equalsIgnoreCase("n")) {
				isPremium = 0;
			} else {
				logger.info("sorry, only 'Y' or 'N' are acceptable responses");
			}
		} while (!modality.equalsIgnoreCase("y") && !modality.equalsIgnoreCase("n"));

		try {

			stmt = connection.prepareStatement(SQL_INSERTUSER);
			stmt.setString(1, u.getName());
			stmt.setInt(2, u.getBirthdate());
			stmt.setString(3, u.getCity());
			stmt.setInt(4, isPremium);
			stmt.executeUpdate();

			System.out.println("ejecutando query:" + SQL_INSERTUSER);
		} catch (SQLException ex) {
			ex.printStackTrace(System.out);
		}
	}

	/**
	 * @author David mÃ©todo para pedir la id de un usuario y borrarlo de la base de
	 *         datos.
	 */

	@Override
	public void deleteUser() {

		PreparedStatement stmt = null;
		Input in = new Input();
		System.out.println("Select the id of the user to delete: ");
		int idToDelete = in.readInt();

		try {

			stmt = connection.prepareStatement(SQL_DELETEUSER);
			stmt.setInt(1, idToDelete);
			stmt.executeUpdate();
			System.out.println("ejecutando query:" + SQL_DELETEUSER);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	public void save(Client cliente) {
		
	}
	
	
	public void delete(int t) {
		
	}
	
	
	public void update(Client cliente) {
		
	}

	void cargarDatos() {
		
	}

	@Override
	public Client get(Integer t) throws DAOException {
		// TODO Auto-generated method stub
		return null;
	}

}
