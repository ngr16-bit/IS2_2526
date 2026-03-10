

/**
 * Interfaz con metodos de consulta de informacion
 * de la empresa de seguros
 */
public interface IInfoSeguros {
	
	/**
	 * Retorna el cliente cuyo dni se indica
	 * @param dni DNI del cliente buscado
	 * @return El cliente cuyo dni coincide
	 * 		   null en caso de que no exista
	 * @throws DataAccessException si se produce un error 
	 * en el acceso a la base de datos
	 */
	public Cliente cliente(String dni) throws DataAccessException; 
	
	/**
	 * Retorna el seguro cuya matricula se indica
	 * @param matricula Identificador del seguro
	 * @return El seguro indicado
	 * 	       null si no existe
	* @throws DataAccessException si se produce un error 
	 * en el acceso a la base de datos
	 */
	public Seguro seguro(String matricula) throws DataAccessException;

}
