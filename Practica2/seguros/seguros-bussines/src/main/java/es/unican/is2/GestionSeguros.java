package es.unican.is2;

/**
 * Clase que implementa la gestion de clientes y seguros.
 */
public class GestionSeguros implements IGestionClientes, IGestionSeguros, IInfoSeguros {

    private IClientesDAO daoClientes;
    private ISegurosDAO daoSeguros;

    public GestionSeguros(IClientesDAO daoClientes, ISegurosDAO daoSeguros) {
        this.daoClientes = daoClientes;
        this.daoSeguros = daoSeguros;
    }

    //Gestion Clientes

    /**
     * Anhade un nuevo cliente
     * @param dni dni del cliente
     * @return El cliente incluido o null si ya existía
     */
    @Override
    public Cliente nuevoCliente(Cliente c) throws DataAccessException {
    
        Cliente existente = daoClientes.cliente(c.getDni());
        if (existente != null) {
            return null; // already exists
        }
        return daoClientes.creaCliente(c);
    }

    /**
     * Elimina un cliente
     * @param dni dni del cliente
     * @return El cliente eliminado o null si no existe
     * @throws OperacionNoValida se lanza si el cliente tiene algun seguro
     */
    @Override
    public Cliente bajaCliente(String dni) throws OperacionNoValida, DataAccessException {
        Cliente c = daoClientes.cliente(dni);
        if (c == null) {
            return null;
        }
        if (c.getSeguros() != null && !c.getSeguros().isEmpty()) {
            throw new OperacionNoValida("El cliente tiene un seguro y no se puede dar de baja");
        }
        return daoClientes.eliminaCliente(dni);
    }

    //Gestion Seguros

    /**
     * Anhade un seguro a un cliente
     * @param s el seguro que se quiere anhadir
     * @param dni dni del cliente
     * @return El seguro anhadido o null si el cliente no existe
     * @throws OperacionNoValida si ya existe un seguro con misma matricula
     */
    @Override
    public Seguro nuevoSeguro(Seguro s, String dni) throws OperacionNoValida, DataAccessException {

        Cliente c = daoClientes.cliente(dni);
        if (c == null) {
            return null; 
        }

        Seguro existente = daoSeguros.seguroPorMatricula(s.getMatricula());
        if (existente != null) {
            throw new OperacionNoValida("Ya existe un seguro con la matricula introducida");
        }

        Seguro creado = daoSeguros.creaSeguro(s);
        c.getSeguros().add(creado);
        daoClientes.actualizaCliente(c);
        return creado;
    }

    /**
     * Elimina el seguro indicado.
     * @param matricula matricula del vehiculo que se quiere quitar del seguro
     * @param dni dni del cliente que tiene el seguro
     * @return el seguro eliminado o null si el seguro o el cliente no existen
     * @throws OperacionNoValida si el seguro no pertenece al cliente indicado
     */
    @Override
    public Seguro bajaSeguro(String matricula, String dni) throws OperacionNoValida, DataAccessException {

        Seguro s = daoSeguros.seguroPorMatricula(matricula);
        if (s == null) {
            return null; 
        }

        Cliente c = daoClientes.cliente(dni);
        if (c == null) {
            return null; 
        }

        //comprueba si el seguro pertenece al cliente
        boolean pertenece = c.getSeguros().stream()
            .anyMatch(seg -> seg.getMatricula().equals(matricula));
        if (!pertenece) {
            throw new OperacionNoValida("El seguro con matricula " + matricula + " no pertenece al cliente " + dni);
        }

        //elimina el seguro del cliente
        c.getSeguros().removeIf(seg -> seg.getMatricula().equals(matricula));
        daoClientes.actualizaCliente(c);
        return daoSeguros.eliminaSeguro(s.getId());
    }

    /**
     * Anhade un conductor adicional a un seguro
     * @param matricula matricula del coche asegurado
     * @param conductor conductor que se va a anhadir
     * @return el seguro modificado o null si el seguro no existe
     */
    @Override
    public Seguro anhadeConductorAdicional(String matricula, String conductor) throws DataAccessException {
        Seguro s = daoSeguros.seguroPorMatricula(matricula);
        if (s == null) {
            return null; 
        }
        s.setConductorAdicional(conductor);
        return daoSeguros.actualizaSeguro(s);
    }

    //Info Seguros

    /**
     * Busca un cliente en la base de datos a partir del dni
     * @return el cliente buscado o null si no existe
     */
    @Override
    public Cliente cliente(String dni) throws DataAccessException {
        return daoClientes.cliente(dni);
    }

    /**
     * Busca un seguro en la base de datos a partir de la matricula del coche asegurado.
     * @return el seguro buscado o null si no existe
     */
    @Override
    public Seguro seguro(String matricula) throws DataAccessException {
        return daoSeguros.seguroPorMatricula(matricula);
    }
    
}