package ar.com.latam.creditCore.utils;

import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.creditCore.dto.ExportCSVDTO;

/**
 * Clase para exportar en CVS.
 * @author diana.karina.rojas
 *
 */
public class ExportCSV {
	
	private static final Logger LOG = Logger.getLogger(ExportCSV.class);
	


	private StringBuffer createBody(ClienteRiesgoCrediticioDTO clienteRiesgoCrediticio){
		StringBuffer sb = new StringBuffer("");
		sb.append(clienteRiesgoCrediticio.getTipoDocAMDOCS()+ ",");
        sb.append(clienteRiesgoCrediticio.getNroDoc()+ ",");
        sb.append(clienteRiesgoCrediticio.getGenero()+ ",");
        sb.append(clienteRiesgoCrediticio.getScoreCalculadoIN()+ ",");
        sb.append(validarNulos(clienteRiesgoCrediticio.getFechaCargaScoreIN())+ ",");
        sb.append(clienteRiesgoCrediticio.getScoreLegadoAM()+ ",");
        sb.append(validarNulos(clienteRiesgoCrediticio.getFechaUpdateAM())+ ",");
        sb.append(clienteRiesgoCrediticio.getScoreManual()+ ",");
        sb.append(validarNulos(clienteRiesgoCrediticio.getFechaCargaScoreManual())+ ",");
        sb.append(clienteRiesgoCrediticio.getUsuarioCargaCambio()+ ",");
        sb.append(clienteRiesgoCrediticio.getFechaUltimoUpdate());

        return sb;
	}
	
	/**
	 *
	 * @param dato
	 * @return
	 */
	public String validarNulos(String dato){


		if (dato == null)
			return "";
		else
			return dato;
	}	
}
