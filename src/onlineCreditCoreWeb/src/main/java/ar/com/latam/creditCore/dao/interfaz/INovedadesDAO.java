package ar.com.latam.creditCore.dao.interfaz;

import java.util.List;

import ar.com.latam.creditCore.dto.NovedadDTO;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public interface INovedadesDAO {

	List<NovedadDTO> consultaTodasNovedades() throws Exception;
}
