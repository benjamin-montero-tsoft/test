package ar.com.latam.creditCore.dao.interfaz;

import java.util.List;
import java.util.Map;

import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.RiesgoCrediticioHistoricoDTO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;

public interface IRiesgoCrediticioHistDAO {
	
	void cargarConversionTipoDocumento(Map<String,String> conversionTipoDocumento) throws Exception;
	List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception;
	List<ClienteRiesgoCrediticioDTO> obtenerClientesRiesgoCrediticioActual(String tipoDoc, String nroDoc, String genero, Map<String,String> conversionTipoDocumento) throws Exception;
	List<RiesgoCrediticioHistoricoDTO> obtenerClientesRiesgoCrediticioHist(String tipoDoc, String nroDoc, String genero, Map<String,String> conversionTipoDocumento_AMDOCS_DW) throws Exception;
}
