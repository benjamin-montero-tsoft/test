package ar.com.latam.creditCore.dto;

import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class AltaManualClienteRiesgoCrediticioDTO{
	
	private String tipoDoc;
	private String nroDoc;
	private String genero;
	private String cargaGenero;
	private String tipoDocAmdocPipeCargaGeneroBusq;	
	private String scoreManual;
	private String usuarioAplicacion;
	
	public String getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public String getNroDoc() {
		return nroDoc;
	}
	public String getNroDocFormato() {
		String formatoNroDoc = nroDoc;
		if(Util.CL.equals(tipoDoc.toUpperCase()) || Util.CUIL.equals(tipoDoc.toUpperCase()) || Util.CU.equals(tipoDoc.toUpperCase()) || Util.CUIT.equals(tipoDoc.toUpperCase())){
			if(nroDoc.length() == 11){
				formatoNroDoc = nroDoc.substring(0,2) + Util.GUION + nroDoc.substring(2,10) + Util.GUION + nroDoc.substring(10,11);
			}			
		}
		return formatoNroDoc;
	}	
	public void setNroDoc(String nroDoc) {
		this.nroDoc = nroDoc;
	}
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}
	public String getCargaGenero() {
		return cargaGenero;
	}
	public void setCargaGenero(String cargaGenero) {
		this.cargaGenero = cargaGenero;
	}
	public String getTipoDocAmdocPipeCargaGeneroBusq() {
		return tipoDocAmdocPipeCargaGeneroBusq;
	}
	public void setTipoDocAmdocPipeCargaGeneroBusq(String tipoDocAmdocPipeCargaGeneroBusq) {
		//En el select de la ventana altaDNIProspectos.jsp se guardan los valores de tipoDocAmdoc y
		//de cargaGenero concatenadas por un pipe. Se procede a separarlas:
		this.tipoDocAmdocPipeCargaGeneroBusq = tipoDocAmdocPipeCargaGeneroBusq;
		String [] campos = tipoDocAmdocPipeCargaGeneroBusq.split("\\|");
		if (campos.length == 2){
			this.setTipoDoc(campos[0]);
			this.setCargaGenero(campos[1]);
		}
	}	
	public String getScoreManual() {
		return scoreManual;
	}
	public void setScoreManual(String scoreManual) {
		this.scoreManual = scoreManual;
	}
	public String getUsuarioAplicacion() {
		return usuarioAplicacion;
	}
	public void setUsuarioAplicacion(String usuarioAplicacion) {
		this.usuarioAplicacion = usuarioAplicacion;
	}

}
