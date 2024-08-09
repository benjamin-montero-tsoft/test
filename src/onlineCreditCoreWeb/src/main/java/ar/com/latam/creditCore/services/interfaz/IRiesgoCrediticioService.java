package ar.com.latam.creditCore.services.interfaz;

import java.util.List;
import java.util.Map;

import ar.com.latam.creditCore.dto.AltaManualClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.ExportCSVDTO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public interface IRiesgoCrediticioService {
	
	void cargarConversionTipoDocumento(Map<String,String> conversionTipoDocumento) throws Exception;
	List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception;
	boolean existeNroDocumento(String tipoDoc, String nroDoc) throws Exception;
	void altaClienteRiesgoCrediticio(AltaManualClienteRiesgoCrediticioDTO altaClienteRiesgoCrediticio)throws Exception;
	List<ClienteRiesgoCrediticioDTO> obtenerClientesRiesgoCrediticio(String tipoDoc, String nroDoc, String genero, Map<String,String> conversionTipoDocumento) throws Exception;
	List<ClienteRiesgoCrediticioDTO> modificacionScoreClienteRiesgoCrediticio(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio, Map<String,String> conversionTipoDocumento)throws Exception;
	void modificacionClienteRiesgoCrediticio(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio, Map<String,String> conversionTipoDocumento)throws Exception;
	List<ClienteRiesgoCrediticioDTO> obtenerReporteRiesgosCrediticiosManuales(ExportCSVDTO dto) throws Exception;
	
}
