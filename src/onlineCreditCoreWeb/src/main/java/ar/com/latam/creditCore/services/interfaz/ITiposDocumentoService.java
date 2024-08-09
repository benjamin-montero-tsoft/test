package ar.com.latam.creditCore.services.interfaz;

import java.util.List;

import ar.com.latam.creditCore.dto.TipoDocumentoDTO;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public interface ITiposDocumentoService {

	List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception;
	void modificacionTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception;
	void altaTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception;
	void eliminarTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception;
}
