package control;

import DAO.DAO;
import DAO.DAOClients;
import DAO.DAOException;
import DAO.FactoryDAO;
import model.Client;
import model.Film;
import model.Input;

public class Fakeflix implements IFakeflix {
	private DAO<Film, Integer> dao;
	private DAOClients<Client, Integer> daoCliente ;
	
	


	public Fakeflix(DAO<Film, Integer> dao, DAOClients<Client, Integer> daoCliente) {
		super();
		this.dao = FactoryDAO.getInstance().getDAO();
		this.daoCliente = FactoryDAO.getInstance().getDAOClient();
	}

	public DAO<Film, Integer> getDao() {
		return dao;
	}

	public void setDao(DAO<Film, Integer> dao) {
		this.dao = dao;
	}

	public DAOClients<Client, Integer> getDaoCliente() {
		return daoCliente;
	}

	public void setDaoCliente(DAOClients<Client, Integer> daoCliente) {
		this.daoCliente = daoCliente;
	}



	@Override
	public void verMenu() {

		System.out.println("*******************************");
		System.out.println("********MENU DE OPCIONES*******");
		System.out.println();
		System.out.println("0. Salir");
		System.out.println();
		System.out.println("*******************************");
		System.out.println("*******************************");
	}

	public void rolEntrada() {
		System.out.println("1. Administrador del sistema");
		System.out.println("2. Usuario");
	}
	
	public void adminOpciones() {
		System.out.println("1. listar usuarios");
		System.out.println("2. alta usuario");
		System.out.println("3. baja usuario");
		System.out.println("4. listar todas las peliculas");
		System.out.println("5. listar peliculas por genero");
		System.out.println("6. listar peliculas no vistas de un usuario");	
		System.out.println("7. listar peliculas mejor valoradas");
		//System.out.println("8. listar peliculas vistas");
		System.out.println("9. añadir pelicula"); 
		System.out.println("10. listar pelicula disponible para un usuario");
		System.out.println("11. listar pelicula mas vistas");
		
		
	}
	
	public void userOpciones() {
		System.out.println("aun sin opciones");
	}
	
	public void opcionesInicio() throws DAOException {
		
		int numero = 0;
		boolean exit = false;
		while (!exit) {
			
			verMenu();
			rolEntrada();
			numero= Input.readInt();


			switch (numero) {
			case 0:
				exit = true;
				break;
			case 1:
				adminOpciones();
				opcionesAdmin();
				break;
			case 2:
				userOpciones();
				break;
			default:

				break;
		

			}
		}
		
	}
	
	public void opcionesAdmin() throws DAOException {

		int numero = 0;
		boolean exit = false;
		while (!exit) {
			verMenu();
			adminOpciones();
			numero= Input.readInt();
			

			switch (numero) {
			case 0:
				exit = true;
				break;
			case 1:
				//listar usuarios
				System.out.println(dao.getAll());
				break;
			case 2:
				//alta usuario
				daoCliente.saveUser();
				break;
			case 3:
				//baja usuario
				daoCliente.deleteUser();
				break;
			case 4:
				//listar todas las peliculas
				dao.getAll();
				break;
			case 5:
				//listar peliculas por genero
				dao.getFilmByGender();
				break;
			case 6:
				//listar peliculas no vistas
				System.out.println("elige el usuario para listar peliculas que no ha visto");
				System.out.println(dao.getAll());
				
				dao.getNotViewedFilms(Input.readInt());
				break;

			case 7:
				//listar peliculas mejor valoradas
				System.out.println(dao.getMostLikedFilms());
				break;
			case 8:
				//listar peliculas vistas
				
				break;
			case 9:
				//añadir pelicula
				dao.introducirPeliculas();
				break;
			case 10:
				// listar pelicula disponible para un usuario
				dao.getFilmsByUser();
				break;
			case 11:
				//listar pelicula mas vistas
				dao.getMostViewedFilms();
				break;
				
				
			default:

				break;
		

			}
		}
	}

}
