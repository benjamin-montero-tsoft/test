package ar.com.latam.creditCore.services.impl;

import java.util.List;

import ar.com.latam.creditCore.dao.interfaz.INovedadesDAO;
import ar.com.latam.creditCore.dto.NovedadDTO;
import ar.com.latam.creditCore.services.interfaz.INovedadesService;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class NovedadesService implements INovedadesService {

	INovedadesDAO novedadesDAO;

	public INovedadesDAO getNovedadesDAO() {
		return novedadesDAO;
	}

	public void setNovedadesDAO(INovedadesDAO novedadesDAO) {
		this.novedadesDAO = novedadesDAO;
	}
	
	public List<NovedadDTO> consultaTodasNovedades() throws Exception{
		try{
			return novedadesDAO.consultaTodasNovedades();
		}catch (Exception e){
			throw e;
		}
	}
}
