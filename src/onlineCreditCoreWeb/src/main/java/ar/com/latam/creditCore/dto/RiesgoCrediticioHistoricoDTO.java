package ar.com.latam.creditCore.dto;

import java.io.Serializable;

import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class RiesgoCrediticioHistoricoDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Tabla RC_RIESGO_CREDITICIO_HIST
	 */
	
	/* CAMPO Party_Identification_Type_Cd (Tipo de Documento)*/
	private String tipoDoc;
	
	private String tipoDocAMDOCS;
	
	/* CAMPO Party_Identification_Num (Nro de Documento)*/
	private String nroDoc;
		
	/* CAMPO Party_Gender_Type_Cd (Sexo)*/
	private String genero;
	
	/*Campo que se utiliza en la busqueda*/
	private String cargaGenero;
	
	/*Campo que se utiliza en la busqueda*/
	private String tipoDocAmdocPipeCargaGeneroBusq;
	
	/* CAMPO Party_Credit_Score_IN_Ind (Score calculado)*/
	private String scoreCalculadoIN;
	
	/* CAMPO Last_Updated_IN_Dt (Fecha carga del score IN)*/
	private String fechaCargaScoreIN;
	
	/* CAMPO Party_Credit_Score_AM_Ind (Score legado o Score T3)*/
	private String scoreLegadoAM;
	
	/* CAMPO Last_Updated_AM_Dt (Fecha de ultimo update o Fecha carga del Score T3)*/
	private String fechaUpdateAM;
	
	/* CAMPO Party_Credit_Score_M_Ind (Score manual)*/
	private String scoreManual;
	
	/* CAMPO Update_Manual_M_Dt (Fecha carga del score manual)*/
	private String fechaCargaScoreManual;
	
	/* CAMPO User_Update_M_Cd (Usuario de carga)*/
	private String usuarioCargaCambio;
	
	/* CAMPO Last_Updated_F_Dt (Fecha de último update)*/
	private String fechaUltimoUpdate;
	
	/* CAMPO Last_Updated_Dt (Fecha de la base)*/
	private String fechaBase;		
	
	public String getTipoDoc() {
		return tipoDoc;
	}
	public void setTipoDoc(String tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	public String getTipoDocAMDOCS() {
		return tipoDocAMDOCS;
	}
	public void setTipoDocAMDOCS(String tipoDocAMDOCS) {
		this.tipoDocAMDOCS = tipoDocAMDOCS;
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
		//En el select de la ventana riesgoCrediticioHist.jsp se guardan los valores de tipoDocAmdoc y
		//de cargaGenero concatenadas por un pipe. Se procede a separarlas:
		this.tipoDocAmdocPipeCargaGeneroBusq = tipoDocAmdocPipeCargaGeneroBusq;
		String [] campos = tipoDocAmdocPipeCargaGeneroBusq.split("\\|");
		if (campos.length == 2){
			this.setTipoDoc(campos[0]);
			this.setCargaGenero(campos[1]);
		}
	}	
	public String getScoreCalculadoIN() {
		return scoreCalculadoIN;
	}
	public void setScoreCalculadoIN(String scoreCalculadoIN) {
		this.scoreCalculadoIN = scoreCalculadoIN;
	}
	public String getFechaCargaScoreIN() {
		return fechaCargaScoreIN;
	}
	public void setFechaCargaScoreIN(String fechaCargaScoreIN) {
		this.fechaCargaScoreIN = fechaCargaScoreIN;
	}
	public String getScoreLegadoAM() {
		return scoreLegadoAM;
	}
	public void setScoreLegadoAM(String scoreLegadoAM) {
		this.scoreLegadoAM = scoreLegadoAM;
	}
	public String getFechaUpdateAM() {
		return fechaUpdateAM;
	}
	public void setFechaUpdateAM(String fechaUpdateAM) {
		this.fechaUpdateAM = fechaUpdateAM;
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
	public String getFechaUltimoUpdate() {
		return fechaUltimoUpdate;
	}
	public void setFechaUltimoUpdate(String fechaUltimoUpdate) {
		this.fechaUltimoUpdate = fechaUltimoUpdate;
	}
	public String getFechaBase() {
		return fechaBase;
	}
	public void setFechaBase(String fechaBase) {
		this.fechaBase = fechaBase;
	}	

}
