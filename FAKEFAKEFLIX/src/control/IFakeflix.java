package control;

import DAO.DAOException;

/**
 * Esta interfaz permite mostrar el menu principal de la aplcación
 **/

public interface IFakeflix {

	public void verMenu();
	
	public void rolEntrada();
	
	public void adminOpciones();
	
	public void userOpciones();
	
	public void opcionesInicio() throws DAOException;
	
	public void opcionesAdmin() throws DAOException;
	
	
}
