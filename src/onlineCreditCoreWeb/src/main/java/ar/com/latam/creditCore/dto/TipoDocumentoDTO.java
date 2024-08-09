package ar.com.latam.creditCore.dto;

import java.io.Serializable;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class TipoDocumentoDTO implements Serializable{
	
	/**
	 * Tabla RC_LKP_TIPO_DOC 
	 */
	
	private static final long serialVersionUID = 1L;
	
	/* CAMPO Party_Identification_Type_DW_Cd (Tipo de Documento en DW)*/
	private String tipoDocDW;
	
	//El siguiente campo se utiliza para guardar el valor original antes de la modificacion de tipo de documento:
	private String tipoDocDWBackup;
	
	/* CAMPO Party_Identification_Type_Cd (Tipo de Documento en AM)*/
	private String tipoDocAmdoc;
	
	//El siguiente campo se utiliza para guardar el valor original antes de la modificacion de tipo de documento:
	private String tipoDocAmdocBackup;

	/* CAMPO Oblig_Party_Gender_Type_Cd (Carga Obligatorio del Sexo). Valores: S o N (El campo no admite nulos)*/
	private String cargaGenero;
	
	private String tipoDocAmdocPipeCargaGenero;
	
	/* CAMPO User_Update_Cd (Usuario de Carga)*/
	private String userCarga;
	
	/* CAMPO Last_Updated_Dt (Fecha de la base)*/
	private String fechaCarga;
	
	public String getTipoDocDW() {
		return tipoDocDW;
	}
	public void setTipoDocDW(String tipoDocDW) {
		this.tipoDocDW = tipoDocDW;
	}
	public String getTipoDocAmdoc() {
		return tipoDocAmdoc;
	}
	public void setTipoDocAmdoc(String tipoDocAmdoc) {
		this.tipoDocAmdoc = tipoDocAmdoc;
	}	
	public String getCargaGenero() {
		return cargaGenero;
	}
	public void setCargaGenero(String cargaGenero) {
		this.cargaGenero = cargaGenero;
	}
	public String getTipoDocAmdocPipeCargaGenero() {
		return tipoDocAmdocPipeCargaGenero;
	}
	public void setTipoDocAmdocPipeCargaGenero(String tipoDocAmdocPipeCargaGenero) {
		this.tipoDocAmdocPipeCargaGenero = tipoDocAmdocPipeCargaGenero;
	}	
	public String getUserCarga() {
		return userCarga;
	}
	public void setUserCarga(String userCarga) {
		this.userCarga = userCarga;
	} 	
	public String getFechaCarga() {
		return fechaCarga;
	}
	public void setFechaCarga(String fechaCarga) {
		this.fechaCarga = fechaCarga;
	}
	public String getTipoDocDWBackup() {
		return tipoDocDWBackup;
	}
	public void setTipoDocDWBackup(String tipoDocDWBackup) {
		this.tipoDocDWBackup = tipoDocDWBackup;
	}
	public String getTipoDocAmdocBackup() {
		return tipoDocAmdocBackup;
	}
	public void setTipoDocAmdocBackup(String tipoDocAmdocBackup) {
		this.tipoDocAmdocBackup = tipoDocAmdocBackup;
	}

}
