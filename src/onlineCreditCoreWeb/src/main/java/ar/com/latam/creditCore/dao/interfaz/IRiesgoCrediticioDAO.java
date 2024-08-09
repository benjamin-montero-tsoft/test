package ar.com.latam.creditCore.dao.interfaz;

import java.util.List;
import java.util.Map;

import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.ExportCSVDTO;
import ar.com.latam.creditCore.dto.RiesgoCrediticioManualDTO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public interface IRiesgoCrediticioDAO {
	
	void cargarConversionTipoDocumento(Map<String,String> conversionTipoDocumento) throws Exception;
	List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception;	
	boolean existeNroDocumento(String tipoDoc, String nroDoc) throws Exception;
	List<ClienteRiesgoCrediticioDTO> existeNroDocumentoResultado(String tipoDoc, String nroDoc) throws Exception;
	void altaClienteRiesgoCrediticioManual(RiesgoCrediticioManualDTO riesgoCrediticioManual)throws Exception;	
	void altaClienteRiesgoCrediticioFinal(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio)throws Exception;
	List<ClienteRiesgoCrediticioDTO> obtenerClientesRiesgoCrediticio(String tipoDocAMDOCS, String nroDoc, String genero,Map<String,String> conversionTipoDocumento) throws Exception;
	int modificacionClienteRiesgoCrediticioManual(RiesgoCrediticioManualDTO riesgoCrediticioManual)throws Exception;
	void altaClienteRiesgoCrediticioHistorico(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio, Map<String,String> conversionTipoDocumento)throws Exception;
	void modificacionClienteRiesgoCrediticioFinal(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio)throws Exception;	
	List<ClienteRiesgoCrediticioDTO> obtenerReporteRiesgosCrediticiosManuales(ExportCSVDTO dto) throws Exception;
	public Map<String,String> cargarConversionTipoDocumentoBajadaManual() throws Exception;
}
