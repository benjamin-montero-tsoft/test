package ar.com.latam.creditCore.dto;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class BusquedaRiesgoCrediticioDTO {

	private String tipoDoc;
	private String nroDoc;
	private String cargaGenero;
	private String genero;
	private String tipoDocAmdocPipeCargaGeneroBusq;
	private String mensajeError;
	private String fechaDesde;
	private String fechaHasta;
	
	public String getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public String getNroDoc() {
		return nroDoc;
	}
	public void setNroDoc(String nroDoc) {
		this.nroDoc = nroDoc;
	}
	public String getCargaGenero() {
		return cargaGenero;
	}
	public void setCargaGenero(String cargaGenero) {
		this.cargaGenero = cargaGenero;
	}	
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getTipoDocAmdocPipeCargaGeneroBusq() {
		return tipoDocAmdocPipeCargaGeneroBusq;
	}
	public void setTipoDocAmdocPipeCargaGeneroBusq(String tipoDocAmdocPipeCargaGeneroBusq) {
		//En el select de la ventana riesgoCrediticioHist.jsp se guardan los valores de tipoDocAmdoc y
		//de cargaGenero concatenadas por un pipe. Se procede a separarlas:
		this.tipoDocAmdocPipeCargaGeneroBusq = tipoDocAmdocPipeCargaGeneroBusq;
		String [] campos = tipoDocAmdocPipeCargaGeneroBusq.split("\\|");
		if (campos.length == 2){
			this.setTipoDoc(campos[0]);
			this.setCargaGenero(campos[1]);
		}
	}	
	
	public String getMensajeError() {
		return mensajeError;
	}
	public void setMensajeError(String mensajeError) {
		this.mensajeError = mensajeError;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
}
