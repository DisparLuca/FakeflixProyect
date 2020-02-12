package control;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import DAO.DAO;
import DAO.DAOException;
import model.Client;
import model.Film;
import model.Input;
import mysqlConnection.DBConnection_MySQL;

public class Database implements DAO<Film, Integer> {

	private Connection connection;
	private final String GET = "SELECT name,category,year FROM pelicula";
	private static final String SQL_INSERT = "INSERT INTO pelicula(name, year, category) VALUES(?, ?, ?)";
	private static final String getFilmByGender = "SELECT * FROM pelicula WHERE category= ?";
	private static final String getAllFilms = "SELECT * FROM pelicula";
	private static final String  getNotViewedFilms = "SELECT a.name AS nClient, b.name AS nFilm , b.year, b.category FROM cliente a , pelicula b, tablec_p c WHERE a.idCliente=c.idCliente AND b.idFilm=c.idFilm AND c.vista=0 AND a.idCliente=?";
	private static final String getMostLikedFilms = "SELECT b.name,b.year,b.category, AVG (c.nota) as media FROM cliente a , pelicula b, tablec_p c WHERE c.nota IS NOT NULL AND b.idFilm=c.idFilm GROUP BY c.idFilm ORDER BY media DESC LIMIT 5";
	

	private static Logger logger;
	static {
		try {
			// you need to do something like below instead of Logger.getLogger(....);
			// Logger.getLogger se usaba hasta la versión 2.7 y muchos ejemplos de internet
			// estan asi
			logger = LogManager.getLogger(Database.class);
		} catch (Throwable e) {
			System.out.println("Don't work");
		}
	}
	private File archivo = null;
	private FileReader fr = null;
	private BufferedReader br = null;

	public Database(Connection connection) {
		this.connection = connection;
	}

	
	
	@Override
	public Film get(Integer idFilm) throws DAOException {
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		Film film = null;

		try {

			preparedStatement = connection.prepareStatement(GET);
			preparedStatement.setInt(1, idFilm);
			resultSet = preparedStatement.executeQuery();

			if (resultSet.next()) {
				String name = resultSet.getString("name");
				String category = resultSet.getString("category");
				int year = resultSet.getInt("year");

				film = new Film(name, category, year);
				System.out.println(film.getName());

			} // if

		} catch (SQLException e) {
			throw new DAOException("Excepción SQL", e);

		} finally {

			try {

				preparedStatement.close();
				resultSet.close();

			} catch (SQLException e) {
				throw new DAOException("Excepción SQL", e);
			}

		} // finally

		return film;
	}

	@Override
	public void save(Film pelicula) throws DAOException {

		PreparedStatement stmt = null;

		try {

			stmt = connection.prepareStatement(SQL_INSERT);
			stmt.setString(1, pelicula.getName());
			stmt.setInt(2, pelicula.getYear());
			stmt.setString(3, pelicula.getCategory());
			stmt.executeUpdate();

			System.out.println("ejecutando query:" + SQL_INSERT);
		} catch (SQLException ex) {
			ex.printStackTrace(System.out);
		}

	}

	@Override
	public void cargarDatos() {

		logger.info("Leyendo fichero de peliculas");
		try {

			archivo = new File("./FakeFlix/settings/peliculas_cat.txt");
			fr = new FileReader(archivo);
			br = new BufferedReader(fr);

			String linea = null;

			Film pelicula;
			while ((linea = br.readLine()) != null) {

				String[] parts = linea.split(",");
				String nombrePelicula = parts[0];
				String yearString = parts[1];
				String categoria = parts[2];
				int yearInt = Integer.parseInt(yearString);
				pelicula = new Film(nombrePelicula, categoria, yearInt);
				save(pelicula);
				System.out.println(pelicula);

			}
			logger.info("Peliculas leidas correctamente");

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (null != fr) {
					fr.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}
	
	/**
	 * @author David
	 * método para listar las pelis ordenadas por número de visitas. FUERTE contribución de Alvaro.
	 */	
	@Override
	public void getMostViewedFilms() throws DAOException {
		
		PreparedStatement stmt = null;
		try {
			logger.trace("generando lista de películas ordenadas por número de visionados...");
		stmt = connection.prepareStatement(getMostViewedFilms);
		ResultSet rs = stmt.executeQuery();
		logger.trace("imprimiendo la lista: ");
		while (rs.next()){
			
			System.out.println("[Película: " + rs.getString("name") + " --- número de visitas: " + rs.getInt("cantidadVisitas") + "]");
		}
		} catch (SQLException e) {
			e.printStackTrace(System.out);
		}
	}
	
	@Override
	public void getFilmByGender() throws DAOException {
		PreparedStatement stmt = null;
		Film f = new Film();
		String category = null;
		System.out.println("select the film's category: ");
		int option = Input.readInt();
		switch (option) {
		case 1:
			category = "comedia";
			break;
		case 2:
			category = "romantica";
			break;
		case 3:
			category = "animacion";
			break;
		case 4:
			category = "thriller";
			break;
		case 5:
			category = "aventuras";
			break;
		case 6:
			category = "policiaca";
			break;
		case 7:
			category = "Ciencia ficcion";
			break;
		}
		f.setCategory(category);
		try {

			stmt = connection.prepareStatement(getFilmByGender);
			stmt.setString(1, f.getCategory());
			stmt.executeUpdate();

			System.out.println("ejecutando query:" + getFilmByGender);
		} catch (SQLException ex) {
			ex.printStackTrace(System.out);
		}
	}

	@Override
	public List<Film> getAll() throws DAOException {
		List<Film> listaClientes = new ArrayList<Film>();

		ResultSet rs = null;
		PreparedStatement stmt = null;

		try {
			stmt = connection.prepareStatement(getAllFilms);
			rs = stmt.executeQuery();
			while (rs.next()) {

				String name = rs.getString("name");
				String category = rs.getString("category");
				int year = rs.getInt("year");

				Film f = new Film();
				f.setName(name);
				f.setCategory(category);
				f.setYear(year);

				listaClientes.add(f);
			}

		} catch (SQLException ex) {
			ex.printStackTrace(System.out);
		}
		return listaClientes;
	}

	@Override
	public void delete(int t) throws DAOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Film t) throws DAOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @author Pablo introducci�n de pel�culas dadas por el cliente en la BBDD.
	 * @throws DAOException
	 */
	@Override
	public void introducirPeliculas() throws DAOException {

		Film pelicula = new Film();

		System.out.println("Introduzca el nombre de la pelicula:");
		pelicula.setName(Input.readString());
		System.out.println("Introduzca el a�o de la pelicula:");
		pelicula.setYear((Input.readInt()));
		System.out.println("Introduzca el genero de la pelicula:");
		pelicula.setCategory(Input.readString());
		save(pelicula);
		logger.info("Pelicula guardada correctamente");
	}

	/**
	 * @author Pablo Muñoz
	 * Metodo que obtiene las peliculas que no ha visto un usuario dado por parametro
	 * @param idUser codigo de usuario
	 */
	public List <Film> getNotViewedFilms(int idUser) throws DAOException {
		
		List<Film> listaPeliculas = new ArrayList<Film>();
		ResultSet rs= null;
		PreparedStatement stmt = null;
		
		try {
			
            stmt = connection.prepareStatement(getNotViewedFilms);
            stmt.setInt(1, idUser);
            rs = stmt.executeQuery();
            while(rs.next()){
            	                
            	String nameFilm = rs.getString("nFilm"); 
            	String category = rs.getString("category");
            	int year = rs.getInt("year");
                Film f = new Film();             
                f.setName(nameFilm);
                f.setYear(year);
                f.setCategory(category);
                listaPeliculas.add(f);
                            }
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
		
		return listaPeliculas;
	}
	
	
	/**
	 * @author Pablo Muñoz Álvaro Muñoz
	 * Metodo que obtiene las 5 peliculas mas valoradas por los usuarios
	 * */
	public List<String> getMostLikedFilms() throws DAOException{
		List<String> listaPeliculas = new ArrayList<String>();
		ResultSet rs= null;
		PreparedStatement stmt = null;
		Float media= null;
		
		try {
			
            stmt = connection.prepareStatement(getMostLikedFilms);
            rs = stmt.executeQuery();
            while(rs.next()){
            	                
            	String nameFilm = rs.getString("name");     
                media = rs.getFloat("media");
                listaPeliculas.add( "\n"+ nameFilm +"  "+ "Media: " + media);
           }
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
		
		return listaPeliculas;		
	}
	
}
