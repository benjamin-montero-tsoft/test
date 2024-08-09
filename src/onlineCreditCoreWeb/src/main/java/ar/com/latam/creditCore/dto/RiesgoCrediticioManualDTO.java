package ar.com.latam.creditCore.dto;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class RiesgoCrediticioManualDTO{

	/**
	 * Tabla RC_RIESGO_CREDITICIO_MANUAL
	 */
	
	/* CAMPO Party_Identification_Type_Cd (Tipo de Documento)*/
	private String tipoDoc;
	
	/* CAMPO Party_Identification_Num (Nro de Documento)*/
	private String nroDoc;
	
	/* CAMPO Party_Gender_Type_Cd (Sexo)*/
	private String genero;	
	
	/* CAMPO Party_Credit_Score_M_Ind (Score manual)*/
	private String scoreManual;
	
	/* CAMPO Update_Manual_M_Dt (Fecha carga del score manual)*/
	private String fechaCargaScoreManual;
	
	/* CAMPO User_Update_M_Cd (Usuario de carga)*/
	private String usuarioCargaCambio;	
	
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
	public String getGenero() {
		return genero;
	}
	public void setGenero(String genero) {
		this.genero = genero;
	}	
	public String getScoreManual() {
		return scoreManual;
	}
	public void setScoreManual(String scoreManual) {
		this.scoreManual = scoreManual;
	}
	public String getFechaCargaScoreManual() {
		return fechaCargaScoreManual;
	}
	
	public void setFechaCargaScoreManual(String fechaCargaScoreManual) {
		this.fechaCargaScoreManual = fechaCargaScoreManual;
	}
	public String getUsuarioCargaCambio() {
		return usuarioCargaCambio;
	}
	public void setUsuarioCargaCambio(String usuarioCargaCambio) {
		this.usuarioCargaCambio = usuarioCargaCambio;
	}

}
