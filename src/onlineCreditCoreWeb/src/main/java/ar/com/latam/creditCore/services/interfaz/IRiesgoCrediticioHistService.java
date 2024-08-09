package ar.com.latam.creditCore.services.interfaz;

import java.util.List;
import java.util.Map;

import ar.com.latam.creditCore.dto.RiesgoCrediticioHistoricoDTO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public interface IRiesgoCrediticioHistService {
	
	void cargarConversionTipoDocumento(Map<String,String> conversionTipoDocumento) throws Exception;
	List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception;
	List<RiesgoCrediticioHistoricoDTO> obtenerClientesRiesgoCrediticioActualMasHist(String tipoDoc, String nroDoc, String genero, Map<String,String> conversionTipoDocumento_AMDOCS_DW) throws Exception;
}
