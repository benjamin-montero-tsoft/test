package ar.com.latam.creditCore.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.com.latam.creditCore.dao.interfaz.IRiesgoCrediticioHistDAO;
import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.RiesgoCrediticioHistoricoDTO;
import ar.com.latam.creditCore.dto.TipoDocumentoDTO;
import ar.com.latam.creditCore.services.interfaz.IRiesgoCrediticioHistService;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class RiesgoCrediticioHistService implements IRiesgoCrediticioHistService {
	
	private static final Logger LOG = Logger.getLogger(RiesgoCrediticioHistService.class);
	
	IRiesgoCrediticioHistDAO riesgoCrediticioHistDAO;

	public IRiesgoCrediticioHistDAO getRiesgoCrediticioHistDAO() {
		return riesgoCrediticioHistDAO;
	}

	public void setRiesgoCrediticioHistDAO(IRiesgoCrediticioHistDAO riesgoCrediticioHistDAO) {
		this.riesgoCrediticioHistDAO = riesgoCrediticioHistDAO;
	}
	
	public void cargarConversionTipoDocumento(Map<String,String> conversionTipoDocumento) throws Exception{
		try{
			riesgoCrediticioHistDAO.cargarConversionTipoDocumento(conversionTipoDocumento);
		}catch (Exception e){
			LOG.info("Error en cargarConversionTipoDocumento de riesgoCrediticioHistDAO");
			throw e;
		}		
	}	
	
	public List<TipoDocumentoDTO> obtenerTiposDocumento() throws Exception{
		try{
			return riesgoCrediticioHistDAO.obtenerTiposDocumento();
		}catch (Exception e){
			LOG.info("Error en obtenerTiposDocumento de riesgoCrediticioHistDAO");
			throw e;
		}			
	}
	
	public List<RiesgoCrediticioHistoricoDTO> obtenerClientesRiesgoCrediticioActualMasHist(String tipoDoc, String nroDoc, String genero, Map<String,String> conversionTipoDocumento_AMDOCS_DW) throws Exception{
		try{
			List<RiesgoCrediticioHistoricoDTO> listaClientesRiesgoCredActualMasHist= new ArrayList<RiesgoCrediticioHistoricoDTO>();
			List<ClienteRiesgoCrediticioDTO> listaClientesRiesgoCredActual= null;
			List<RiesgoCrediticioHistoricoDTO> listaClientesRiesgoCredHist= null;
			listaClientesRiesgoCredActual = riesgoCrediticioHistDAO.obtenerClientesRiesgoCrediticioActual(tipoDoc, nroDoc, genero, conversionTipoDocumento_AMDOCS_DW);
			if(listaClientesRiesgoCredActual != null){
				RiesgoCrediticioHistoricoDTO registroActual = new RiesgoCrediticioHistoricoDTO();
				registroActual.setTipoDoc(listaClientesRiesgoCredActual.get(0).getTipoDoc());
				registroActual.setTipoDocAMDOCS(listaClientesRiesgoCredActual.get(0).getTipoDocAMDOCS());
				registroActual.setNroDoc(listaClientesRiesgoCredActual.get(0).getNroDoc());
				registroActual.setGenero(listaClientesRiesgoCredActual.get(0).getGenero());
				registroActual.setScoreCalculadoIN(listaClientesRiesgoCredActual.get(0).getScoreCalculadoIN());
				registroActual.setFechaCargaScoreIN(listaClientesRiesgoCredActual.get(0).getFechaCargaScoreIN());
				registroActual.setScoreLegadoAM(listaClientesRiesgoCredActual.get(0).getScoreLegadoAM());
				registroActual.setFechaUpdateAM(listaClientesRiesgoCredActual.get(0).getFechaUpdateAM());
				registroActual.setScoreManual(listaClientesRiesgoCredActual.get(0).getScoreManual());
				registroActual.setFechaCargaScoreManual(listaClientesRiesgoCredActual.get(0).getFechaCargaScoreManual());
				registroActual.setUsuarioCargaCambio(listaClientesRiesgoCredActual.get(0).getUsuarioCargaCambio());
				registroActual.setFechaUltimoUpdate(listaClientesRiesgoCredActual.get(0).getFechaUltimoUpdate());

				listaClientesRiesgoCredActualMasHist.add(registroActual);
			}
			listaClientesRiesgoCredHist = riesgoCrediticioHistDAO.obtenerClientesRiesgoCrediticioHist(tipoDoc, nroDoc, genero, conversionTipoDocumento_AMDOCS_DW);
			if(listaClientesRiesgoCredHist != null){
				listaClientesRiesgoCredActualMasHist.addAll(listaClientesRiesgoCredHist);
			}
			return listaClientesRiesgoCredActualMasHist;
		}catch (Exception e){
			LOG.info("Error en obtenerClientesRiesgoCrediticioHist de riesgoCrediticioHistDAO");
			LOG.info(e);
			throw e;
		}
	}
	
}
