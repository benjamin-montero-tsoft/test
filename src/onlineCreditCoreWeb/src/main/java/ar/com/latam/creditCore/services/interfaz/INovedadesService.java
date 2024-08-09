package ar.com.latam.creditCore.services.interfaz;

import java.util.List;

import ar.com.latam.creditCore.dto.NovedadDTO;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public interface INovedadesService {
	
	List<NovedadDTO> consultaTodasNovedades() throws Exception;

}
