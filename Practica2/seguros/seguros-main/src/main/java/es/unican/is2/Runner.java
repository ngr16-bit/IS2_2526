package es.unican.is2;


public class Runner {

	public static void main(String[] args) {
		IClientesDAO daoClientes = new ClientesDAO();
		ISegurosDAO daoSeguros = new SegurosDAO();
		GestionSeguros negocio = new GestionSeguros(daoClientes, daoSeguros);
		VistaAgente vista = new VistaAgente(
			(IGestionClientes) negocio, 
			(IGestionSeguros) negocio, 
			(IInfoSeguros) negocio)
			;
		vista.setVisible(true);
	}

}
