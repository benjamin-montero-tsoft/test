package ar.com.latam.creditCore.services.impl;

import java.util.List;

import org.apache.log4j.Logger;

import ar.com.latam.creditCore.dao.interfaz.ITiposDocumentoDAO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;
import ar.com.latam.creditCore.services.interfaz.ITiposDocumentoService;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class TiposDocumentoService implements ITiposDocumentoService {

	private static final Logger LOG = Logger.getLogger(TiposDocumentoService.class);
	
	ITiposDocumentoDAO tiposDocumentoDAO;

	public ITiposDocumentoDAO getTiposDocumentoDAO() {
		return tiposDocumentoDAO;
	}

	public void setTiposDocumentoDAO(ITiposDocumentoDAO tiposDocumentoDAO) {
		this.tiposDocumentoDAO = tiposDocumentoDAO;
	}
	
	public List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception{
		try{
			return tiposDocumentoDAO.obtenerTiposDocumento();
		}catch (Exception e){
			LOG.info("Error en obtenerTiposDocumento de tiposDocumentoDAO");
			throw e;
		}			
	}
	
	public void modificacionTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception{
		try{
			tipoDocumento.setFechaCarga(Util.SYSDATE);
			tiposDocumentoDAO.modificacionTipoDoc(tipoDocumento);
		}catch (Exception e){
			LOG.info("Error en modificacionTipoDoc de tiposDocumentoDAO");
			throw e;
		}			
	}
	
	public void altaTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception{
		try{
			tipoDocumento.setFechaCarga(Util.SYSDATE);
			tiposDocumentoDAO.altaTipoDoc(tipoDocumento);
		}catch (Exception e){
			LOG.info("Error en altaTipoDoc de tiposDocumentoDAO");
			throw e;
		}		
	}
	
	public void eliminarTipoDoc(TipoDocumentoDTO tipoDocumento)throws Exception{
		try{
			tiposDocumentoDAO.eliminarTipoDoc(tipoDocumento);
		}catch (Exception e){
			LOG.info("Error en eliminarTipoDoc de tiposDocumentoDAO");
			throw e;
		}
	}
}
