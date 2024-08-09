package ar.com.latam.creditCore.dto;

import java.io.Serializable;

import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class ClienteRiesgoCrediticioDTO implements Serializable{
	
	/**
	 * Tabla RC_RIESGO_CREDITICIO_FINAL
	 */
	
	private static final long serialVersionUID = 1L;
	
	/* CAMPO Party_Identification_Type_Cd (Tipo de Documento)*/
	private String tipoDoc;
	
	private String tipoDocAMDOCS;
	
	/* CAMPO Party_Identification_Num (Nro de Documento)*/
	private String nroDoc;
	
	/* CAMPO Party_Gender_Type_Cd (Sexo)*/
	private String genero;
	
	/* CAMPO Party_Credit_Score_IN_Ind (Score calculado)*/
	private String scoreCalculadoIN;
	
	/*Variable para ser utilizada en la grabacion del historico*/
	private String fechaCargaScoreINBase;
	
	/* CAMPO Last_Updated_IN_Dt (Fecha carga del score IN)*/
	private String fechaCargaScoreIN;
	
	/* CAMPO Party_Credit_Score_AM_Ind (Score legado o Score T3)*/
	private String scoreLegadoAM;
	
	/*Variable para ser utilizada en la grabacion del historico*/
	private String fechaUpdateAMBase;
	
	/* CAMPO Last_Updated_AM_Dt (Fecha de ultimo update o Fecha carga del Score T3)*/
	private String fechaUpdateAM;
	
	/* CAMPO Party_Credit_Score_M_Ind (Score manual)*/
	private String scoreManual;
	
	/*Variable para ser utilizada en la grabacion del historico*/
	private String fechaCargaScoreManualBase;
	
	/* CAMPO Update_Manual_M_Dt (Fecha carga del score manual)*/
	private String fechaCargaScoreManual;
	
	/* CAMPO User_Update_M_Cd (Usuario de carga)*/
	private String usuarioCargaCambio;
	
	/*Variable para ser utilizada en la grabacion del historico*/
	private String fechaUltimoUpdateBase;	
	
	/* CAMPO Last_Updated_F_Dt (Fecha de ultimo update)*/
	private String fechaUltimoUpdate;
	
	/* FechaDesde para realizar bajada*/
	private String fechaDesde;
	
	/* FechaHasta para realizar bajada*/
	private String fechaHasta;
	
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
	public String getScoreCalculadoIN() {
		return scoreCalculadoIN;
	}
	public void setScoreCalculadoIN(String scoreCalculadoIN) {
		this.scoreCalculadoIN = scoreCalculadoIN;
	}
	public String getFechaCargaScoreINBase() {
		return fechaCargaScoreINBase;
	}
	public void setFechaCargaScoreINBase(String fechaCargaScoreINBase) {
		this.fechaCargaScoreINBase = fechaCargaScoreINBase;
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
	public String getFechaUpdateAMBase() {
		return fechaUpdateAMBase;
	}
	public void setFechaUpdateAMBase(String fechaUpdateAMBase) {
		this.fechaUpdateAMBase = fechaUpdateAMBase;
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
	public String getFechaCargaScoreManualBase() {
		return fechaCargaScoreManualBase;
	}
	public void setFechaCargaScoreManualBase(String fechaCargaScoreManualBase) {
		this.fechaCargaScoreManualBase = fechaCargaScoreManualBase;
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
	public String getFechaUltimoUpdateBase() {
		return fechaUltimoUpdateBase;
	}
	public void setFechaUltimoUpdateBase(String fechaUltimoUpdateBase) {
		this.fechaUltimoUpdateBase = fechaUltimoUpdateBase;
	}	
	public String getFechaUltimoUpdate() {
		return fechaUltimoUpdate;
	}
	public void setFechaUltimoUpdate(String fechaUltimoUpdate) {
		this.fechaUltimoUpdate = fechaUltimoUpdate;
	}
	public String getfechaDesde() {
		return fechaDesde;
	}
	public void setfechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getfechaHasta() {
		return fechaHasta;
	}
	public void setfechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
}
