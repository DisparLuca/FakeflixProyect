package DAO;

import java.util.List;

import model.Film;

public interface DAO<T, K> {
	
	//Obtener
	public T get(K t) throws DAOException;
	
	//Obtener Todos
	public List<T> getAll() throws DAOException;
	
	//Guardar
	public void save(T t) throws DAOException;
	
	//Eliminar
	public void delete(int t) throws DAOException;
	
	//Actualizar
	public void update(T t) throws DAOException;

	void cargarDatos() throws DAOException;
	
		//listado de pelis ordenadas por número de visitas
	public void getMostViewedFilms() throws DAOException;
	
	public List <T>  getNotViewedFilms(int idUser) throws DAOException;
	
	public List<String> getMostLikedFilms()throws DAOException;

}//DataAccessObject
