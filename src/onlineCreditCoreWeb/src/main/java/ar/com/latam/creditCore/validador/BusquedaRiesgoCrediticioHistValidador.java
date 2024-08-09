package ar.com.latam.creditCore.validador;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.com.latam.creditCore.dto.RiesgoCrediticioHistoricoDTO;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class BusquedaRiesgoCrediticioHistValidador implements Validator{
	private static final Logger log = Logger.getLogger(BusquedaRiesgoCrediticioHistValidador.class);
	
	private static final String CAMPO_TIPO_DOC = "tipoDoc";
	private static final String CAMPO_NRO_DOC = "nroDoc";
	private static final String CAMPO_GENERO = "genero";
	private static final String ERROR_TIPO_DOC = "error.campoTipoDoc.vacio";	
	private static final String ERROR_NRO_DOC_VACIO = "error.campoNroDoc.vacio";	
	private static final String ERROR_GENERO_VACIO = "error.genero.vacio";
	
	public boolean supports(Class<?> clase) {
		return RiesgoCrediticioHistoricoDTO.class.isAssignableFrom(clase);
	}

	public void validate(Object target, Errors errors){
		RiesgoCrediticioHistoricoDTO beanCliente=(RiesgoCrediticioHistoricoDTO) target;
		log.info("Se ingreso al metodo validate de BusquedaRiesgoCrediticioHistValidador");
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
		
		if (Util.SI.equals(cargaGenero)){
			if (Util.EMPTY_CHARACTER.equals(genero)){
				errors.rejectValue(CAMPO_GENERO, ERROR_GENERO_VACIO);
			}				
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
