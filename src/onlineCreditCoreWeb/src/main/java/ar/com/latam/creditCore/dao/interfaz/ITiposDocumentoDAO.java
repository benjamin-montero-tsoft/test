package ar.com.latam.creditCore.dao.interfaz;

import java.util.List;

import ar.com.latam.creditCore.dto.TipoDocumentoDTO;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public interface ITiposDocumentoDAO {

	List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception;
	void modificacionTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception;
	void altaTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception;
	void eliminarTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception;
}
