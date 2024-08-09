package ar.com.tasa.credi.exception;

public class ReporteCrediticioOnlineException extends Exception {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private String Mensaje;


	public String getMensaje() {
		return Mensaje;
	}

	public void setMensaje(String mensaje) {
		Mensaje = mensaje;
	}


	public ReporteCrediticioOnlineException (String mensaje){
		this.Mensaje = mensaje;
	}
}
