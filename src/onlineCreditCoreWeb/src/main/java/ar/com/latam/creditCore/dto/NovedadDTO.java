package ar.com.latam.creditCore.dto;

import java.io.Serializable;

import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class NovedadDTO implements Serializable{
	
	/**
	 * Tabla RC_FEEDBACK_AM	 
	 */
	private static final long serialVersionUID = 1L;
	
	/* CAMPO Party_Identification_Type_Cd (Tipo de Documento)*/
	private String tipoDoc;
	
	/* CAMPO Party_Identification_Num (Nro de Documento)*/
	private String nroDoc;
	
	/* CAMPO Party_Gender_Type_Cd (Sexo)*/
	private String genero;
	
	/* CAMPO Party_Credit_Score_Ind (Score enviado)*/
	private String scoreEnv;
	
	/* CAMPO Error_Desc (Descripcion de Error)*/
	private String descripcionError;
	
	/* CAMPO Last_Updated_IN_Dt (Fecha carga)*/
	private String fechaCarga;
	
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
	public String getScoreEnv() {
		return scoreEnv;
	}
	public void setScoreEnv(String scoreEnv) {
		this.scoreEnv = scoreEnv;
	}
	public String getDescripcionError() {
		return descripcionError;
	}
	public void setDescripcionError(String descripcionError) {
		this.descripcionError = descripcionError;
	}
	public String getFechaCarga() {
		return fechaCarga;
	}
	public void setFechaCarga(String fechaCarga) {
		this.fechaCarga = fechaCarga;
	}

}
