package ar.com.latam.creditCore.validador;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.com.latam.creditCore.dto.BusquedaRiesgoCrediticioDTO;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class BusquedaRiesgoCrediticioValidador implements Validator{
	private static final Logger log = Logger.getLogger(BusquedaRiesgoCrediticioValidador.class);
	
	private static final String CAMPO_TIPO_DOC = "tipoDoc";
	private static final String CAMPO_NRO_DOC = "nroDoc";
	private static final String CAMPO_GENERO = "genero";
	private static final String ERROR_TIPO_DOC = "error.campoTipoDoc.vacio";	
	private static final String ERROR_NRO_DOC_VACIO = "error.campoNroDoc.vacio";
	private static final String ERROR_GENERO_VACIO = "error.genero.vacio";
	
	/*private static final String CAMPO_BAJADA_MANUAL_DESDE = "fechaDesde";
	private static final String CAMPO_BAJADA_MANUAL_HASTA = "fechaHasta";
	private static final String ERROR_FECHAS_INVALIDAS = "error.fechas.invalidas";
	private static final String ERROR_FECHAS_INTERVALO_SUPERIOR = "error.intervalo.fechas";
	private static final String ERROR_FECHA_DESDE_MAYOR = "error.fecha.desde.mayor";
	private static final String ERROR_FECHA_HASTA_MENOR = "error.fecha.hasta.menor";
	private static final String FECHA_INTERVALO = "fecha.intervalo";*/
	
	public boolean supports(Class<?> clase) {
		return BusquedaRiesgoCrediticioDTO.class.isAssignableFrom(clase);
	}

	public void validate(Object target, Errors errors){
		BusquedaRiesgoCrediticioDTO beanCliente=(BusquedaRiesgoCrediticioDTO) target;
		log.info("Se ingreso al metodo validate de BusquedaRiesgoCrediticioValidador");
		
		String tipoDoc = validarNulos(beanCliente.getTipoDoc()).trim();
		String nroDoc = validarNulos(beanCliente.getNroDoc()).trim();		
		String cargaGenero = validarNulos(beanCliente.getCargaGenero()).trim();
		String genero = validarNulos(beanCliente.getGenero()).trim();
		
		if (Util.EMPTY_CHARACTER.equals(tipoDoc)){
			errors.rejectValue(CAMPO_TIPO_DOC, ERROR_TIPO_DOC);
		}
		
		if (Util.EMPTY_CHARACTER.equals(nroDoc)){
			errors.rejectValue(CAMPO_NRO_DOC, ERROR_NRO_DOC_VACIO);
		}
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
