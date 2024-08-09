package ar.com.latam.creditCore.validador;

import org.apache.log4j.Logger;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import ar.com.latam.creditCore.dto.ClienteRiesgoCrediticioDTO;
import ar.com.latam.util.Util;

/**
 * 
 * @author diana.karina.rojas
 *
 */
public class ModificacionRiesgoCrediticioValidador implements Validator{

	private static final Logger log = Logger.getLogger(ModificacionRiesgoCrediticioValidador.class);
	
	private static final String CAMPO_SCORE_MANUAL = "scoreManual";
	private static final String ERROR_SCORE_MANUAL_NO_VALIDO = "error.campoScoreManual.noValido";
	
	public boolean supports(Class<?> clase) {
		return ClienteRiesgoCrediticioDTO.class.isAssignableFrom(clase);
	}

	public void validate(Object target, Errors errors){
		ClienteRiesgoCrediticioDTO beanCliente=(ClienteRiesgoCrediticioDTO) target;
		log.info("Se ingreso al metodo validate de ModificacionRiesgoCrediticioValidador");
		String score = validarNulos(beanCliente.getScoreManual()).trim();
		
		try{
			if (!(Util.EMPTY_CHARACTER.equals(score))){
				try{
					int scoreManual = Integer.parseInt(beanCliente.getScoreManual().trim());
					if (scoreManual < 0 || scoreManual> 999){
						errors.rejectValue(CAMPO_SCORE_MANUAL, ERROR_SCORE_MANUAL_NO_VALIDO);
					}
				}catch(NumberFormatException e){
					errors.rejectValue(CAMPO_SCORE_MANUAL, ERROR_SCORE_MANUAL_NO_VALIDO);
				}
			}
		}catch(Exception e){
			log.info(e);
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
