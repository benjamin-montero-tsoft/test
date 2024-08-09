package ar.com.latam.creditCore.dto;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

public class ExportCSVDTO {

	private HttpServletResponse response;
	private String title;
	private String fechaDesde;
	private String fechaHasta;
	private Map<String,String> conversionTipoDocumento_DW_AMDOCS;
	
	
	public Map<String, String> getConversionTipoDocumento_DW_AMDOCS() {
		return conversionTipoDocumento_DW_AMDOCS;
	}
	public void setConversionTipoDocumento_DW_AMDOCS(
			Map<String, String> conversionTipoDocumento_DW_AMDOCS) {
		this.conversionTipoDocumento_DW_AMDOCS = conversionTipoDocumento_DW_AMDOCS;
	}
	
	public HttpServletResponse getResponse() {
		return response;
	}
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
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
